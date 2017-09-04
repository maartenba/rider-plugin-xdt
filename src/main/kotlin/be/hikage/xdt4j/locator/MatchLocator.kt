package be.hikage.xdt4j.locator

import com.google.common.base.Joiner
import org.dom4j.Element
import java.util.*

class MatchLocator(parameter: String?) : Locator(parameter) {
    override fun generateXPath(target: Element): String {
        val resultXpath = StringBuilder(target.path)

        resultXpath.append(generateCondition(target))

        return resultXpath.toString()
    }

    override fun generateCondition(target: Element): String {
        val xPathCondition = StringBuilder()
        val conditionsList = ArrayList<String>()

        if (!parameter.isNullOrEmpty()) {
            // Create Query in form @attribute = "value"
            xPathCondition.append("[")
            val tokens = parameter!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (attributeName in tokens) {
                val attr = target.attribute(attributeName)
                val builder = StringBuilder()
                builder.append("@").append(attr.name).append("=").append("\"").append(attr.value).append("\"")
                conditionsList.add(builder.toString())

            }
            Joiner.on(" and ").appendTo(xPathCondition, conditionsList)
            xPathCondition.append("]")
        }

        return xPathCondition.toString()
    }


}
