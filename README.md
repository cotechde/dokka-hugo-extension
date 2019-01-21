# Dokka Hugo Extension

This library is a [Dokka](https://github.com/Kotlin/dokka) extension.
Dokka is a documentation engine for Kotlin, performing the same function as Javadoc for Java.
Mixed-language Java/Kotlin projects are fully supported.
Dokka understands standard Javadoc comments in Java files and KDoc comments in Kotlin files.
The same holds true for this Dokka extension.

## Build

This extension is build as a fat-jar, i.e., it includes all dependenices in a single jar file using the [Gradle Shadow Plugin](https://imperceptiblethoughts.com/shadow/).

``./gradlew shadowJar``

## Requires

This extension is thought as an example how to build a dokka extension.
It is not working out of the box.

* Currently uses the Hugo academic theme
* Requires special docs template called "javadocs"
* Requires shortcode to insert markdown in html. Put `md.html` in your "shortcodes" folder with the content:
```
{{ .Inner }}
```

## Usage with Gradle

Put into your projects libs folder and configure dokka:

```groovy
dokka {
    outputFormat = "hugo"
    dokkaFatJar = files('libs/dokka-hugo-fatjar-0.9.17.jar')
}
```

## Credits

* Structure borrowed from https://github.com/ScaCap/spring-auto-restdocs/tree/master/spring-auto-restdocs-dokka-json
* https://medium.com/@flbenz/how-does-kotlins-documentation-engine-dokka-work-and-can-it-be-extended-5e83dc663ef7

## Notes

* Use same version of dependencies as in dokka
* gradle.properties from https://github.com/Kotlin/dokka/blob/master/gradle.properties
