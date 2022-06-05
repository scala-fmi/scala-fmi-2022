package modularitythincake

import com.typesafe.config.{Config, ConfigFactory}
import modularitythincake.a.{A1, A2, A3, AModule}
import modularitythincake.b.{B1, B2, BModule}
import modularitythincake.c.{C, CModule}
import modularitythincake.d.DModule

object MyApplication extends CModule with BModule with AModule with DModule:
  lazy val config: Config = ConfigFactory.load()

  def main(args: Array[String]): Unit =
    c.doSomething()
    println(d)
