package de.cotech.dokka.hugo

import com.google.inject.Inject
import com.google.inject.name.Named
import org.jetbrains.dokka.*
import org.jetbrains.dokka.Utilities.impliedPlatformsName

// TODO: prepend _index.md instead of index.md

open class HugoOutputBuilder(to: StringBuilder,
                               location: Location,
                               generator: NodeLocationAwareGenerator,
                               languageService: LanguageService,
                               extension: String,
                               impliedPlatforms: List<String>)
    : MarkdownOutputBuilder(to, location, generator, languageService, extension, impliedPlatforms) {
    override fun appendNodes(nodes: Iterable<DocumentationNode>) {
        to.appendln("+++")
        appendFrontMatter(nodes, to)
        to.appendln("+++")
        to.appendln("")
        super.appendNodes(nodes)
    }

    protected open fun appendFrontMatter(nodes: Iterable<DocumentationNode>, to: StringBuilder) {
        to.appendln("""title = "${getPageTitle(nodes)}"""")
        to.appendln("""draft = false""")
        to.appendln("""toc = false""")
        to.appendln("""type = "javadocs"""")
        // TODO: only if it's a package and only show package name, not " - hw-security"
        to.appendln("""linktitle = "${getPageTitle(nodes)}"""")
        to.appendln("""[menu.docs]""")
        to.appendln("""  parent = "Packages"""")
        to.appendln("""  weight = 1""")
    }
    
    // Hugo markdown (blackfriday) requires table headers
    // https://github.com/Kotlin/dokka/blob/master/core/src/main/kotlin/Formats/GFMFormatService.kt
    override fun appendTable(vararg columns: String, body: () -> Unit) {
        to.appendln(columns.joinToString(" | ", "| ", " |"))
        to.appendln("|" + "---|".repeat(columns.size))
        body()
    }

    override fun appendUnorderedList(body: () -> Unit) {
        if (inTableCell) {
            wrapInTag("ul", body)
        } else {
            super.appendUnorderedList(body)
        }
    }

    override fun appendOrderedList(body: () -> Unit) {
        if (inTableCell) {
            wrapInTag("ol", body)
        } else {
            super.appendOrderedList(body)
        }
    }

    override fun appendListItem(body: () -> Unit) {
        if (inTableCell) {
            wrapInTag("li", body)
        } else {
            super.appendListItem(body)
        }
    }
}


open class HugoFormatService(
        generator: NodeLocationAwareGenerator,
        signatureGenerator: LanguageService,
        linkExtension: String,
        impliedPlatforms: List<String>
) : MarkdownFormatService(generator, signatureGenerator, linkExtension, impliedPlatforms) {

    @Inject constructor(
            generator: NodeLocationAwareGenerator,
            signatureGenerator: LanguageService,
            @Named(impliedPlatformsName) impliedPlatforms: List<String>
    ) : this(generator, signatureGenerator, "md", impliedPlatforms)

    override fun createOutputBuilder(to: StringBuilder, location: Location): FormattedOutputBuilder =
            HugoOutputBuilder(to, location, generator, languageService, extension, impliedPlatforms)

}
