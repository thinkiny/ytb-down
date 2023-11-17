import scala.collection.mutable.HashMap

type ConfigMap = HashMap[String, ConfigEntry[_]]

class ConfigSet(entries: ConfigEntry[_]*):
  val configMap = entries.map(entry => entry.key -> entry).to(HashMap)
  def ++(entries: List[ConfigEntry[_]]): ConfigMap = ++(entries: _*)
  def ++(entries: ConfigEntry[_]*): ConfigMap =
    entries.foreach(entry => configMap.update(entry.key, entry))
    configMap

class DefaultConfig
    extends ConfigSet(
      ConfigEntry[Connection](5),
      ConfigEntry[Retry](true),
      ConfigEntry[RemuxMp4](true),
      ConfigEntry[Format]("bestvideo+bestaudio/best"),
      ConfigEntry[Prefix]("")
    )

object ProxyConfig extends DefaultConfig:
  ++(ConfigEntry[Proxy](true), ConfigEntry[AutoSub](true))

object DemosticConfig extends DefaultConfig:
  ++(ConfigEntry[Cookie](true))
