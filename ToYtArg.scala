import java.nio.file.Paths

trait ToYtArg[T]:
  def toArg(t: T): Option[String]

object ToYtArg:
  type ToArgFunc = [K <: ConfigKey, V] =>> ToYtArg[ConfigEntry[K, V]]

  class ToggleFlag[K <: ConfigKey](flag: String)
      extends ToYtArg[ConfigEntry[K, Boolean]]:
    def toArg(t: ConfigEntry[K, Boolean]): Option[String] =
      if (t.value) Some(flag) else None

  given ToArgFunc[Proxy, Boolean] =
    new ToggleFlag[Proxy]("--proxy http://127.0.0.1:1087")
  given ToArgFunc[AutoSub, Boolean] =
    new ToggleFlag[AutoSub]("--write-auto-subs --convert-subs srt")
  given ToArgFunc[Retry, Boolean] = new ToggleFlag[Retry](
    "--fragment-retries infinite -R infinite --file-access-retries infinite"
  )
  given ToArgFunc[RemuxMp4, Boolean] =
    new ToggleFlag[RemuxMp4]("--remux-video mp4")

  given ToArgFunc[Connection, Int] = new {
    def toArg(e: ConfigEntry[Connection, Int]): Option[String] = Some(
      s"-N ${e.value}"
    )
  }

  given ToArgFunc[Cookie, Boolean] =
    new ToggleFlag[Cookie]("--cookies-from-browser firefox")
  given ToArgFunc[Format, String] = new {
    def toArg(e: ConfigEntry[Format, String]): Option[String] = Some(
      s"-f ${e.value}"
    )
  }

  given ToArgFunc[RecodeMp4, Boolean] =
    new ToggleFlag[RecodeMp4]("--recode-video mp4")

  given ToArgFunc[Prefix, String] = new {
    def getCurrentFolder(): String =
      val folderName =
        Paths.get("").toAbsolutePath().getFileName().toString()
      if (folderName.isEmpty) then "" else folderName + "-"

    def toArg(e: ConfigEntry[Prefix, String]): Option[String] =
      Some(e.value match
        case "." =>
          s"-o '${getCurrentFolder()}%(playlist_index)s-%(title)s.%(ext)s'"

        case prefix if prefix.nonEmpty =>
          s"-o '${prefix}-%(title)s.%(ext)s'"
        case _ =>
          "-o '%(playlist_index)s-%(title)s.%(ext)s'"
      )
  }
