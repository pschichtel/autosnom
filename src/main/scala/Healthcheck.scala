import java.net.URI
import java.net.http.HttpResponse.BodyHandlers
import java.net.http.{HttpClient, HttpRequest}

object Healthcheck {
  def main(args: Array[String]): Unit = {
    val client = HttpClient.newHttpClient()
    val req = HttpRequest.newBuilder(URI(args(0))).GET().build()
    val res = client.send(req, BodyHandlers.ofString())
    val exitWith =
      if (res.statusCode() == 200 && res.body == "OK") 0
      else 1
    System.exit(exitWith)
  }
}