package de.cotech.dokka.hugo

import com.google.inject.Inject
import com.google.inject.name.Named
import org.jetbrains.dokka.*
import org.jetbrains.dokka.Utilities.impliedPlatformsName

// TODO: use _index.md instead of index.md

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
        to.appendln("title = \"${getPageLinkTitle(nodes)}\"")
        to.appendln("draft = false")
        to.appendln("toc = false")
        to.appendln("type = \"apidocs\"")
        
        // Add menu item for all packages
        if (isPackage(nodes)) {
            to.appendln("linktitle = \"${getPageLinkTitle(nodes)}\"")
            to.appendln("[menu.docs]")
            to.appendln("  parent = \"apidocs\"")
            to.appendln("  weight = 1")
        }
    }

    // Use HTML tables to have multiline content in cells. This is not possible with Markdown tables
    override fun appendTable(vararg columns: String, body: () -> Unit) {
        to.appendln("<table>")

        to.appendln("<thead>")
        to.appendln("<tr>")
        columns.forEach {
            to.appendln("<th>")
            to.appendln("$it")
            to.appendln("</th>")
        }
        to.appendln("</tr>")
        to.appendln("</thead>")

        body()

        to.appendln("</table>")
    }

    override fun appendTableBody(body: () -> Unit) {
        to.appendln("<tbody>")
        body()
        to.appendln("</tbody>")
    }

    override fun appendTableRow(body: () -> Unit) {
        to.appendln("<tr>")
        body()
        to.appendln("</tr>")
    }

    override fun appendTableCell(body: () -> Unit) {
        to.appendln("<td>")
        to.appendln("{{% md %}}")
        body()
        to.appendln("{{% /md %}}")
        to.appendln("</td>")
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
    
    private fun isPackage(nodes: Iterable<DocumentationNode>): Boolean {
        val node = nodes.singleOrNull()
        
        if (node?.kind == NodeKind.Package) {
            return true
        }      
        return false
    }
    
    fun getPageLinkTitle(nodes: Iterable<DocumentationNode>): String? {
        val breakdownByLocation = nodes.groupBy { node -> formatPageLinkTitle(node) }
        return breakdownByLocation.keys.singleOrNull()
    }

    fun formatPageLinkTitle(node: DocumentationNode): String {
        val path = node.path
        val moduleName = path.first().name
        if (path.size == 1) {
            return moduleName
        }

        val qName = qualifiedNameForPageTitle(node)
        return qName
    }
    
    // from HtmlFormatService.kt
    private fun qualifiedNameForPageTitle(node: DocumentationNode): String {
        if (node.kind == NodeKind.Package) {
            var packageName = node.qualifiedName()
            if (packageName.isEmpty()) {
                packageName = "root package"
            }
            return packageName
        }

        val path = node.path
        var pathFromToplevelMember = path.dropWhile { it.kind !in NodeKind.classLike }
        if (pathFromToplevelMember.isEmpty()) {
            pathFromToplevelMember = path.dropWhile { it.kind != NodeKind.Property && it.kind != NodeKind.Function }
        }
        if (pathFromToplevelMember.isNotEmpty()) {
            return pathFromToplevelMember.map { it.name }.filter { it.length > 0 }.joinToString(".")
        }
        return node.qualifiedName()
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
