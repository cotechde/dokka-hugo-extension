package de.cotech.dokka.hugo

import org.jetbrains.dokka.*
import org.jetbrains.dokka.Utilities.bind
import kotlin.reflect.KClass
import org.jetbrains.dokka.Formats.*

// based on https://github.com/Kotlin/dokka/blob/master/core/src/main/kotlin/Formats/StandardFormats.kt
class HugoFormatDescriptor
    : FileGeneratorBasedFormatDescriptor(),
        DefaultAnalysisComponent,
        DefaultAnalysisComponentServices by KotlinAsKotlin {
    override val generatorServiceClass = FileGenerator::class
    override val outlineServiceClass: KClass<out OutlineFormatService>? = null
    override val packageListServiceClass = DefaultPackageListService::class

    override val formatServiceClass = HugoFormatService::class
}
