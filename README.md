# DigiQuest
Text adventure game that interfaces with lib-dcom for battles

Compose Multiplatform Application

**Desktop**
- `./gradlew desktop:run` - run application

[//]: # (**Android**)

[//]: # (- `./gradlew installDebug` - install Android application on an Android device &#40;on a real device or on an emulator&#41;)
## Build Distributable
* Requires Java 15+
* run `gradlew.bat desktop:createDistributable`
* Download warp-packer https://github.com/dgiagio/warp/releases/download/v0.3.0/windows-x64.warp-packer.exe
* Run `warp-packer.exe --arch windows-x64 --input_dir desktop\build\compose\binaries\main\app\DigiQuest --exec DigiQuest.exe --output DigiQuest-${version}.exe`
