# BB10 minimalistic browser (BEOL)

## Prerequisites

- Android Studio
- Environment variables set (read an article about setting up cordova)

## Project setup

```
$ git clone https://github.com/jonathan-reisdorf/bb10-minimalistic-browser.git
$ cd bb10-minimalistic-browser
$ npm i -g cordova
$ npm i
$ cordova prepare
$ cp patches/* platforms/ -R
$ cordova build
```

If you run into trouble because cordova prepare did not do everything and e.g. cordova_plugins.js is missing, you can do this:
```
$ cordova platform remove android
$ cordova platform add android
$ cp patches/* platforms/ -R
```

## Emulation

Create an Android 4.3 avd and rum `cordova emulate android`.
