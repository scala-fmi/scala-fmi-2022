package modularitycomposition.c

import modularitycomposition.a.A3
import modularitycomposition.b.{B2, BModule}

class CModule(a3: A3, b2: B2):
  val c = new C(a3, b2)
