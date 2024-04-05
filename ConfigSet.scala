import scala.collection.mutable.HashMap

type ConfigMap = HashMap[String, ConfigEntry[?]]

class ConfigSet(entries: ConfigEntry[?]*):
  val optionMap = entries.map(entry => entry.key -> entry).to(HashMap)
  def ++(entries: List[ConfigEntry[?]]): ConfigMap = ++(entries*)
  def +=(entry: ConfigEntry[?]): this.type =
    optionMap.put(entry.key, entry)
    this
  def ++(entries: ConfigEntry[?]*): ConfigMap =
    entries.foreach(entry => optionMap.update(entry.key, entry))
    optionMap

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
