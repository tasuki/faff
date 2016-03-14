package modules

import com.google.inject.AbstractModule
import models._
import dao._

class ApplicationModule extends AbstractModule {
  def configure() = {
    bind(classOf[LanguageRepository]).to(classOf[LanguageDAO])
  }
}
