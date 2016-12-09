/**
  * This script parses `2996` unique class names from Oracle web site and generate the same count of Longs
  *
  * You can pass integer as argument to multiply count of class names
  * Examples:
  *
  * scala generator.scala // will generate 2996 test entries
  * scala generator.scala 10 // will generate 2996 * 10  test entries
  *
  */

import scala.io.Source
import java.io.{PrintWriter, File}

val times = if (args.nonEmpty) Integer.parseUnsignedInt(args(0)) else 1
val url = "https://docs.oracle.com/javase/7/docs/api/allclasses-noframe.html"
val page = Source.fromURL(url).mkString
val regex = """<a.*>(.*)</a>""".r
val pw = new PrintWriter(new File("../classNames.txt"))
val pw1 = new PrintWriter(new File("../modDates.txt"))

for (m <- regex.findAllIn(page).matchData; e <- m.subgroups) {
  if (e.trim.nonEmpty) {
    Range(0, times).foreach(x => {
      println(e)
      pw.println(e + x)
      pw1.println(java.util.concurrent.ThreadLocalRandom.current().nextLong(111111111111111L))
    })
  }
}

pw.close()
pw1.close()
