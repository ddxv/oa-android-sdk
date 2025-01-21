package dev.openattribution.sdk

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.UUID
import java.util.concurrent.TimeUnit
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager






class UserIdManager(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    fun getUserId(): String {
        // Try to get existing ID
        var userId = preferences.getString(KEY_USER_ID, null)

        // If no ID exists, create one and save it
        if (userId == null) {
            userId = UUID.randomUUID().toString()
            preferences.edit()
                .putString(KEY_USER_ID, userId)
                .apply()
        }

        return userId
    }

    companion object {
        private const val PREF_NAME = "user_prefs"
        private const val KEY_USER_ID = "user_id"

        @Volatile
        private var instance: UserIdManager? = null

        fun getInstance(context: Context): UserIdManager {
            return instance ?: synchronized(this) {
                instance ?: UserIdManager(context.applicationContext).also { instance = it }
            }
        }
    }
}



object EmulatorDetector {
    fun isEmulator(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_gphone")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("vbox86p")
                || Build.HOST.startsWith("Build") // Android Studio
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
    }
}


class OpenAttribution private constructor(private val context: Context) {

        companion object {
            private var myBaseUrl: String? = null

            // Method to initialize the SDK
            fun initialize(context: Context, baseUrl: String): OpenAttribution {

                Log.d("MyOA", "InitStart")


                val instance = OpenAttribution(context)
                instance.scheduleTrackAppOpen()

                myBaseUrl = baseUrl
//                return OpenAttribution(context)
                return instance
            }

            // Getter for the base URL to ensure it's set
            fun getBaseUrl(): String {
                return myBaseUrl ?: throw IllegalStateException("Base URL is not yet initialized. Call OpenAttribution.initialize() first.")
            }
        }

    private fun scheduleTrackAppOpen() {
        val workRequest = OneTimeWorkRequestBuilder<TrackAppOpenWorker>()
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }


}