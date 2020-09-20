package controllers

import java.io.FileNotFoundException
import java.nio.file.{Path, Paths}

import javax.inject.Inject
import play.api.{Configuration, Logger}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.xml.Elem

class MainController @Inject() (cc: ControllerComponents, conf: Configuration) extends AbstractController(cc) {

    private val logger = Logger(classOf[MainController])

    private val configBase: Path = Paths.get(conf.get[String]("phone-config-path"))
    private val configOverridesBase = configBase.resolve("overrides")

    def getConfiguration(phoneType: String, mac: String) = Action { implicit req =>

        val normalizedMacAddr = mac.replaceAll("[^a-fA-F0-9]", "").toUpperCase

        logger.info(s"Configuration request received by ${req.remoteAddress}: phoneType=$phoneType device=$normalizedMacAddr")

        val typeConfPath = configBase.resolve(phoneType + ".xml")
        val deviceConfPath = configOverridesBase.resolve(normalizedMacAddr + ".xml")

        logger.info(s"Loading type configuration $typeConfPath")
        logger.info(s"Loading device configuration $deviceConfPath")

        val typeConf = loadXML(typeConfPath)
        val deviceConf = loadXML(deviceConfPath)

        (typeConf, deviceConf) match {
            case (Some(t), None) =>
                logger.warn("phone type configuration found, but no device configuration")
                Ok(t)
            case (None, Some(d)) =>
                logger.warn("device configuration found, but no phone type configuration")
                Ok(d)
            case (Some(t), Some(d)) =>
                Ok(mergeTrees(t, d))
            case (None, None) =>
                logger.error("neither phone type nor device configuration found!")
                NotFound
        }
    }

    private def loadXML(path: Path): Option[Elem] = try {
        Some(scala.xml.XML.loadFile(path.toFile))
    } catch {
        case _: FileNotFoundException => None
    }

    private def mergeTrees(left: Elem, right: Elem): Elem = {
        left.copy(child = left.child ++ right.child)
    }
}
