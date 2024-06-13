package app.cashadvisor

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import app.cashadvisor.analytics.data.db.DbRepository
import app.cashadvisor.analytics.data.db.MainDb
import app.cashadvisor.analytics.data.db.asCategoryEntity
import app.cashadvisor.analytics.data.db.asUserAnalyticsEntity
import app.cashadvisor.analytics.data.db.test.TestCategoryResponse
import app.cashadvisor.analytics.data.db.test.TestUserAnalyticsResponse
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setRussianLocale()
        configureTimber()

        ///////////   DB Test    //////////////////
        val testCategoryResponse = TestCategoryResponse().categorySettingsDto
        val testUserAnalyticsResponse = TestUserAnalyticsResponse().userAnalyticsDto
        val categoryEntityList = testCategoryResponse.asCategoryEntity()
        val userAnalyticsEntityList = testUserAnalyticsResponse.asUserAnalyticsEntity()

        val db = MainDb.getDb(this)
        val dao = db.getDao()

        val dbRepository = DbRepository(dao)

        runBlocking {
            launch(Dispatchers.IO){
                dbRepository.upsertCategory(categoryEntityList)
                dbRepository.upsertUserAnalytics(userAnalyticsEntityList)
            }
        }
        /////////////////////////////////
    }

    private fun configureTimber() = when (BuildConfig.LOGGING_LEVEL) {
        DEBUG -> {
            Timber.plant(Timber.DebugTree())
        }

        RELEASE -> {
            plantReleaseTree()
        }

        QA -> {
            Timber.plant(Timber.DebugTree())
            plantReleaseTree()
        }

        else -> {}
    }

    private fun plantReleaseTree() {
        Timber.plant(object : Timber.Tree() {
            override fun isLoggable(tag: String?, priority: Int): Boolean {
                return priority == Log.WARN || priority == Log.ERROR
            }

            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                if (priority == Log.DEBUG || priority == Log.INFO) {
                    return
                }
                when (priority) {
                    Log.WARN -> {
                        FirebaseCrashlytics.getInstance().log("$priority $tag $message")
                        FirebaseCrashlytics.getInstance()
                            .recordException(t ?: RuntimeException(message))
                    }

                    Log.ERROR -> FirebaseCrashlytics.getInstance()
                        .recordException(t ?: RuntimeException(message))
                }
            }
        })
    }

    private fun setRussianLocale() {
        val appLocale: LocaleListCompat =
            LocaleListCompat.forLanguageTags("ru")
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    companion object {
        private const val DEBUG = "DEBUG"
        private const val RELEASE = "RELEASE"
        private const val QA = "QA"

    }
}