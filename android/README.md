This project requires Android Studio 3.0

Running the Android client:

1) Create file `android/gradle/env-debug.gradle` with the following content:
```
android.buildTypes.debug {
  buildConfigField 'String', 'API_URL', '"http://<server ip>:8080"'
}
```
Replace `<server ip>` with the actual IP address the server is running on. Make sure that it is reachable from your Android device/emulator.

2) Open the project in Android Studio and launch the `android` module.
