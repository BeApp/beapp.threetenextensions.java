# Introduction

This library provides some extras features for [ThreeTen](http://www.threeten.org/) project.

Some features are inspired from JodaTime to fill a gap in ThreeTen library.It may be a simplification and not cover the whole scope for all languages. But it's enough for our uses-cases.

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