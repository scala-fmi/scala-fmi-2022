package modularitycomposition.b

import modularitycomposition.a.A1

class BModule(a1: A1):
  val b1 = new B1
  val b2 = new B2(b1, a1)
