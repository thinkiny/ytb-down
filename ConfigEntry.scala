enum ConfigKey[V]:
  case Proxy extends ConfigKey[Boolean]
  case AutoSub extends ConfigKey[Boolean]
  case Prefix extends ConfigKey[String]
  case Connection extends ConfigKey[Int]
  case Cookie extends ConfigKey[Boolean]
  case Retry extends ConfigKey[Boolean]
  case RemuxMp4 extends ConfigKey[Boolean]
  case Format extends ConfigKey[String]
  case RecodeMp4 extends ConfigKey[Boolean]

case class ConfigEntry[V](key: ConfigKey[V], value: V):
  lazy val toArg = ToArgFunc(key)(value)
