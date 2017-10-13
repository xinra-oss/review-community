The client is based on [angular-seed-advanced](https://github.com/NathanWalker/angular-seed-advanced). Go there for a full documentation on build scripts.

## Prerequisites

1. On Windows this requires git >= 2.11.1 with [enabled symlinks](https://stackoverflow.com/a/42137273)
2. Install [NodeJS](https://nodejs.org/en/)
3. Install [Android Studio](https://developer.android.com/studio/index.html)
4. If you want to run the Android app on your device you *may* need to install [additional drivers](http://adbdriver.com/downloads/). You also have to [enable USB Debugging](https://www.kingoapp.com/root-tutorials/how-to-enable-usb-debugging-mode-on-android.htm)
5. If this is not done automatically, you have so set the environment variable `ANDROID_HOME`. On Windows 7+ this can be done with `setx ANDROID_HOME "C:\Users\<username>\AppData\Local\Android\sdk"`. The `ANDROID_HOME` needs to contain the directory `platform-tools`
6. Install Nativescipt: `$ npm install -g nativescript`
7. In the client directory, run `$ npm install`

## Generate Sources

Some sources are shared between the client and the server. For the client to work, you have to run the following command from the root directory of the repository (on Mac/Linux use `./gradlew` instead of `gradlew`):
```
$ gradlew :client:generate
```

## Configuration

TODO

## Running the client

Run Android app on emulator or device:
```
$ npm run start.android
$ npm run start.android.device
```

Run tests (includes linting)
```
$ npm test
```