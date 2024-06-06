import controllers.{AssetsComponents, MainController}
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}

class AutosnomLoader extends ApplicationLoader {
  override def load(context: ApplicationLoader.Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment, context.initialConfiguration, Map.empty)
    }
    new AutosnomComponents(context).application
  }
}

class AutosnomComponents(context: ApplicationLoader.Context)
  extends BuiltInComponentsFromContext(context) {

  override def httpFilters: Seq[EssentialFilter] = Seq.empty

  private val main = MainController(controllerComponents, configuration)
  // The router
  override def router: Router = new _root_.router.Routes(
    httpErrorHandler, main
  )
}