package com.da.experiment

import sttp.tapir._
import Library._
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object Endpoints {
  implicit val ec  = ExecutionContext.global
  case class User(name: String) extends AnyVal
  val helloEndpoint: PublicEndpoint[User, Unit, String, Any] = endpoint.get
    .in("hello")
    .in(query[User]("name"))
    .out(stringBody)
  val helloServerEndpoint: ServerEndpoint[Any, Future] = helloEndpoint.serverLogicSuccess(user => Future.successful(s"Hello ${user.name}"))

  val booksListing: PublicEndpoint[Unit, Unit, List[Book], Any] = endpoint.get
    .in("books" / "list" / "all")
    .out(jsonBody[List[Book]])

  val bookAdd = endpoint.post.in("books").in(jsonBody[Book])

  val booksListingServerEndpoint: ServerEndpoint[Any, Future] = booksListing.serverLogicSuccess(_ => Future.successful(Library.books))

  val bookAddServerEndpoint = bookAdd.serverLogicSuccess{ book => Future {
    Library.books = Library.books :+ book
  }}


  val apiEndpoints = List(helloServerEndpoint, booksListingServerEndpoint, bookAddServerEndpoint)

  val docEndpoints: List[ServerEndpoint[Any, Future]] = SwaggerInterpreter()
    .fromServerEndpoints[Future](apiEndpoints, "tapir-test", "1.0.0")

  val all: List[ServerEndpoint[Any, Future]] = apiEndpoints ++ docEndpoints
}

object Library {
  case class Author(author: AuthorType)

  sealed trait AuthorType
  case class Human(name: String) extends AuthorType
  case class AI(name: String) extends AuthorType

  case class Book(title: String, year: Int, author: Author)

  var books = List(
    Book("The Sorrows of Young Werther", 1774, Author(Human("Johann Wolfgang von Goethe"))),
    Book("On the Niemen", 1888, Author(AI("Eliza Orzeszkowa"))),
    Book("The Art of Computer Programming", 1968, Author(Human("Donald Knuth"))),
    Book("Pharaoh", 1897, Author(Human("Boleslaw Prus")))
  )
}
