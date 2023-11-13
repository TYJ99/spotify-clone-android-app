# spotify-clone-android-app  
Spotify Clone music player Android App (Kotlin, Firestore, MVVM, Hilt, Media3, Pager, Compose) (Demo)		      
- Reduced code maintenance efforts due to MVVM with Clean Architecture, resulting in a highly modular and scalable codebase.
- Crafted UI with Jetpack Compose, using LazyColumn for efficient audio browsing and Compose Navigation for a clear flow.
- Integrated ViewModel with StateFlow, ensuring real-time updates to the UI and enhancing user engagement.
- Implemented a visually captivating splash screen with animations created in Figma and Shape Shifter, using the SplashScreen API.
- Enhanced media playback capabilities using Media3 ExoPlayer, offering users high-quality audio playback.
- Integrated Firebase as the database backend, providing users with seamless access to the music source.
- Implemented dynamic image loading from URLs with Glide, ensuring fast and reliable image retrieval.
- Implemented a song pager for seamless song transitions and implemented audio playback animations for the application.
- Reduced latency in asynchronous operations by utilizing Coroutines, enhancing user experience responsiveness. 
- Reduced code complexity by 30% via efficient dependency injection with Hilt, enhancing code maintainability and scalability.

Here is the [APK](./app/release/app-release.apk)  

It supports several features:  
1. Animated Splash Screen:

https://github.com/TYJ99/spotify-clone-android-app/assets/97372852/1aed4956-7d07-4ff2-9142-b052974fb170


2. Home Screen:
    - Audio list: Show animation while playing the audio
    - play/pause the audio when you click the selected song
    - Pager player: next/previous audio, play/pause

https://github.com/TYJ99/spotify-clone-android-app/assets/97372852/e0c6a7c7-98e2-4b13-90fb-ed653966a9d7

    - Normal player: next/previous audio, play/pause

https://github.com/TYJ99/spotify-clone-android-app/assets/97372852/9f408581-2e92-4713-81c5-3a781b457bbd

3. Song Screen:
    - next/previous audio, rewind/fast forward, play/pause, image animation, playback animation

https://github.com/TYJ99/spotify-clone-android-app/assets/97372852/92e4df84-4b99-44c4-aa04-46cf74992865


4. Predictive back gesture

https://github.com/TYJ99/spotify-clone-android-app/assets/97372852/432626b2-0353-417c-9758-6e7d51f69ba3


5. The music player keeps playing the audio while the app is in the background.

https://github.com/TYJ99/spotify-clone-android-app/assets/97372852/e50936a0-438c-4ada-890d-41df60323a1b

