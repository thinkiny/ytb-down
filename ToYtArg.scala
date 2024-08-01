import java.nio.file.Paths

trait ToYtArg[K, V]:
  def toArg(v: V): Option[String]

object ToYtArg:
  class ToggleFlag[K <: ConfigKey](flag: String) extends ToYtArg[K, Boolean]:
    def toArg(v: Boolean): Option[String] =
      if (v) Some(flag) else None

  given ToYtArg[Proxy, Boolean] =
    new ToggleFlag[Proxy]("--proxy http://127.0.0.1:1087")
  given ToYtArg[AutoSub, Boolean] =
    new ToggleFlag[AutoSub]("--write-auto-subs --convert-subs srt")
  given ToYtArg[Retry, Boolean] = new ToggleFlag[Retry](
    "--fragment-retries infinite -R infinite --file-access-retries infinite"
  )
  given ToYtArg[RemuxMp4, Boolean] =
    new ToggleFlag[RemuxMp4]("--remux-video mp4")

  given ToYtArg[Connection, Int] = new {
    def toArg(v: Int): Option[String] = Some(
      s"-N ${v}"
    )
  }

  given ToYtArg[Cookie, Boolean] =
    new ToggleFlag[Cookie]("--cookies-from-browser firefox")

  given ToYtArg[Format, String] = new {
    def toArg(v: String): Option[String] = Some(
      s"-f ${v}"
    )
  }

  given ToYtArg[RecodeMp4, Boolean] =
    new ToggleFlag[RecodeMp4]("--recode-video mp4")

  given ToYtArg[Prefix, String] = new {
    def getCurrentFolder(): String =
      val folderName =
        Paths.get("").toAbsolutePath().getFileName().toString()
      if (folderName.isEmpty) then "" else folderName + "-"

    def toArg(v: String): Option[String] =
      Some(v match
        case "." =>
          s"-o '${getCurrentFolder()}%(playlist_index)s-%(title)s.%(ext)s'"

        case prefix if prefix.nonEmpty =>
          s"-o '${prefix}-%(title)s.%(ext)s'"
        case _ =>
          "-o '%(playlist_index)s-%(title)s.%(ext)s'"
      )
  }

  given ToYtArg[From, Int] = new {
    def toArg(v: Int): Option[String] = Some(s"-I ${v}::")
  }
