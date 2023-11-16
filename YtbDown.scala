import AppOptions.given
import caseapp.CaseApp
import caseapp.core.RemainingArgs

import sys.process.*

object YtbDown extends CaseApp[AppOptions]:
  def run(opt: AppOptions, remain: RemainingArgs): Unit =
    val args = opt.toYtArgs()
    val cmd = s"yt-dlp ${args} '${opt.url}'"
    println(cmd)
    System.exit(cmd.!)
