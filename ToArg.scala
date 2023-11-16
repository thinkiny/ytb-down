import java.nio.file.Paths

type ToArgFunc[T] = T => Option[String]

object ToArgFunc:
  def make[T](t: T => Option[String]): ToArgFunc[T] = t
  def toggle(arg: String) = make[Boolean]: b =>
    if (b) Some(arg) else None

  def apply[V](key: ConfigKey[V]): ToArgFunc[V] =
    import ConfigKey.*
    key match
      case Proxy   => toggle("--proxy http://127.0.0.1:1087")
      case AutoSub => toggle("--write-auto-subs --convert-subs srt")
      case Retry =>
        toggle(
          "--fragment-retries infinite -R infinite --file-access-retries infinite"
        )
      case Mp4 => toggle("--remux-video mp4")
      case Prefix =>
        make[String]: p =>
          p match
            case "." => {
              val folderName =
                Paths.get("").toAbsolutePath().getFileName().toString()
              val prefix = if (folderName.isEmpty) then "" else folderName + "-"
              Some(s"-o '${prefix}%(playlist_index)s-%(title)s.%(ext)s'")
            }
            case fix if p.nonEmpty => {
              Some(s"-o '${fix}-%(title)s.%(ext)s'")
            }
            case "_" => {
              Some("-o '%(playlist_index)s-%(title)s.%(ext)s'")
            }
      case Connection =>
        make[Int]: num =>
          Some(s"-N ${num}")
      case Cookie => toggle("--cookies-from-browser firefox")
      case Format =>
        make[String]: i =>
          Some(s"-f ${i}")
