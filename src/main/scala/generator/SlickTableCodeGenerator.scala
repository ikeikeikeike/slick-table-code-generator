package generator

import slick.driver.MySQLDriver.api._
import slick.jdbc.MySQLProfile
import slick.model.Model

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object SlickTableCodeGenerator extends App {

  val config = com.typesafe.config.ConfigFactory.load

  val db = Database.forURL(
    config.getString("db.url"),
    driver = config.getString("db.jdbc.driver"),
    user = config.getString("db.user"),
    password = config.getString("db.password")
  )

  val model: Model = Await.result(db.run(MySQLProfile.createModel(None, ignoreInvalidDefaults = false)), Duration.Inf)
  new CustomSourceCodeGenerator(model).writeToFile(
    config.getString("db.slick.driver"),
    config.getString("output.dir"),
    config.getString("package")
  )
}