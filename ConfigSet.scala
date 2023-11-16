import scala.collection.mutable.HashMap

case class ConfigEntry[K <: ConfigKey, V](value: V)(using
    ta: ToArg[ConfigEntry[K, V]]
):
  val keyName = ConfigKey.getName[K]
  val arg = ta.toArg(this)

type ConfigMap = HashMap[String, ConfigEntry[_, _]]

class ConfigSet(entries: ConfigEntry[_, _]*):
  val configMap = entries.map(entry => entry.keyName -> entry).to(HashMap)
  def ++(entries: List[ConfigEntry[_, _]]): ConfigMap = ++(entries: _*)
  def ++(entries: ConfigEntry[_, _]*): ConfigMap =
    entries.foreach(entry => configMap.update(entry.keyName, entry))
    configMap

class DefaultConfig
    extends ConfigSet(
      ConfigEntry[Connection, Int](5),
      ConfigEntry[Retry, Boolean](true),
      ConfigEntry[RemuxMp4, Boolean](true),
      ConfigEntry[Format, String]("best"),
      ConfigEntry[Prefix, String]("")
    )

object ProxyConfig extends DefaultConfig:
  ++(ConfigEntry[Proxy, Boolean](true), ConfigEntry[AutoSub, Boolean](true))

object DemosticConfig extends DefaultConfig:
  ++(ConfigEntry[Cookie, Boolean](true))
