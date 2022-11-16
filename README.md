# Sparkled

## Under active development
Sparkled is in a pre-alpha state. You are more than welcome to experiment with Sparkled, but it's not yet ready for
production use.

Sparkled is a web-based LED sequencer that aims to reduce the barrier to entry for lighting projects.
![Sparkled](https://github.com/sparkled/sparkled/raw/gh-pages/images/sparkled-screenshot.jpg "Sparkled")

## Features
With Sparkled, you get an integrated solution containing:
 * A rich web interface for staging, sequencing, previewing and scheduling music-backed LED animations
 * A fast UDP service to stream rendered pixel data to Sparkled clients, which use the data to drive LED strips (or
   whatever you want!)

## Clients
Sparkled clients are responsible for rendered pixel data from the Sparkled server and displaying it on the LED strips.
The official Sparkled clients are listed below:

- [sparkled-client-esp](https://github.com/sparkled/sparkled-client-esp) (ESP32 and ESP8266)

## Roadmap
- [ ] Add more effect types
- [ ] Multithreaded rendering
- [ ] Support sequences without music (currently requires a mute MP3 file to be uploaded)
- [ ] Ability to replace MP3 files for existing sequences (even with different lengths)

## Getting Started
Sparkled is currently in pre-alpha, so significant chunks of core functionality are still missing or incomplete.

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites
The following software and hardware requirements must be met before installing the Sparkled server:
* Java 11 or later
* Gradle 7 or later (or use `gradlew`)
* A modern web browser

### Installing
[Fork](https://help.github.com/articles/fork-a-repo) and clone the repository
```
SSH:
git clone git@github.com:<your_username>/sparkled.git

HTTPS:
git clone https://github.com/<your_username>/sparkled.git
```

##Build the project

**Note:** the initial build will take quite a while, as the dependencies need to be downloaded.
```
cd sparkled
./gradlew clean buildWebUi copyWebUi build
```

Run Sparkled
```
cd build/libs
java -jar sparkled-all.jar
```

Wait for the application to start, then connect to the UI in Chrome via the following URL:
```
http://localhost
```

## Running the tests
Use `gradle` to run the backend unit tests:
```
cd sparkled
./gradlew test
```

Use `npm` to run the frontend unit tests:
```
cd sparkled/webui
npm run test
```

## Built With
* [Kotlin](https://kotlinlang.org)
* [Micronaut](https://micronaut.io)
* [Gradle](https://gradle.org)
* [HikariCP](https://github.com/brettwooldridge/HikariCP)
* [React.js](https://reactjs.org)
* [Sqlite](https://sqlite.org)
* [Wavesurfer.js](https://wavesurfer-js.org)

## Authors
* **Chris Parton** - [chrisparton1991](https://github.com/chrisparton1991)

See also the list of [contributors](https://github.com/sparkled/sparkled/contributors) who participated in this project.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments
* [EJ-Technologies](https://www.ej-technologies.com) for providing an Open Source license for
  [JProfiler, the award-winning all-in-one Java Profiler](https://www.ej-technologies.com/products/jprofiler/overview.html).
* [FastLED](http://fastled.io) for building and maintaining an incredible LED strip library, without which this project
  would have never been conceived.
* [The FastLED community](https://reddit.com/r/fastled) for being a great source of help and inspiration.
