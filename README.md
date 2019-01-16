# Dokka Hugo Extension

This library is a [Dokka](https://github.com/Kotlin/dokka) extension.
Dokka is a documentation engine for Kotlin, performing the same function as Javadoc for Java.
Mixed-language Java/Kotlin projects are fully supported.
Dokka understands standard Javadoc comments in Java files and KDoc comments in Kotlin files.
The same holds true for this Dokka extension.

## Build

This extension is build as a fat-jar, i.e., it includes all dependenices in a single jar file using the [Gradle Shadow Plugin](https://imperceptiblethoughts.com/shadow/).

``./gradlew shadowJar``

## Usage with Gradle

Put into your projects libs folder and configure dokka:

```groovy
dokka {
    outputFormat = "hugo"
    dokkaFatJar = files('libs/dokka-hugo-all.jar')
}
```

## Credits

* Structure borrowed from https://github.com/ScaCap/spring-auto-restdocs/tree/master/spring-auto-restdocs-dokka-json
* https://medium.com/@flbenz/how-does-kotlins-documentation-engine-dokka-work-and-can-it-be-extended-5e83dc663ef7
