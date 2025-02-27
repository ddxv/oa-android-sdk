package dev.openattribution.sdk

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import java.util.UUID
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
        private var instance: OpenAttribution? = null

        // Method to initialize the SDK
        fun initialize(context: Context, baseUrl: String): OpenAttribution {
            Log.d("MyOA", "InitStart")
            myBaseUrl = baseUrl
            instance = OpenAttribution(context.applicationContext)
            instance?.scheduleTrackAppOpen()
            return instance!!
        }


        fun trackEvent(context: Context, eventName: String, value:Number) {
            if (instance == null) {
                throw IllegalStateException("OpenAttribution not initialized. Call OpenAttribution.initialize() first.")
            }
            val workRequest = TrackEventWorker.createWorkRequest(eventName)
            WorkManager.getInstance(context).enqueue(workRequest)
        }


        fun trackPurchase(context: Context, revenueAmount: Double, currency: String, eventName: String="iap_purchase") {
            if (instance == null) {
                Log.w("OpenAttribution", "SDK not initialized. Auto-initializing with default settings.")
                initialize(context, "https://default-endpoint.openattribution.dev")
            }
            val workRequest = TrackEventWorker.createRevenueWorkRequest(eventName, revenueAmount, currency)
            WorkManager.getInstance(context).enqueue(workRequest)
        }


        fun getBaseUrl(): String {
            return myBaseUrl ?: throw IllegalStateException("Base URL is not yet initialized. Call OpenAttribution.initialize() first.")
        }
    }

    private fun scheduleTrackAppOpen() {
        val workRequest = TrackEventWorker.createWorkRequest("app_open")
        WorkManager.getInstance(context).enqueue(workRequest)
    }

}

