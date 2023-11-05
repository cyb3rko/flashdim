# FlashDim
[![API](https://img.shields.io/badge/API-33%2B-brightgreen.svg?style=flat)](https://apilevels.com)
[![release](https://img.shields.io/github/release/cyb3rko/flashdim.svg)](https://github.com/cyb3rko/flashdim/releases/latest)
[![fdroid](https://img.shields.io/f-droid/v/com.cyb3rko.flashdim.svg)](https://f-droid.org/packages/com.cyb3rko.flashdim)
[![license](https://img.shields.io/github/license/cyb3rko/logviewer-for-openhab-app)](https://www.apache.org/licenses/LICENSE-2.0)
[![last commit](https://img.shields.io/github/last-commit/cyb3rko/flashdim?color=F34C9F)](https://github.com/cyb3rko/flashdim/commits/main)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/4e7fd3ea03da4a32aae48ff39693ee91)](https://www.codacy.com/gh/cyb3rko/flashdim/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cyb3rko/flashdim&amp;utm_campaign=Badge_Grade)

[![FOSSA Status](https://app.fossa.com/api/projects/custom%2B35689%2Fgithub.com%2Fcyb3rko%2Fflashdim.svg?type=small)](https://fossa.com/)

<p align="center">
  <img alt="FlashDim" src="https://playbadges.pavi2410.me/badge/full?id=com.cyb3rko.flashdim" height="120"/>
</p>

---

- [About this project](#about-this-project)   
- [Features](#features)  
- [Download](#download)
  - [Verification](#verification)
- [Supported devices](#supported-devices)
  - [Exluded devices (Play Store) / Unsupported devices](#exluded-devices-play-store--unsupported-devices)
- [Screenshots](#screenshots)
- [Contribute](#contribute)
- [Used Icons](#used-icons)
- [License](#license)

---

[<img alt="Detailed App Review by 'Explaining Android'" src="https://i.imgur.com/2QU91tl.png" />](https://www.youtube.com/watch?v=mVX444ksdg0)
[<img alt="Featured on Android Authority: The best apps for Google Pixel 8" src="https://i.imgur.com/wJhiuyA.png" />](https://androidauthority.com/google-pixel-8-best-apps-3372729/)
[<img alt="Featured on Sam Beckman: Top 20 Android Apps 2023" src="https://i.imgur.com/IQbl2CU.png" />](https://youtu.be/TOxEvJ_YM5g?t=279)
[<img alt="Featured on Sam Beckman: The Best Free and Open Source Apps in 2023" src="https://i.imgur.com/sunDB4F.png" />](https://youtu.be/4gbSnaASTt0?t=140)
[<img alt="Featured on Sam Beckman: Top 20 Android Apps I Can't live without in 2023!" src="https://i.imgur.com/1Tiyk5L.png" />](https://youtu.be/s6KNdNlNXho?t=257)
[<img alt="Featured on HowToMen: 20 Best Android Apps 2023!" src="https://i.imgur.com/CUTXWGL.png" />](https://youtu.be/jFnLA5KV0L8?t=484)
[<img alt="Featured on HowToMen: Best Android Apps - December 2022" src="https://i.imgur.com/U57jXpn.png" />](https://youtu.be/C-qvkT2dawI?t=120)
---
[<img alt="Read Medium article" src="https://i.imgur.com/p6PHIK7.png" />](https://medium.com/p/835cdf2d6f3e)

---

## About this project
Starting with Android 13, it's possible to control multiple brightness levels of the flashlight.  
To be able to use this new feature I've developed this app.

Inspired by [polodarb/Flashlight-Tiramisu](https://github.com/polodarb/Flashlight-Tiramisu) 💛  
Custom SeekBar built with code from [massoudss/waveformSeekBar](https://github.com/massoudss/waveformSeekBar) 💛

## Features
- 🔦 dimming your flashlight level by level
- 🎚 shortcut buttons for different brightness levels
- 🆘 SOS flash button
- 📫 morse code flash mode
- ⏲️ Interval / BPM mode
- ⚡ quick settings tile for fast access
- 🔊 press both volume buttons for easy flashlight toggle
- 🔒 private, no ads, no internet connection
- 💯 modern Material You (M3) design elements
- 🎨 app colors adapt to device's system colors

## Download

[<img height=80 alt="Get it on Google Play"
src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
/>](http://play.google.com/store/apps/details?id=com.cyb3rko.flashdim)
[<img height="80" alt="Get it on F-Droid"
src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
/>](https://f-droid.org/app/com.cyb3rko.flashdim)
[<img height="80" src="https://raw.githubusercontent.com/gotify/android/master/download-badge.png"/>](https://github.com/cyb3rko/flashdim/releases/latest)

### Verification

The APK files can be verified using [apksigner](https://developer.android.com/studio/command-line/apksigner.html#options-verify).

```
apksigner verify --print-certs -v example.apk
```

---

**Google Play Store**  
The output should look like this:

```
Verifies
Verified using v1 scheme (JAR signing): false
Verified using v2 scheme (APK Signature Scheme v2): false
Verified using v3 scheme (APK Signature Scheme v3): true
```

The certificate content and digests should look like this:

```
DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
Certificate Digests:
  SHA-256: 4b:05:58:fa:9d:2e:32:b0:1c:d0:00:7b:13:66:35:b5:ac:67:b5:ca:63:ff:c2:ef:e2:58:9a:80:20:e8:ca:4f
  SHA-1:   47:2d:b2:9e:57:df:be:48:09:55:3b:f9:c1:d8:15:a5:e3:ed:27:82
  MD5:     14:cc:12:d7:6b:29:9a:79:64:ac:36:c8:56:ec:a5:78
```

---

**F-Droid and GitHub** (signed using the same key)    
The output should look like this:

```
Verifies
Verified using v1 scheme (JAR signing): false
Verified using v2 scheme (APK Signature Scheme v2): true
```

The certificate content and digests should look like this:

```
DN: OU=FlashDim, O=Cyb3rKo OpenSource, L=GitHub / F-Droid, C=DE
Certificate Digests:
  SHA-256: ea:78:69:1c:ba:41:86:00:a4:56:32:89:b1:33:f9:d0:0d:3d:0c:24:a5:f5:4c:5c:59:ee:1c:95:2b:b0:e1:1d
  SHA-1:   28:cf:15:ea:1c:93:1a:04:67:2d:8e:b0:2f:01:77:9b:03:90:f8:dd
  MD5:     09:05:c3:62:fa:94:a0:2e:c4:9c:d0:4e:b8:a3:cc:99
```

## Supported Devices
The full functionalizy of this app is ONLY AVAILABLE for devices which support the dim functionality of the flashlight (hardware limited).  
Here's a list of devices I know of which do work FOR SURE. If you've used a device that works too please let me know so I can add it here.

- Google Pixel 6 series
- Google Pixel 7 series
- nearly every Samsung phone with Android 13
- many other phones from different manufacturers

### Exluded devices (Play Store) / Unsupported devices

The following list shows phones, which were manually excluded by myself in the Google Play Store, because of unsupported flashlight dimming functionality.  
If the list contains any phone which does has a dimmable flashlight, please let me know.

- Asus ROG Phone 6 (asus ASUS_AI2201)
- Asus ROG Phone 5 (asus ASUS_I005_1)
- Google Pixel 4 (google flame)
- Google Pixel 4 XL (google coral)
- Google Pixel 4a 5G (google bramble)
- Google Pixel 5 (google redfin)
- Google Pixel 5a 5G (google barbet)
- Lava Blaze 5G (LAVA LXX503)
- Motorola motorola edge 30 (motorola dubai)
- Nothing Phone 2 (Nothing Pong)
- OnePlus 8T (OnePlus OnePlus8T)
- OnePlus 11 5G (OnePlus OP594DL1)
- OnePlus 11R 5G (OnePlus OP5961L1)
- OnePlus Nord CE 2 Lite 5G (OnePlus OP535DL1)
- Oppo CPH2145 (OPPO OP4F1BL1)
- Oppo CPH2249 (OPPO OP4F81L1)
- Oppo CPH2293 (OPPO OP52E1L1)
- Oppo OPPO Reno6 Z 5G (OPPO OP4FA7L1)
- Oppo OPPO Reno7 (OPPO OP533FL1)
- POCO F4 (POCO munch)
- Redmi Note 10 Pro (Redmi sweetin)
- realme 10 Pro+ 5G (realme RE58A5L1)
- realme 11 Pro+ 5G (realme RE58B6L1)
- realme narzo 30 5G (realme RE513CL1)
- Realme realme GT Master Edition (realme RE54ABL1)
- Realme realme GT Neo2 5G (realme RE879AL1)
- realme 真我GT 2 (realme RE58B2L1)
- Vivo I2011 (iQOO 2011)
- Vivo V2053 (vivo 2041)
- Vivo V27 (vivo V2231)
- Xiaomi 12T Pro (Xiaomi diting)
- Xiaomi 13T (Xiaomi aristotle)

## Screenshots
|<img src="https://i.imgur.com/v0sPQYL.png" width="250">|<img src="https://i.imgur.com/iCfW7qm.png" width="250">|<img src="https://i.imgur.com/SoUi95t.png" width="250">|<img src="https://i.imgur.com/mQONbW5.png" width="250">|
|:---:|:---:|:---:|:---:|
|<img src="https://i.imgur.com/lAkL2Cn.png" width="250">|<img src="https://i.imgur.com/R9LiPry.png" width="250">|


## Contribute
Of course I'm happy about any kind of contribution.

For creating [issues](https://github.com/cyb3rko/flashdim/issues) there's no real guideline you should follow.
If you create [pull requests](https://github.com/cyb3rko/flashdim/pulls) please try to use the syntax I use.
Using a unified code format makes it much easier for me and for everyone else.

## Used Icons

| 💛 |
| --- |  
| <a href="https://www.flaticon.com/free-icons/github" title="github icons">Github icons created by Laisa Islam Ani - Flaticon</a> |
| <a href="https://www.flaticon.com/free-icons/information" title="information icons">Information icons created by Freepik - Flaticon</a> |
| <a href="https://www.flaticon.com/free-icons/link" title="link icons">Link icons created by Creaticca Creative Agency - Flaticon</a> |
| <a href="https://www.flaticon.com/free-icons/morse-code" title="morse code icons">Morse code icons created by Freepik - Flaticon</a> |
| <a href="https://www.flaticon.com/free-icons/restart" title="restart icons">Restart icons created by AlternativeStd - Flaticon</a> |
| <a href="https://www.flaticon.com/free-icons/settings" title="settings icons">Settings icons created by Freepik - Flaticon</a> |
| <a href="https://www.flaticon.com/free-icons/start-button" title="start button icons">Start button icons created by Freepik - Flaticon</a> |
| <a href="https://www.flaticon.com/free-icons/ui" title="ui icons">Ui icons created by flashicon - Flaticon</a> |
| <a href="https://www.flaticon.com/free-icons/vibrate" title="vibrate icons">Vibrate icons created by Bombasticon Studio - Flaticon</a> |
| <a href="https://www.flaticon.com/free-icons/volume" title="volume icons">Volume icons created by Freepik - Flaticon</a> |

## License

    Copyright 2023, Cyb3rKo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
