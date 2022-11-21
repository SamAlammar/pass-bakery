package services

import cats.effect.IO
import doobie.Read
import doobie.util.transactor.Transactor
import doobie.implicits._
import cats.effect.unsafe.implicits.global
import doobie.util.transactor.Transactor.Aux
import models.Products

trait ProductDAO {
  val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", // driver classname
    "jdbc:postgresql:bakery_db", // connect URL (driver-specific)
    "postgres", // user
    "" // password
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

  /** Get a specific product from the inventory
   *
   * @param id
   * @tparam T
   * @return
   */
  def getResultById[T: Read](id: String) : List[T] = {

    sql"select * from Product where id=${id}::uuid "
      .query[T]
      .to[List]
      .transact(xa)
      .unsafeRunSync()
  }

  /** Insert statements into the database to save the product given
   *
   * @param product
   * @return
   */
  def save(product: Products): Int = {
    sql"insert into Product (name, quantity, price) values (${product.name}, ${product.quantity}, ${product.price})"
      .update
      .run
      .transact(xa)
      .unsafeRunSync()
  }

  /** Update statement into the database to update a product given in the product instance using its UUID
   *
   * @param id
   * @param product
   * @tparam T
   * @return
   */
  def updateResultById[T](id: String, product: Products): Int = {
    sql"update Product set name = ${product.name}, quantity = ${product.quantity}, price = ${product.price} where id=${id}::uuid "
      .update
      .run
      .transact(xa)
      .unsafeRunSync()
  }

  /** Delete statement into the database to delete a product given its UUID
   *
   * @param id
   * @tparam T
   * @return
   */
  def deleteResultById[T](id: String): Int = {
    sql"delete from Product where id=${id}::uuid "
      .update
      .run
      .transact(xa)
      .unsafeRunSync()
  }
}
