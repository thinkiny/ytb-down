import java.net.URL
import caseapp.core.argparser.ArgParser
import caseapp.core.argparser.SimpleArgParser
import scala.util.Try
import caseapp.core.Error.MalformedValue

import ConfigKey.*

object AppOptions:
  given ArgParser[URL] = SimpleArgParser.from[URL]("url") { s =>
    Try(URL(s)).toEither.left.map(t => MalformedValue("URL", t.getMessage()))
  }

case class AppOptions(
    url: URL,
    noAutoSub: Boolean = false,
    proxy: Boolean = false,
    prefix: Boolean = false,
    format: String = ""
):
  def getPredefConfig(): ConfigSet =
    url.getHost() match
      case "www.youtube.com" => ProxyConfig
      case _                 => DemosticConfig

  def toArgs(): String =
    val userFlags = List.newBuilder[ConfigEntry[_]]
    if (noAutoSub) userFlags += ConfigEntry(AutoSub, false)
    if (proxy) userFlags += ConfigEntry(Proxy, false)
    if (prefix) userFlags += ConfigEntry(Prefix, true)
    if (format.nonEmpty) userFlags += ConfigEntry(Format, format)
    (getPredefConfig() ++ userFlags.result()).values
      .flatMap(_.toArg)
      .mkString(" ")
