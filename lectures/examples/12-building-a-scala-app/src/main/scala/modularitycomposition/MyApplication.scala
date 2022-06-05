package modularitycomposition

import com.typesafe.config.{Config, ConfigFactory}
import modularitycomposition.a.{A1, A2, A3, AModule}
import modularitycomposition.b.{B1, B2, BModule}
import modularitycomposition.c.{C, CModule}
import modularitycomposition.d.DModule

object MyApplication:
  val config: Config = ConfigFactory.load()

  val aModule = new AModule
  val bModule = new BModule(aModule.a1)
  val cModule = new CModule(aModule.a3, bModule.b2)
  val dModule = new DModule(config)

  def main(args: Array[String]): Unit =
    cModule.c.doSomething()
    println(dModule.d)
