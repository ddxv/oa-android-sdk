# OpenAttribution Android SDK

🏗️Under construction.🏗️ If you'd like to help feel free to send a PR or join the Dicsord. More info on [OpenAttribution.dev](https://openattribution.dev).


## MVP Goal

To have a fully functional SDK which can be used to track and attribute installs, events and revenue for iOS back to an OpenAttribution server.

## MVP feature roadmap

- Library installable via Cocoapods (? is this the right approach?) or other dependency manager for mac/ios
- user input server endpoint ie `https://demo.openattribution.dev`
- event tracking with params
- documentation for how to use and next steps

## Usage
`build.gradle.kts`
```kotlin 
dependencies {
    implementation("dev.openattribution:open-attribution-sdk:0.0.7")
    // rest of your dependencies
}
```

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


### Event tracking and params details
Events:
- Basic app_open tracking and attributing
- Basic event tracking
- Basic revenue tracking

### ExistingOpenAttribution Params:

These are very loosely defined in:
https://github.com/OpenAttribution/Open-Attribution/blob/main/apps/postback-api/config/dimensions.py

```python
# In App key values
APP_EVENT_UID = "event_uid" # UUID4, unique per tracking call
APP_EVENT_ID = "event_id" # app_open, event_name
APP_EVENT_TIME = "event_time" # epoch timestamp in ms
APP_EVENT_REV = "revenue" # not sure if this is float or string
APP_OA_USER_ID = "oa_uid" # UUID4, unique per user
```

#### Sample postback payload
```
https://demo.openattribution.dev/collect/events/com.example.app?ifa=00000000-0000-0000-0000-000000000000&event_time=1732003510046&event_uid=5730a99e-b009-41da-9d52-1315e26941c1&event_id=app_open&oa_uid=3bd9e091-fa6e-4b91-8dd1-503f8d4fe8f2
```

