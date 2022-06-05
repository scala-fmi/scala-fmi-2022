package modularitycomposition.a

class AModule:
  val a1 = new A1
  val a2 = new A2
  val a3 = new A3(a1, a2)
