package de.cotech.dokka.hugo

import org.jetbrains.dokka.Formats.KotlinFormatDescriptorBase

class HugoFormatDescriptor : KotlinFormatDescriptorBase() {
    override val formatServiceClass = HugoFormatService::class
}
