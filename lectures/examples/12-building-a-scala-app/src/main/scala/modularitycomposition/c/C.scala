package modularitycomposition.c

import modularitycomposition.a.A3
import modularitycomposition.b.B2

class C(a3: A3, b2: B2):
  def doSomething() = println("Hello from C" + a3)
