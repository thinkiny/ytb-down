import caseapp.CaseApp
import caseapp.core.RemainingArgs
import scala.collection.mutable.ListBuffer
import sys.process.*

case class AppOptions(
    noAutoSub: Boolean = false,
    noProxy: Boolean = false,
    conn: Int = 5,
    cookie: Boolean = false
):
  def toArgs(): Seq[String] =
    val args = ListBuffer[String](
      "-o '%(playlist_index)s-%(title)s.%(ext)s'",
      // "-f 'bv*[ext=mp4]+ba[ext=m4a]/b[ext=mp4] / bv*+ba/b'",
      "-f bestvideo+bestaudio",
      "--recode-video mp4",
      "--fragment-retries infinite -R infinite --file-access-retries infinite",
      s"-N ${conn}"
    )

    if (!noAutoSub) then args += "--write-auto-subs --convert-subs srt"
    if (!noProxy) then args += "--proxy http://127.0.0.1:1087"
    if (cookie) then args += "--cookies-from-browser firefox"
    args.toList

// yt-dlp --write-auto-subs  --remux-video mp4   -f bestvideo+bestaudio https://www.youtube.com/playlist?list=PLYp4IGUhNFmw8USiYMJvCUjZe79fvyYge -o "%(playlist_index)s-%(title)s.%(ext)s"
object YtbDown extends CaseApp[AppOptions]:
  def run(command: AppOptions, remain: RemainingArgs): Unit =
    val args =
      (command.toArgs() ++ remain.unparsed ++ remain.remaining).mkString(" ")
    val cmd = s"yt-dlp ${args}"
    println(cmd)
    System.exit(cmd.!)
