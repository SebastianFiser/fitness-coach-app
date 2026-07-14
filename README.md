
# Fitcoach App

A gamified Android fitness helper -plan your weekly schedule, track sets and prs, and compete on the leaderboard
## Badges

![Platform](https://img.shields.io/badge/Platform-Android-green) ![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue) ![License](https://img.shields.io/badge/License-Custom-red)

## Screenshots

| Workout Flow | Account |
|--------------|-----------| 
|![](https://cdn.hackclub.com/019ef978-9919-7a13-812e-a4d4a7fc2c2e/screenshot_2026-06-24-13-48-47-887_com.sebastianfiser.fitnesscoach.jpg)| ![](https://cdn.hackclub.com/019ef978-9b94-7e75-9f10-3f13d2527641/screenshot_2026-06-24-13-48-30-827_com.sebastianfiser.fitnesscoach.jpg) |
| Leaderboard | Overview |
| ![](https://cdn.hackclub.com/019ef978-9ddd-7e47-9063-b48cbe19af7d/screenshot_2026-06-24-13-48-27-096_com.sebastianfiser.fitnesscoach.jpg) | ![](https://cdn.hackclub.com/019ef978-a04c-70fb-aa04-951df8586374/screenshot_2026-06-24-13-48-20-866_com.sebastianfiser.fitnesscoach.jpg) |




## Features

- dark mode
- Custom weekly workout schedule -set your exercises for each day of the week!
- Set-by-set workout tracking-logging each one of your sets, and pulls your sets for prs and other statistics
- Rest timer -automatic countdown inbetween sets
- Workout completion tracking -visual indicator while working out.
- Personal records and statistics - overview of your topsets, prs and weight increase
- Cloud Sync - Your data is backed up in appwrite database
- Account sys. - registration and logging in
## Tech Stack

**Client:** 
### Language and UI
 - Kotlin --Primary Language
 - Jetpack Compose --declarative UI
 - Material3 - design system

### Architecture
 - MVVM - ViewModel -> Repository pattern
 - Jetpack navigation -- in-app navigation

**Server:**
### Backend & Storage
 - Appwrite -- auth, database, bucket

**Additional features**
### Github CI
 - runs gradle build system
 - debug release publish
 - releases tab cleanup

### Build system
 - Gradlew
## Run Locally

Supports easy, out-of-the-box running
**Two use options**
## Consumer
- download latest NON pre-releases from ![here](https://github.com/SebastianFiser/fitness-coach-app/releases)
- install the .apk package, and use !!
**Disclaimer**
- because the app uses internet, and it isnt **Signed** package, google will try to warn you, but the app will function normally if you ignore it.

## Acknowledgements

- [HackClub](https://hackclub.com) - for inspiration and awsome rewards
- [Appwrite](https://appwrite.io) - open-source provider of free backend
- [readme.so](https://readme.so) - README editor
- [Kimplify countries](https://github.com/Kimplify/KCountries) -providing country code library for kotlin
## License

[custom](https://github.com/SebastianFiser/fitness-coach-app/blob/main/LICENSE)

## Releases

First official signed release v1.0.0 beta is out!! go and check it in releases
