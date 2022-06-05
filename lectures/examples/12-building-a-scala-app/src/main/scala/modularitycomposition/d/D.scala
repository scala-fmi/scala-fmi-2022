package modularitycomposition.d

import com.typesafe.config.Config

trait D
class D1 extends D
class D2 extends D

class DModule(config: Config):
  val d: D =
    if config.getString("myApplication.dVersion") == "d1" then new D1
    else new D2
