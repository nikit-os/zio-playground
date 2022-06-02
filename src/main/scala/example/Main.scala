package example

import zio._
import zio.console._

import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main extends App {

  val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

  val program: ZIO[Console, IOException, String] = for {
   _ <- putStrLn("Choose your color (red, yellow, green) or quit")
   _ <- putStr("-> ")
   choice <- getStrLn.map(_.toLowerCase())
    _ <- choice match {
      case "red" | "yellow" | "green" => {
        val currentTime = LocalDateTime.now().format(dateTimeFormatter)
        putStrLn(s"$currentTime [$choice]\n")
      }
      case "quit" => ZIO.unit
      case _ => putStrLn(s"Wrong choice - [$choice]! Pick (red, yellow, green) or quit\n")
    }
  } yield choice

  val schedulePolicy: Schedule[Any, String, String] = Schedule.recurUntil[String](_ == "quit")

  def run(args: List[String]) = program.repeat(schedulePolicy).exitCode
}

