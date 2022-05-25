package sql

import doobie.*
import doobie.implicits.*

case class Country(code: String, name: String, pop: Int, gnp: Option[Double])

object Doobie02Querying:
  val ex =
    sql"""
         SELECT code, name, population, gnp
         FROM country
         LIMIT 100"""
      .query[(String, String, Int, Option[Int])]
      .to[List]

  // def biggerThan(minPop: Int) = sql"""
  //   select code, name, population, gnp
  //   from country
  //   where population > $minPop
  // """.query[Country]

  // val ex2 = biggerThan(8000000).to[List]

  // case class Email(name: String, domain: String)

  // given Meta[Email] = Meta[String].imap(_.split("@") match
  //   case Array(name, domain) => Email(name, domain)
  // )(e => s"${e.name}@${e.domain}")

  // val ex3 = sql"SELECT 'viktor@gmail.com'".query[Email].unique
