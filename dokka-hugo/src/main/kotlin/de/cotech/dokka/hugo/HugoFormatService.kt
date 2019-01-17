package de.cotech.dokka.hugo

import com.google.inject.Inject
import com.google.inject.name.Named
import org.jetbrains.dokka.*
import org.jetbrains.dokka.Utilities.impliedPlatformsName

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
        to.appendln("""toc = true""")
        to.appendln("""type = "javadocs"""")
        to.appendln("""linktitle = "${getPageTitle(nodes)}"""")
        to.appendln("""[menu.docs]""")
        to.appendln("""  parent = "Dokka"""")
        to.appendln("""  weight = 1""")
    }
    
    override fun appendTable(vararg columns: String, body: () -> Unit) {
        to.appendln(columns.joinToString(" | ", "| ", " |"))
        to.appendln("|" + "---|".repeat(columns.size))
        body()
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
    ) : this(generator, signatureGenerator, "html", impliedPlatforms)

    override fun createOutputBuilder(to: StringBuilder, location: Location): FormattedOutputBuilder =
            HugoOutputBuilder(to, location, generator, languageService, extension, impliedPlatforms)

}
