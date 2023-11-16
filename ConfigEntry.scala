case class ConfigEntry[K <: ConfigKey, V](value: V)(using
    ta: ToArg[ConfigEntry[K, V]]
):
  val keyName = ConfigKey.getName[K]
  val arg = ta.toArg(this)

class ConfigEntryBuilder[K <: ConfigKey]:
  inline def apply[V](v: V)(using ToArg[ConfigEntry[K, V]]): ConfigEntry[K, V] =
    ConfigEntry[K, V](v)

object ConfigEntry:
  inline def apply[K <: ConfigKey] = new ConfigEntryBuilder[K]
