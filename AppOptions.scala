import caseapp.core.Error.MalformedValue
import caseapp.core.argparser.ArgParser
import caseapp.core.argparser.SimpleArgParser

import scala.util.Try
import java.net.URI

object AppOptions:
  given ArgParser[URI] = SimpleArgParser.from[URI]("url") { s =>
    Try(URI.create(s)).toEither.left.map(t =>
      MalformedValue("URL", t.getMessage())
    )
  }

case class AppOptions(
    url: URI,
    noAutoSub: Boolean = false,
    proxy: Boolean = false,
    prefix: String = "",
    format: String = "",
    mp4: Boolean = false,
    listFormat: Boolean = false,
    from: Option[Int] = None,
    cookie: Boolean = false
):
  def getPredefConfig(): ConfigSet =
    url.getHost() match
      case "www.youtube.com" => ProxyConfig += ConfigEntry[RecodeMp4](true)
      case _                 => DemosticConfig

  def toYtArgs(): String =
    if (listFormat) then "--list-formats --cookies-from-browser firefox"
    else {
      val configs = getPredefConfig()
      if (noAutoSub) configs += ConfigEntry[AutoSub](false)
      if (proxy) configs += ConfigEntry[Proxy](false)
      if (prefix.nonEmpty)
        configs += ConfigEntry[Prefix](prefix)
      if (format.nonEmpty)
        configs += ConfigEntry[Format](format)
      if (cookie)
        configs += ConfigEntry[Cookie](true)

      from match
        case Some(i) => configs += ConfigEntry[From](i)
        case _       =>

      if (mp4) then configs += ConfigEntry[RecodeMp4](true)
      configs.optionMap.values
        .flatMap(_.toYtArg())
        .mkString(" ")
    }
