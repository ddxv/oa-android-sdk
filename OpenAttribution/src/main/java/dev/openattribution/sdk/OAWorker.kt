package dev.openattribution.sdk

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit



class TrackEventWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("MyOA", "doWork start")

        val eventId = inputData.getString(EVENT_ID_KEY) ?: return Result.failure()
        return trackEvent(appContext, eventId)
    }

    companion object {
        private const val EVENT_ID_KEY = "event_id"

        fun createWorkRequest(eventId: String): androidx.work.OneTimeWorkRequest {
            val inputData = Data.Builder()
                .putString(EVENT_ID_KEY, eventId)
                .build()

            return androidx.work.OneTimeWorkRequestBuilder<TrackEventWorker>()
                .setInputData(inputData)
                .build()
        }

        suspend fun trackEvent(context: Context, eventId: String): Result {
            Log.d("TrackEventWorker", "Tracking event: $eventId")
            val userIdManager = UserIdManager.getInstance(context)
            val baseUrl = OpenAttribution.getBaseUrl()

            try {
                val androidId =
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                val myTimestamp = System.currentTimeMillis()
                val myUid = UUID.randomUUID().toString()
                val myOaUid = userIdManager.getUserId()
                val basePackageName = context.packageName
                val appendedPackageName = if (EmulatorDetector.isEmulator()) {
                    "${basePackageName}_test"
                } else {
                    basePackageName
                }

                val gaid = withContext(Dispatchers.IO) {
                    try {
                        val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
                        adInfo.id
                    } catch (e: Exception) {
                        Log.e("OpenAttribution", "Error retrieving GAID: ${e.message}")
                        null
                    }
                }

                val (url, trackingEvent) = constructTrackingRequest(
                    baseUrl,
                    appendedPackageName,
                    myOaUid,
                    gaid,
                    androidId ?: "unknown",
                    eventId,
                    myUid,
                    myTimestamp
                )

                val client = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()

                val jsonBody = JSONObject().apply {
                    put("oa_uid", trackingEvent.oaUid)
                    put("ifa", trackingEvent.ifa)
                    put("android_id", trackingEvent.androidId)
                    put("event_id", trackingEvent.eventId)
                    put("event_uid", trackingEvent.eventUid)
                    put("event_time", trackingEvent.eventTime)
                }

                val request = Request.Builder()
                    .url(url)
                    .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                withContext(Dispatchers.IO) {
                    client.newCall(request).execute()
                }.use { response ->
                    return if (response.isSuccessful) {
                        Log.i("OpenAttribution", "Tracking request successful: ${response.code}")
                        Result.success()
                    } else {
                        Log.w("OpenAttribution", "Tracking request failed: ${response.code}")
                        Result.retry()
                    }
                }
            } catch (e: Exception) {
                Log.e("OpenAttribution", "Error in TrackAppOpenWorker: ${e.message}")
                return Result.retry()
            }
        }

        private fun constructTrackingRequest(
            baseUrl: String,
            packageName: String,
            oauid: String,
            gaid: String?,
            androidId: String,
            eventId: String,
            eventUid: String,
            eventTime: Long
        ): Pair<String, TrackingEvent> {
            val url = "$baseUrl/collect/events/$packageName"

            val trackingEvent = TrackingEvent(
                oaUid = oauid,
                ifa = gaid,
                androidId = androidId,
                eventId = eventId,
                eventUid = eventUid,
                eventTime = eventTime
            )

            Log.i("OpenAttribution", "Constructing tracking URL $url")
            return Pair(url, trackingEvent)
        }
    }
}
