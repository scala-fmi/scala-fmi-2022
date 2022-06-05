package modularitythincake.c

import modularitythincake.a.A3
import modularitythincake.b.B2

class C(a3: A3, b2: B2):
  def doSomething() = println("Hello from C" + a3)
