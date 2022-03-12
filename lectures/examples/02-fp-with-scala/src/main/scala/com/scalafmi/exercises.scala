package com.scalafmi

def toInteger(value: Any): Int =
  value match
    case n: Int => n
    case s: String => s.toInt
    case d: Double => d.toInt


@main def run =
  toInteger(42) // 42
  toInteger("42") // 42
  toInteger(42.0) // 42
  toInteger(List.empty)

val parsedResult =
  try
    val string = "42L"
    string.toInt
  catch
    case e: NumberFormatException => 0

def sum(xs: Seq[Int]): Int =
  if xs.isEmpty then 0
  else xs.head + sum(xs.tail)

def balanced(e: List[Char]): Boolean =
  def weight(char: Char): Int = char match
    case '(' => 1
    case ')' => -1
    case _ => 0

  def calculate(e: List[Char], balance: Int): Int =
    if e.isEmpty then balance
    else if balance < 0 then balance
    else calculate(e.tail, balance + weight(e.head))

  calculate(e, 0) == 0


@main def runBalance = println {
  balanced("((1 + 2) * 3".toList)
}

def example =
  lazy val a = 10

  lazy val b = 40
  lazy val c = 50

  lazy val y = b * 40

  lazy val x = a + c
  lazy val z = y * x * x

def substrings(str: String): Seq[String] =
  for
    beginningIndex <- 0 until str.size
    length <- 1 to str.size - beginningIndex
  yield str.drop(beginningIndex).take(length)

@main def runSubstrings = println {
  substrings("abcdef")
}










