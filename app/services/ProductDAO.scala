package services

import cats.effect.IO
import doobie.Read
import doobie.util.transactor.Transactor
import doobie.implicits._
import cats.effect.unsafe.implicits.global

trait ProductDAO {
  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", // driver classname
    "jdbc:postgresql:bakery_db", // connect URL (driver-specific)
    "postgres", // user
    "" // password
  )

  // Select * statement with rows returned as a list so I can put it in JSON format
  def getResults[T: Read]: List[T] = {
    sql"select * from Bakery".query[T] // Query0[*]
      .to[List] // ConnectionIO[List[Product]]
      .transact(xa) // IO[List[Product]]
      .unsafeRunSync() // List[Product]
      .take(5) // List[Product]
  }
}
