package io.sentry.sentrykotlinsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.sentry.core.Sentry
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(DebugTree())

        Timber.i("Sentry.isEnabled() = ${Sentry.isEnabled()}")

        crash_from_kotlin.setOnClickListener {
            throw RuntimeException("test from kotlin")
        }

        capture_exception.setOnClickListener {
            Timber.i("main thread: ${Thread.currentThread().name}")
            GlobalScope.async {
                Timber.i("GlobalScope.async: ${Thread.currentThread().name}")
                try {
                    throw RuntimeException("test from GlobalScope.async")
                } catch (e: RuntimeException) {
                    Sentry.captureException(e)
                }
            }
        }
    }
}
