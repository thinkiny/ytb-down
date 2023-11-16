import scala.collection.mutable.HashMap

type ConfigMap = HashMap[String, ConfigEntry[_, _]]

class ConfigSet(entries: ConfigEntry[_, _]*):
  val configMap = entries.map(entry => entry.keyName -> entry).to(HashMap)
  def ++(entries: List[ConfigEntry[_, _]]): ConfigMap = ++(entries: _*)
  def ++(entries: ConfigEntry[_, _]*): ConfigMap =
    entries.foreach(entry => configMap.update(entry.keyName, entry))
    configMap

class DefaultConfig
    extends ConfigSet(
      ConfigEntry[Connection](5),
      ConfigEntry[Retry](true),
      ConfigEntry[RemuxMp4](true),
      ConfigEntry[Format]("best"),
      ConfigEntry[Prefix]("")
    )

object ProxyConfig extends DefaultConfig:
  ++(ConfigEntry[Proxy](true), ConfigEntry[AutoSub](true))

object DemosticConfig extends DefaultConfig:
  ++(ConfigEntry[Cookie](true))
