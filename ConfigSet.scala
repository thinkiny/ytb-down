import scala.collection.mutable.HashMap

type ConfigMap = HashMap[String, ConfigEntry[_]]

class ConfigSet(entries: ConfigEntry[_]*):
  val dict = entries.map(entry => entry.key -> entry).to(HashMap)
  def ++(entries: List[ConfigEntry[_]]): ConfigMap = ++(entries: _*)
  def +=(entry: ConfigEntry[_]): this.type =
    dict.put(entry.key, entry)
    this
  def ++(entries: ConfigEntry[_]*): ConfigMap =
    entries.foreach(entry => dict.update(entry.key, entry))
    dict

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
