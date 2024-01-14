# FlashDim
[![API](https://img.shields.io/badge/API-33%2B-brightgreen.svg?style=flat)](https://apilevels.com)
[![gplay](https://img.shields.io/endpoint?url=https%3A%2F%2Fplay.cuzi.workers.dev%2Fplay%3Fi%3Dcom.cyb3rko.flashdim%26l%3Dgplay%26m%3Dv%24version&color=blue)](https://play.google.com/store/apps/details?id=com.cyb3rko.flashdim)
[![fdroid](https://img.shields.io/f-droid/v/com.cyb3rko.flashdim.svg)](https://f-droid.org/packages/com.cyb3rko.flashdim)
[![github](https://img.shields.io/github/release/cyb3rko/flashdim.svg)](https://github.com/cyb3rko/flashdim/releases/latest)
[![license](https://img.shields.io/github/license/cyb3rko/logviewer-for-openhab-app)](https://www.apache.org/licenses/LICENSE-2.0)
[![last commit](https://img.shields.io/github/last-commit/cyb3rko/flashdim?color=F34C9F)](https://github.com/cyb3rko/flashdim/commits/main)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/4e7fd3ea03da4a32aae48ff39693ee91)](https://www.codacy.com/gh/cyb3rko/flashdim/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cyb3rko/flashdim&amp;utm_campaign=Badge_Grade)

[![FOSSA Status](https://app.fossa.com/api/projects/custom%2B35689%2Fgithub.com%2Fcyb3rko%2Fflashdim.svg?type=small)](https://fossa.com/)

<p align="center">
  <a href="https://play.google.com/store/apps/details?id=com.cyb3rko.flashdim"><img alt="FlashDim" src="https://playbadges.pavi2410.me/badge/full?id=com.cyb3rko.flashdim" height="120"/></a>
</p>

---

- [About this project](#about-this-project)   
- [Features](#features)  
- [Download](#download)
  - [Verification](#verification)
- [Screenshots](#screenshots)
- [Supported devices](#supported-devices)
  - [Excluded devices (Play Store) / Unsupported devices](#excluded-devices-play-store--unsupported-devices)
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

> [!IMPORTANT]  
> The full functionality of this app is ONLY AVAILABLE for devices which support the dim functionality of the flashlight (hardware limited).
> See [Supported devices](#supported-devices)

If you think it's worth to support this project, feel free to give a small donation :heart:.

---

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
[<img height="80" src="https://github.com/machiav3lli/oandbackupx/blob/034b226cea5c1b30eb4f6a6f313e4dadcbb0ece4/badge_github.png"/>](https://github.com/cyb3rko/flashdim/releases/latest)

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

## Screenshots
|<img src="metadata/en-US/images/phoneScreenshots/1.png" width="250">|<img src="metadata/en-US/images/phoneScreenshots/2.png" width="250">|<img src="metadata/en-US/images/phoneScreenshots/3.png" width="250">|<img src="metadata/en-US/images/phoneScreenshots/4.png" width="250">|
|:---:|:---:|:---:|:---:|
|<img src="metadata/en-US/images/phoneScreenshots/5.png" width="250">|<img src="metadata/en-US/images/phoneScreenshots/6.png" width="250">|

## Supported Devices
The full functionality of this app is ONLY AVAILABLE for devices which support the dim functionality of the flashlight (hardware limited).  
Here's a list of devices I know of which do support flashlight dimming.

- Google Pixel 6/7/8 series
- nearly every Samsung phone with Android 13
- many other devices from different manufacturers

### Excluded devices (Play Store) / Unsupported devices

The following list shows phones, which were manually excluded by myself in the Google Play Store, because of unsupported flashlight dimming functionality.  
If the list contains any phone which does has a dimmable flashlight, please let me know.

<!--- marker1 -->
<em>Total: 241</em>
<details>
  <summary>Asus (4)</summary>
  <ul>
    <li>Asus ROG Phone 5 (asus ASUS_I005_1)</li>
    <li>Asus ROG Phone 6 (asus ASUS_AI2201)</li>
    <li>Asus Zenfone 8 (asus ASUS_I006D)</li>
    <li>Asus Zenfone 8 Flip (asus ASUS_I004D)</li>
  </ul>
</details>
<details>
  <summary>Blu (1)</summary>
  <ul>
    <li>Blu G33 (BLU G0890, BLU G0892)</li>
  </ul>
</details>
<details>
  <summary>Cubot (1)</summary>
  <ul>
    <li>Cubot P80 (CUBOT P80)</li>
  </ul>
</details>
<details>
  <summary>Fairphone (3)</summary>
  <ul>
    <li>Fairphone 5 5G (Fairphone FP5)</li>
    <li>Fairphone Fairphone3 (Fairphone FP3)</li>
    <li>Fairphone Fairphone4 (Fairphone FP4)</li>
  </ul>
</details>
<details>
  <summary>Google (6)</summary>
  <ul>
    <li>Google Pixel 4 (google flame)</li>
    <li>Google Pixel 4 XL (google coral)</li>
    <li>Google Pixel 4a (google sunfish)</li>
    <li>Google Pixel 4a 5G (google bramble)</li>
    <li>Google Pixel 5 (google redfin)</li>
    <li>Google Pixel 5a 5G (google barbet)</li>
  </ul>
</details>
<details>
  <summary>Honor (3)</summary>
  <ul>
    <li>Honor HONOR 90 Lite (HONOR HNCRT-M2)</li>
    <li>Honor HONOR X7 (HONOR HNCMA-Q)</li>
    <li>Honor HONOR X8 (HONOR HNTFY-Q)</li>
  </ul>
</details>
<details>
  <summary>Infinix (10)</summary>
  <ul>
    <li>Infinix GT 10 Pro (Infinix Infinix-X6739)</li>
    <li>Infinix NOTE 12 (Infinix Infinix-X670)</li>
    <li>Infinix NOTE 12 Pro 5G (Infinix Infinix-X671B)</li>
    <li>Infinix NOTE 30 (Infinix Infinix-X6833B, Infinix Infinix-X6716, Infinix Infinix-X6716B)</li>
    <li>Infinix NOTE 30 5G (Infinix Infinix-X6711)</li>
    <li>Infinix NOTE 30 Pro (Infinix Infinix-X678B)</li>
    <li>Infinix NOTE 30 VIP (Infinix Infinix-X6710)</li>
    <li>Infinix ZERO 30 (Infinix Infinix-X6731B)</li>
    <li>Infinix ZERO 30 5G (Infinix Infinix-X6731)</li>
    <li>Infinix ZERO 5G 2023 (Infinix Infinix-X6815C)</li>
  </ul>
</details>
<details>
  <summary>Itel (1)</summary>
  <ul>
    <li>Itel itel P55 Bandhan (Itel itel-P661N)</li>
  </ul>
</details>
<details>
  <summary>Lava (3)</summary>
  <ul>
    <li>Lava Blaze 5G (LAVA LXX503)</li>
    <li>Lava Blaze Pro 5G (LAVA LXX506)</li>
    <li>Lava LXX504 (LAVA LXX504)</li>
  </ul>
</details>
<details>
  <summary>LGE (3)</summary>
  <ul>
    <li>LGE LG V60 ThinQ (lge timelm)</li>
    <li>LGE LG WING (lge winglm)</li>
    <li>LGE VELVET (lge caymanlm)</li>
  </ul>
</details>
<details>
  <summary>Mi (1)</summary>
  <ul>
    <li>Mi 11 X (Mi aliothin)</li>
  </ul>
</details>
<details>
  <summary>Motorola (15)</summary>
  <ul>
    <li>Motorola moto g13 (motorola penangf)</li>
    <li>Motorola moto g32 (motorola devon)</li>
    <li>Motorola moto g52 (motorola rhode)</li>
    <li>Motorola moto g54 5G (motorola cancunf)</li>
    <li>Motorola moto g62 5G (motorola rhodei, motorola rhodec)</li>
    <li>Motorola moto g72 (motorola vicky)</li>
    <li>Motorola moto g73 5G (motorola devonf)</li>
    <li>Motorola moto g82 5G (motorola rhodep)</li>
    <li>Motorola moto g84 5G (motorola bangkk)</li>
    <li>Motorola motorola edge 20 (motorola berlin)</li>
    <li>Motorola motorola edge 30 (motorola dubai)</li>
    <li>Motorola motorola edge 30 neo (motorola miami)</li>
    <li>Motorola motorola edge 30 ultra (motorola eqs)</li>
    <li>Motorola motorola edge 40 (motorola lyriq)</li>
    <li>Motorola motorola edge 40 neo (motorola manaus)</li>
  </ul>
</details>
<details>
  <summary>Nothing (2)</summary>
  <ul>
    <li>Nothing Phone 1 (Nothing Spacewar)</li>
    <li>Nothing Phone 2 (Nothing Pong)</li>
  </ul>
</details>
<details>
  <summary>OnePlus (20)</summary>
  <ul>
    <li>OnePlus 8 5G (OnePlus OnePlus8TMO)</li>
    <li>OnePlus 8 Pro (OnePlus OnePlus8Pro)</li>
    <li>OnePlus 8T (OnePlus OnePlus8T)</li>
    <li>OnePlus 9 5G (OnePlus OnePlus9, OnePlus OnePlus9TMO)</li>
    <li>OnePlus 9 Pro 5G (OnePlus OnePlus9Pro, OnePlus OnePlus9ProTMO)</li>
    <li>OnePlus 9R (OnePlus OnePlus9R)</li>
    <li>OnePlus 9RT 5G (OnePlus OP5154L1, OnePlus OP5155L1)</li>
    <li>OnePlus 10 Pro 5G (OnePlus OP516FL1)</li>
    <li>OnePlus 10R 5G (OnePlus OP5566L1, OnePlus OP5567L1)</li>
    <li>OnePlus 10T 5G (OnePlus OP5552L1)</li>
    <li>OnePlus 11 5G (OnePlus OP594DL1)</li>
    <li>OnePlus 11R 5G (OnePlus OP5961L1)</li>
    <li>OnePlus Nord 2T 5G (OnePlus OP557AL1)</li>
    <li>OnePlus Nord 3 5G (OnePlus OP556FL1)</li>
    <li>OnePlus Nord CE 2 (OnePlus OP555BL1)</li>
    <li>OnePlus Nord CE 2 Lite 5G (OnePlus OP535DL1)</li>
    <li>OnePlus Nord CE 3 Lite 5G (OnePlus OP5958L1)</li>
    <li>OnePlus Nord CE 5G (OnePlus OnePlusNordCE)</li>
    <li>OnePlus Nord N30 5G (OnePlus OP5958L1)</li>
    <li>OnePlus Nord2 5G (OnePlus OP515BL1)</li>
  </ul>
</details>
<details>
  <summary>Oppo (15)</summary>
  <ul>
    <li>Oppo A38 (OPPO OP5759L1)</li>
    <li>Oppo CPH2145 (OPPO OP4F1BL1)</li>
    <li>Oppo CPH2249 (OPPO OP4F81L1)</li>
    <li>Oppo CPH2251 (OPPO OP4F83L1)</li>
    <li>Oppo CPH2293 (OPPO OP52E1L1)</li>
    <li>Oppo CPH2473 (OPPO OP5637L1)</li>
    <li>Oppo CPH2527 (OPPO OP56E7L1)</li>
    <li>Oppo Find X5 (OPPO OP52D5L1)</li>
    <li>Oppo OPPO Reno6 Z 5G (OPPO OP4FA7L1)</li>
    <li>Oppo OPPO Reno7 (OPPO OP533FL1)</li>
    <li>Oppo OPPO Reno8 5G (OPPO OP5339L1)</li>
    <li>Oppo OPPO Reno10 5G (OPPO OP5655)</li>
    <li>Oppo Reno5 Lite (OPPO OP4F43L1)</li>
    <li>Oppo Reno8 T 5G (OPPO OP56EDL1)</li>
    <li>Oppo Reno10 5G (OPPO OP5705L1)</li>
  </ul>
</details>
<details>
  <summary>POCO (12)</summary>
  <ul>
    <li>POCO C55 (POCO earth)</li>
    <li>POCO F3 (POCO alioth)</li>
    <li>POCO F4 (POCO munch)</li>
    <li>POCO F5 (POCO marble)</li>
    <li>POCO M4 5G (POCO thunder)</li>
    <li>POCO M4 Pro (POCO miel)</li>
    <li>POCO M5 (POCO stone, POCO rock)</li>
    <li>POCO M6 Pro 5G (POCO sky)</li>
    <li>POCO X2 (POCO phoenixin)</li>
    <li>POCO X3 Pro (POCO bhima, POCO vayu)</li>
    <li>POCO X4 GT (POCO xaga)</li>
    <li>POCO X4 Pro 5G (POCO peux, POCO veux)</li>
  </ul>
</details>
<details>
  <summary>Redmi (28)</summary>
  <ul>
    <li>Redmi 10 2022 (Redmi selene)</li>
    <li>Redmi 10 5G (Redmi thunder)</li>
    <li>Redmi 10C (Redmi rain, Redmi wind, Redmi fog)</li>
    <li>Redmi 12 (Redmi fire, Redmi heat)</li>
    <li>Redmi 12 5G (Redmi XIG03, Redmi river)</li>
    <li>Redmi 12C (Redmi earth)</li>
    <li>Redmi A2 (Redmi cloud)</li>
    <li>Redmi A2+ (Redmi water)</li>
    <li>Redmi K40 (Redmi alioth)</li>
    <li>Redmi K50i (Redmi xagain)</li>
    <li>Redmi Note 10 Pro (POCO chopin, Redmi sweet, Redmi sweetin)</li>
    <li>Redmi Note 10S (Redmi secret, Redmi rosemary, Redmi maltose)</li>
    <li>Redmi Note 11 (Redmi spes, Redmi spesn)</li>
    <li>Redmi Note 11 Pro (Redmi viva, Redmi vida, Redmi pissarro)</li>
    <li>Redmi Note 11 Pro 5G (Redmi veux)</li>
    <li>Redmi Note 11 Pro+ 5G (Redmi peux, Redmi pissarropro)</li>
    <li>Redmi Note 11 SE (Redmi secret)</li>
    <li>Redmi Note 11S (Redmi miel)</li>
    <li>Redmi Note 11T 5G (Redmi evergo)</li>
    <li>Redmi Note 11T Pro (Redmi xaga)</li>
    <li>Redmi Note 11T Pro + (Redmi xagapro)</li>
    <li>Redmi Note 12 (Redmi tapas, Redmi topaz)</li>
    <li>Redmi Note 12 5G (Redmi sunstone)</li>
    <li>Redmi Note 12 Pro 5G (Redmi ruby)</li>
    <li>Redmi Note 12 Pro+ 5G (Redmi rubypro)</li>
    <li>Redmi Note 12 Turbo (Redmi marble)</li>
    <li>Redmi Note 13 Pro 5G (Redmi garnet)</li>
    <li>Redmi POCO F5 (POCO marblein)</li>
  </ul>
</details>
<details>
  <summary>realme (46)</summary>
  <ul>
    <li>realme 8 (realme RMX3085L1)</li>
    <li>realme 8i (realme RE54B4L1)</li>
    <li>realme 9 (realme RE54E2L1)</li>
    <li>realme 9 5G (realme RE54CBL1, realme RE54F3L1)</li>
    <li>realme 9i (realme RED8C1L1)</li>
    <li>realme 9i 5G (realme RE588DL1)</li>
    <li>realme 9Pro 5G (realme RE54CBL1)</li>
    <li>realme 9 Pro+ (realme RE879EL1)</li>
    <li>realme 10 (realme RE8DDCL1)</li>
    <li>realme 10 Pro (realme RE5849)</li>
    <li>realme 10 Pro 5G (realme RE588BL1, realme RE58B8L1)</li>
    <li>realme 10 Pro+ (realme RE5854)</li>
    <li>realme 10 Pro+ 5G (realme RE58A5L1, realme RE58A6L1)</li>
    <li>realme 11 (realme RE5852, realme RE5C7CL1)</li>
    <li>realme 11 Pro+ (realme RE5865)</li>
    <li>realme 11 Pro+ 5G (realme RE58B6L1)</li>
    <li>realme 11x 5G (realme RE5C6CL1)</li>
    <li>realme C25 (realme RMX3191, realme RMX3193)</li>
    <li>realme C25s (realme RMX3195, realme RMX3197)</li>
    <li>realme C31 (realme RE549C)</li>
    <li>realme C33 (realme RE5894, realme RE58AF)</li>
    <li>realme C35 (realme RE87BAL1)</li>
    <li>realme C51 (realme RE58BC)</li>
    <li>realme C53 (realme RE58C2)</li>
    <li>realme C55 (realme REE2ADL1)</li>
    <li>realme GT 2 Pro (realme RED8ACL1)</li>
    <li>realme GT NEO 3 150W (realme RED8BEL1)</li>
    <li>realme GT NEO 3T (realme RE54E4L1)</li>
    <li>realme narzo 30 5G (realme RE513CL1)</li>
    <li>realme narzo 50 (realme RE54B4L1)</li>
    <li>realme narzo 50 5G (realme RED8CDL1)</li>
    <li>realme narzo 50 Pro 5G (realme RE879EL1)</li>
    <li>realme narzo 50A (realme RED8AF)</li>
    <li>realme narzo 50A Prime (realme RE54D8L1)</li>
    <li>realme narzo N53 (realme RE58C6)</li>
    <li>realme realme 9 5G Speed Edition (realme RE54BFL1)</li>
    <li>Realme realme GT Master Edition (realme RE54ABL1)</li>
    <li>Realme realme GT Neo 2 (realme RE5473)</li>
    <li>Realme realme GT Neo2 5G (realme RE879AL1)</li>
    <li>realme RMX3710 (realme REE2ADL1)</li>
    <li>realme X7 Max (realme RMX3031L1)</li>
    <li>realme 真我11 Pro (realme RE5869)</li>
    <li>realme 真我GT 2 (realme RE58B2L1, realme RE5471)</li>
    <li>realme 真我GT Neo (realme RMX3031CN)</li>
    <li>Realme 真我GT Neo2T (realme RE5469)</li>
    <li>realme 真我 GT NEO 3 150W (realme RE5489)</li>
  </ul>
</details>
<details>
  <summary>Sky Devices (1)</summary>
  <ul>
    <li>Sky Devices SKY PAD8PRO (SKY_Devices SKY_PAD8PROUSON)</li>
  </ul>
</details>
<details>
  <summary>Sony (2)</summary>
  <ul>
    <li>Sony Xperia 1 V (Sony XQ-DQ72)</li>
    <li>Sony Xperia 10 III (Sony XQ-BT52)</li>
  </ul>
</details>
<details>
  <summary>Tecno (5)</summary>
  <ul>
    <li>Tecno POVA 5 Pro 5G (TECNO TECNO-LH8n)</li>
    <li>Tecno SPARK Go 2024 (TECNO TECNO-BG6)</li>
    <li>Tecno TECNO SPARK 10 Pro (TECNO TECNO-KI7, TECNO TECNO-KI7s)</li>
    <li>Tecno TECNO CAMON 20 (TECNO TECNO-CK6n, TECNO TECNO-CK6ns)</li>
    <li>Tecno TECNO SPARK 20C (TECNO TECNO-BG7, TECNO TECNO-BG7n)</li>
  </ul>
</details>
<details>
  <summary>Umidigi (3)</summary>
  <ul>
    <li>Umidigi G3 EEA (UMIDIGI UMIDIGI_G3)</li>
    <li>Umidigi G3 Plus EEA (UMIDIGI G3_Plus)</li>
    <li>Umidigi UMIDIGI G3 Max EEA (UMIDIGI G3_Max)</li>
  </ul>
</details>
<details>
  <summary>Unihertz (1)</summary>
  <ul>
    <li>Unihertz Jelly Star (Unihertz Jelly_Star)</li>
  </ul>
</details>
<details>
  <summary>Vivo (45)</summary>
  <ul>
    <li>Vivo I2011 (iQOO 2011)</li>
    <li>Vivo I2012 (iQOO 2012)</li>
    <li>Vivo I2017 (iQOO 2017)</li>
    <li>Vivo I2202 (iQOO I2202)</li>
    <li>Vivo iQOO 9 SE (iQOO 2019)</li>
    <li>Vivo iQOO 9T (iQOO I2201)</li>
    <li>Vivo iQOO Neo7 (iQOO I2214, vivo PD2231)</li>
    <li>Vivo iQOO Neo7 Pro (iQOO I2217)</li>
    <li>Vivo iQOO Neo7 SE (vivo PD2238)</li>
    <li>Vivo iQOO Neo7 竞速版 (vivo PD2232)</li>
    <li>Vivo iQOO Z7 5G (iQOO I2207)</li>
    <li>Vivo iQOO Z7s 5G (iQOO I2223)</li>
    <li>Vivo T1 (vivo V2154)</li>
    <li>Vivo T1 Pro 5G (vivo V2151)</li>
    <li>Vivo T2 (vivo V2237)</li>
    <li>Vivo T2 Pro 5G (vivo V2321)</li>
    <li>Vivo T2x 5G (vivo V2225)</li>
    <li>Vivo V2025 (vivo 2025)</li>
    <li>Vivo V2041 (vivo 2041)</li>
    <li>Vivo V2050 (vivo 2050)</li>
    <li>Vivo V2045 (vivo 2045)</li>
    <li>Vivo V2053 (vivo 2041)</li>
    <li>Vivo V2066 (vivo 2050)</li>
    <li>Vivo V2109 (vivo 2109)</li>
    <li>Vivo V2115 (vivo 2116)</li>
    <li>Vivo V2116 (vivo 2116)</li>
    <li>Vivo V2117 (vivo 2116)</li>
    <li>Vivo V2124 (vivo 2124)</li>
    <li>Vivo V2126 (vivo 2126)</li>
    <li>Vivo V2130 (vivo 2130)</li>
    <li>Vivo V2130A (vivo PD2130)</li>
    <li>Vivo V2141 (vivo 2141)</li>
    <li>Vivo V2141A (vivo PD2141)</li>
    <li>Vivo V2152 (vivo 2110)</li>
    <li>Vivo V25 Pro (vivo V2158)</li>
    <li>Vivo V25e (vivo V2201, vivo V2237)</li>
    <li>Vivo V27 (vivo V2231)</li>
    <li>Vivo V27 Pro (vivo V2230)</li>
    <li>Vivo V27e (vivo V2237)</li>
    <li>Vivo V29 (vivo V2250)</li>
    <li>Vivo vivo 2018 (vivo 2018)</li>
    <li>Vivo X80 (vivo V2144)</li>
    <li>Vivo Y100 (vivo V2239)</li>
    <li>Vivo Y27 (vivo V2249)</li>
    <li>Vivo Y35 (vivo V2205)</li>
  </ul>
</details>
<details>
  <summary>Xiaomi (10)</summary>
  <ul>
    <li>Xiaomi 11 Lite 5G NE (Xiaomi lisa)</li>
    <li>Xiaomi 11i (Xiaomi pissarroin)</li>
    <li>Xiaomi 11i HyperCharge (Xiaomi pissarroinpro)</li>
    <li>Xiaomi 11T Pro (Xiaomi vili)</li>
    <li>Xiaomi 12 Pro (Xiaomi zeus)</li>
    <li>Xiaomi 12T Pro (Xiaomi diting, Redmi diting)</li>
    <li>Xiaomi 12X (Xiaomi psyche)</li>
    <li>Xiaomi 13 (Xiaomi fuxi)</li>
    <li>Xiaomi 13T (Xiaomi aristotle, Xiaomi XIG04)</li>
    <li>Xiaomi Mi 11 Lite (Xiaomi courbet, Xiaomi courbetin)</li>
  </ul>
</details>
<!--- marker2 -->

## Contribute
Of course, I'm happy about any kind of contribution.

For creating [issues](https://github.com/cyb3rko/flashdim/issues) there's no real guideline you should follow.
If you create [pull requests](https://github.com/cyb3rko/flashdim/pulls) please try to use the syntax I use.
Using a unified code format makes it much easier for me and for everyone else.

## Used Icons

| 💛 |
| --- |
| <a href="https://pictogrammers.com/library/mdi/icon/information-slab-circle-outline">information-slab-circle-outline created by Jeff Anders - Pictogrammers</a> |
| <a href="https://pictogrammers.com/library/mdi/icon/vibrate">vibrate created by Google - Pictogrammers</a> |
| <a href="https://pictogrammers.com/library/mdi/icon/rocket-launch-outline">rocket-launch-outline created by Michael Irigoyen - Pictogrammers</a> |

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
