trait ConfigEntry[V]:
  val key: String
  val value: V
  def toYtArg(): Option[String]

object ConfigEntry:
  class Builder[K <: ConfigKey]:
    inline def apply[V](v: V)(using f: ToYtArg[K, V]): ConfigEntry[V] =
      new ConfigEntry[V] {
        val key = ConfigKey.getName[K]
        val value = v
        def toYtArg() = f.toArg(v)
      }

  inline def apply[K <: ConfigKey] = new Builder[K]
