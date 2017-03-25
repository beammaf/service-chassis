package allawala.modules

import com.google.inject.{AbstractModule, Provides, Singleton}
import com.typesafe.config.{Config, ConfigFactory}
import net.codingwell.scalaguice.ScalaModule
import net.ceedubs.ficus.Ficus
import net.ceedubs.ficus.readers.ArbitraryTypeReader

case class HttpConfig(host: String, port: Int)
case class Configuration(name: String, httpConfig: HttpConfig)

class ConfigModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {}
}

object ConfigModule {
  import ArbitraryTypeReader._
  import Ficus._

  @Provides
  @Singleton
  def getConfig(): Configuration = {
    val config: Config = ConfigFactory.load()
    config.as[Configuration]("service")
  }
}
