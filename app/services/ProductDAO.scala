package services

import cats.effect.IO
import doobie.Read
import doobie.util.transactor.Transactor
import doobie.implicits._
import cats.effect.unsafe.implicits.global
import doobie.util.transactor.Transactor.Aux
import models.UserProducts

import scala.util.{Failure, Success, Try}

trait ProductDAO {
  val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql:bakery_db",
    "postgres",
    ""
  )

  /** Select * statement with rows returned as a list so I can put it in JSON format
   *
   * @tparam T: What the query will put its data in the format of.
   * @return A list of all products in the database.
   */
  def getResults[T: Read]: List[T] = {

    sql"select * from Product".query[T]
      .to[List]
      .transact(xa)
      .unsafeRunSync()
  }

  /** Get a specific product from the inventory, if an error is caught (meaning the uuid string is incorrect)
   * then return null
   *
   * @param id: String of the UUID given by the user
   * @tparam T: What the query will put its data in the format of.
   * @return Either a list of the row being selected or an empty list if it doesn't exist from the ID given
   */
  def getResultById[T: Read](id: String) : List[T] = {
    val status = Try(
      sql"select * from Product where id=$id::uuid "
        .query[T]
        .to[List]
        .transact(xa)
        .unsafeRunSync()
    )
    status match {
      case Success(list) => list
      case Failure(_) => List.empty
    }
  }

  /** Insert statements into the database to save the product given
   *
   * @param product: Given product instance to use in query.
   * @return
   */
  def save(product: UserProducts): Int = {
    sql"insert into Product (name, quantity, price) values (${product.name}, ${product.quantity}, ${product.price})"
      .update
      .run
      .transact(xa)
      .unsafeRunSync()
  }

  /** Update statement into the database to update a product given in the product instance using its UUID
   *
   * @param id: String of the UUID given by the user.
   * @param product: Given product instance to use in query.
   * @tparam T: What the query will put its data in the format of.
   * @return A number signifying either a row updated (1) or -1 if the ID doesn't exist
   */
  def updateResultById[T](id: String, product: UserProducts): Int = {
    val status = Try(
      sql"update Product set name = ${product.name}, quantity = ${product.quantity}, price = ${product.price} where id=$id::uuid "
      .update
      .run
      .transact(xa)
      .unsafeRunSync()
    )

    status match {
      case Success(num) => num
      case Failure(_) => -1
    }
  }

  /** Delete statement into the database to delete a product given its UUID
   *
   * @param id: String of the UUID given by the user.
   * @tparam T: What the query will put its data in the format of.
   * @return A number signifying either a row deleted (1) or -1 if the ID doesn't exist
   */
  def deleteResultById[T](id: String): Int = {
    val status = Try(
      sql"delete from Product where id=$id::uuid "
        .update
        .run
        .transact(xa)
        .unsafeRunSync()
    )

    status match {
      case Success(num) => num
      case Failure(_) => -1
    }
  }
}
