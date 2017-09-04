package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtException
import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Document
import org.dom4j.Element

import java.util.regex.Pattern

object XdtTransformFactory {
    private val LOG = Logger.getInstance(XdtTransformFactory::class.java)

    private val ATTRIBUTE_VALIDATOR_PATTERN = Pattern.compile("(\\w*)(\\((.*)\\))?")

    fun createTransform(transformElement: Element, workingDocument: Document): Transform {
        val transformAttributeValue = transformElement.attributeValue("Transform")
        val matcher = ATTRIBUTE_VALIDATOR_PATTERN.matcher(transformAttributeValue)

        if (!matcher.matches())
            throw XdtException("The Transform Attributes value is invalid" + transformAttributeValue)

        if (LOG.isDebugEnabled) {
            LOG.debug("Current Element XPath : {}", transformElement.path)
            LOG.debug("Current Element Action :  {}", matcher.group(1))
            LOG.debug("Current Element Parameter :  {}", matcher.group(3))
        }

        val transformName = matcher.group(1)
        val className = "be.hikage.xdt4j.transform.${transformName}Transform"

        return try {
            val transformClass = Class.forName(className)
            val constructor = transformClass.getConstructor(Document::class.java, Element::class.java, String::class.java)

            constructor.newInstance(workingDocument, transformElement, matcher.group(3)) as Transform
        } catch (e: Exception) {
            throw XdtException("Could not create an instance of {$className}." +
                    "The transformation '$transformName' is not supported or invalid arguments were passed.", e)
        }
    }
}
