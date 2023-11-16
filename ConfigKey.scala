import scala.quoted.*

sealed trait ConfigKey
trait Proxy extends ConfigKey
trait AutoSub extends ConfigKey
trait Prefix extends ConfigKey
trait Connection extends ConfigKey
trait Cookie extends ConfigKey
trait Retry extends ConfigKey
trait RemuxMp4 extends ConfigKey
trait Format extends ConfigKey
trait RecodeMp4 extends ConfigKey

object ConfigKey:
  inline def getName[T <: ConfigKey]: String = ${ getNameImpl[T] }
  def getNameImpl[T: Type](using Quotes): Expr[String] =
    Expr(Type.show[T])
