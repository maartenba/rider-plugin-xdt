package be.hikage.xdt4j

import be.hikage.xdt4j.transform.XdtTransformFactory
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import java.util.*

class XdtTransformer {
    fun transform(inputDocument: Document, transformDocument: Document): Document {
        val xPath = DocumentHelper.createXPath("//*[@xdt:Transform]")
        xPath.setNamespaceURIs(Collections.singletonMap("xdt", XdtConstants.XDT_NAMESPACE))

        val workingCopy = inputDocument.clone() as Document

        val xdtNode = xPath.selectNodes(transformDocument)

        val transformToApply = xdtNode.map {
            XdtTransformFactory.createTransform(it as Element, workingCopy)
        }

        for (tr in transformToApply) {
            tr.apply()
        }

        return workingCopy
    }
}
