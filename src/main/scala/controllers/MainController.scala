package controllers

import java.nio.file.{Path, Paths}

import javax.inject.Inject
import play.api.Configuration
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.xml.Elem

class MainController @Inject() (cc: ControllerComponents, conf: Configuration) extends AbstractController(cc) {

    private val configBase: Path = Paths.get(conf.get[String]("phone-config-path"))
    private val configOverridesBase = configBase.resolve("overrides")

    def getConfiguration(phoneType: String, mac: String) = Action {

        val normalizedMacAddr = mac.replaceAll("[^a-fA-F0-9]", "").toUpperCase
        val typeConfPath = configBase.resolve(phoneType + ".xml")
        val deviceConfPath = configOverridesBase.resolve(normalizedMacAddr + ".xml")

        val typeConf = loadXML(typeConfPath)
        val deviceConf = loadXML(deviceConfPath)

        Ok(mergeTrees(typeConf, deviceConf))
    }

    private def loadXML(path: Path): Elem = scala.xml.XML.loadFile(path.toFile)

    private def mergeTrees(left: Elem, right: Elem): Elem = {
        left.copy(child = left.child ++ right.child)
    }
}
