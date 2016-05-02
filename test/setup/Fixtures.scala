package test.setup

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.util._

import models._

trait Fixtures extends Inject {
  val languageRepository = inject[LanguageRepository]
  val languagePairRepository = inject[LanguagePairRepository]

  val languages = List(
    Language(code="cs-cz", name="Czech"),
    Language(code="pl-pl", name="Polish")
  )

  def before() = {
    val deleteLanguagePairs: Future[Int] = languagePairRepository.delete
    val deleteLanguages: Future[Int] = deleteLanguagePairs.flatMap {
      case _ => languageRepository.delete
    }

    val insertedLanguages: Future[List[Language]] = deleteLanguages.flatMap {
      case _ => Future.sequence(languages.map(languageRepository.insert(_)))
    }

    val insertedLanguagePairs: Future[Int] = insertedLanguages.flatMap {
      case languages => {
        val languagePair = LanguagePair(
          fromLanguage = languages(0),
          toLanguage = languages(1))

        languagePairRepository.insert(languagePair)
      }
    }

    Await.result(insertedLanguagePairs, 1 seconds);
  }
}
