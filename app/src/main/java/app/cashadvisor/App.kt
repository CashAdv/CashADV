package app.cashadvisor

import android.app.Application
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        configureTimber()
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

    companion object {
        private const val DEBUG = "DEBUG"
        private const val RELEASE = "RELEASE"
        private const val QA = "QA"

    }
}