package modularitythincake.b

import modularitythincake.a.A1

trait BModule:
  def a1: A1

  lazy val b1 =
    new B1
  lazy val b2 = new B2(b1, a1)
