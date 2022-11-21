package services

import cats.effect.IO
import doobie.Read
import doobie.util.transactor.Transactor
import doobie.implicits._
import cats.effect.unsafe.implicits.global

trait ProductDAO {
  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql:bakery_db",
    "postgres",
    ""
  )

  /** Select * statement with rows returned as a list so I can put it in JSON format
   *
   * @tparam T
   * @return
   */
  def getResults[T: Read]: List[T] = {
    sql"select * from Product".query[T]
      .to[List]
      .transact(xa)
      .unsafeRunSync()
  }
}
