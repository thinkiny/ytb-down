import caseapp.core.Error.MalformedValue
import caseapp.core.argparser.ArgParser
import caseapp.core.argparser.SimpleArgParser

import java.net.URL
import scala.util.Try

object AppOptions:
  given ArgParser[URL] = SimpleArgParser.from[URL]("url") { s =>
    Try(URL(s)).toEither.left.map(t => MalformedValue("URL", t.getMessage()))
  }

case class AppOptions(
    url: URL,
    noAutoSub: Boolean = false,
    proxy: Boolean = false,
    prefix: String = "",
    format: String = "",
    listFormat: Boolean = false
):
  def getPredefConfig(): ConfigSet =
    url.getHost() match
      case "www.youtube.com" => ProxyConfig
      case _                 => DemosticConfig

  def toYtArgs(): String =
    if (listFormat) then "--list-formats --cookies-from-browser firefox"
    else {
      val userConfig = List.newBuilder[ConfigEntry[_]]
      if (noAutoSub) userConfig += ConfigEntry[AutoSub](false)
      if (proxy) userConfig += ConfigEntry[Proxy](false)
      if (prefix.nonEmpty)
        userConfig += ConfigEntry[Prefix](prefix)
      if (format.nonEmpty)
        userConfig += ConfigEntry[Format](format)
      (getPredefConfig() ++ userConfig.result()).values
        .flatMap(_.toYtArg())
        .mkString(" ")
    }
