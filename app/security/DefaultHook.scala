package security

import be.objectify.deadbolt.scala.cache.HandlerCache
import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}

class DefaultHook extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    bind[HandlerCache].to[DefaultHandlerCache]
  )
}