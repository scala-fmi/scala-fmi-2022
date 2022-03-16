package answers

import scala.annotation.tailrec

def filter[A](la: List[A], p: A => Boolean): List[A] =
  @tailrec
  def loop(rest: List[A], acc: List[A] = Nil): List[A] =
    if rest.isEmpty then acc
    else if p(rest.head) then loop(rest.tail, rest.head :: acc)
    else loop(rest.tail, acc)

  loop(la).reverse

def map[A, B](la: List[A], f: A => B): List[B] =
  @tailrec
  def loop(rest: List[A], acc: List[B] = Nil): List[B] =
    if rest.isEmpty then acc
    else loop(rest.tail, f(rest.head) :: acc)

  loop(la).reverse

def reduce[A](la: List[A], f: (A, A) => A): A =
  @tailrec
  def loop(rest: List[A], acc: A): A =
    if rest.isEmpty then acc
    else loop(rest.tail, f(rest.head, acc))

  loop(la.tail, la.head)
