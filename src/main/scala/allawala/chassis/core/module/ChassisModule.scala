  package allawala.chassis.core.module

import javax.inject.Named

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorMaterializer
import allawala.chassis.config.model.BaseConfig
import allawala.chassis.config.module.ConfigModule
import allawala.chassis.health.module.HealthModule
import allawala.chassis.http.module.HttpModule
import allawala.chassis.http.service.AkkaHttpService
import allawala.chassis.util.module.UtilModule
import com.google.inject.{AbstractModule, Module, Provides, Singleton}
import net.codingwell.scalaguice.ScalaModule

import scala.concurrent.ExecutionContext


class ChassisModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    install(new ConfigModule)
    install(new HttpModule)
    install(new HealthModule)
    install(new UtilModule)

    bind[AkkaHttpService].asEagerSingleton()
  }
}

object ChassisModule {
  def apply(): Module = new ChassisModule

  @Provides
  @Singleton
  def getAkkaMaterializer(implicit actorSystem: ActorSystem): ActorMaterializer = {
    ActorMaterializer()
  }

  @Provides
  @Singleton
  def getActorSystem(baseConfig: BaseConfig): ActorSystem = {
    ActorSystem(baseConfig.name)
  }

  @Provides
  @Singleton
  @Named("default-dispatcher")
  def getDefaultExecutionContextExecutor(implicit actorSystem: ActorSystem) : ExecutionContext = {
    actorSystem.dispatcher
  }

  @Provides
  @Singleton
  @Named("blocking-fixed-pool-dispatcher")
  def getBlockingExecutionContextExecutor(implicit actorSystem: ActorSystem) : ExecutionContext = {
    actorSystem.dispatchers.lookup("blocking-fixed-pool-dispatcher")
  }

  @Provides
  @Singleton
  def getLoggingAdapter(implicit actorSystem: ActorSystem) : LoggingAdapter = {
    Logging(actorSystem, getClass)
  }
}
