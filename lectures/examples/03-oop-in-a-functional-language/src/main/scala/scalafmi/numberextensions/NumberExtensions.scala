package scalafmi.numberextensions

extension (n: Int)
  def squared = n * n
  def **(exp: Double) = math.pow(n, exp)

extension (n: Double)
  def squared = n * n
  def **(exp: Double) = math.pow(n, exp)