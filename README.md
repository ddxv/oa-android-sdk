# OpenAttribution Android SDK

🏗️Under construction.🏗️ 
If you'd like to help feel free to send a PR or join the [OpenAttribution Discord](https://discord.gg/Z5ueYE3Ct3).


More info on [OpenAttribution.dev](https://openattribution.dev).


## MVP Goal

To have a fully functional SDK which can be used to track and attribute installs, events and revenue for iOS back to an OpenAttribution server.
### Event tracking and details


## MVP feature road map

- [x] Library installable via Maven
- [x] user input server endpoint ie `https://demo.openattribution.dev`
- [ ] Events:
	- [x] app_open tracking and attributing
	- [ ] Basic event tracking
	- [ ] Basic revenue tracking
- [ ] Documentation for how to use and next steps

## Install

You can get the latest version of OpenAttribution's (Android SDK from Maven Central)[https://central.sonatype.com/artifact/dev.openattribution/open-attribution-sdk]

`build.gradle.kts`
```kotlin 
dependencies {
    implementation("dev.openattribution:open-attribution-sdk:{LATEST_VERSION}")
    // rest of your dependencies
}
```

## Tracking App Open

`MyApplication.kts`
```kotlin

import android.app.Application

import dev.openattribution.sdk.OpenAttribution

class MyApplication : Application() {

    private lateinit var openAttribution: OpenAttribution

    override fun onCreate() {
        super.onCreate()
        // Initialize the OpenAttribution SDK, replace with your domain
        openAttribution = OpenAttribution.initialize(this, "https://demo.openattribution.dev")

    }

}

```




## Local Development:

If you're interested in contributing to OpenAttribution Android SDK please don't hesitate to reach out [on Discord](https://discord.gg/Z5ueYE3Ct3)  or with a PR. 

The OpenAttribution postback API will expect or allow the values as defined here:
https://github.com/OpenAttribution/Open-Attribution/blob/main/apps/postback-api/config/dimensions.py

#### Sample post back payload
This is a sample of the minimum required postback by the OpenAttribution server. The empty IFA.

```http
POST https://demo.openattribution.dev/collect/events/com.example.app HTTP/1.1
Content-Type: application/json

{
  "ifa": "00000000-0000-0000-0000-000000000000",
  "event_time": 1732003510046,
  "event_uid": "5730a99e-b009-41da-9d52-1315e26941c1",
  "event_id": "app_open",
  "oa_uid": "3bd9e091-fa6e-4b91-8dd1-503f8d4fe8f2"
}

