package exercises

import scala.annotation.tailrec

object Recursion extends App:
  def fact(n: Int): Int =
    if n <= 1 then 1
    else n * fact(n - 1)

  def size[A](l: List[A]): Int =
    if l.isEmpty then 0
    else 1 + size(l.tail)

  def sum(l: List[Int]): Int =
    if l.isEmpty then 0
    else l.head + sum(l.tail)

  //f(5) = f(4) + f(3)
  def fibonacci(i: Int): Int = ???

object TailRecursion extends App:
  // We could introduce inner functions if we don't want to pollute the interface
  // But let's use default parameters

  @tailrec
  def fact(n: Int, acc: Int = 1): Int =
    if n <= 1 then acc
    else fact(n - 1, acc * n)

  @tailrec
  def size[A](l: List[A], acc: Int = 0): Int =
    if l.isEmpty then acc
    else size(l.tail, acc + 1)

  @tailrec
  def sum(l: List[Int], acc: Int = 0): Int =
    if l.isEmpty then acc
    else sum(l.tail, l.head + acc)

  // @tailrec
  def fibonacci(i: Int): Int = ???

object MoreListFunctions extends App:
  @tailrec
  def drop[A](la: List[A], n: Int): List[A] =
    if n <= 0 || la.isEmpty then la
    else drop(la.tail, n - 1)

  @tailrec
  def reverse[A](l: List[A], acc: List[A] = Nil): List[A] =
    if l.isEmpty then acc
    else reverse(l.tail, l.head :: acc)

  def take[A](la: List[A], n: Int): List[A] = ???

  def nthElement[A](la: List[A], n: Int): A = ???

  def concat(l1: List[Int], l2: List[Int]): List[Int] = ???
