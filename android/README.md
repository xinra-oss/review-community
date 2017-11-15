Create file `gradle/env-debug.gradle` with the following content:

```
android.buildTypes.debug {
  buildConfigField 'String', 'API_URL', 'http://<server ip>:8080'
}
```

Replace `<server ip>` with the actual IP address the server is running on. Make sure that it is reachable from your Android device/emulator.
