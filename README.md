# Introduction

This library provides some extras features for [ThreeTen](http://www.threeten.org/) project.

Some of them are inspired from [JodaTime](http://www.joda.org/joda-time/). Beware that it's a simplified version and should not cover the whole scope for all languages, but it's enough for main uses-cases.

# Installation

Add Beapp's repository in your project's repositories list, then add the dependency.

```groovy
repositories {
    jcenter()
    // ...
    maven { url 'https://repository.beapp.fr/libs-release-local' }
}

dependencies {
    compile 'fr.beapp.threeten:threeten-extensions:<version>'
}
```