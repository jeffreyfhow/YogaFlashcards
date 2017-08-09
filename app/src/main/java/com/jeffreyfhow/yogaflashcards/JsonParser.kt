/**
 * Created by jeffreyhow on 7/12/17.
 */
import com.beust.klaxon.Parser
import com.beust.klaxon.JsonObject
import com.beust.klaxon.JsonArray

class JsonParser {
    fun parse(name: String) : Any? {
        val cls = Parser::class.java
        return cls.getResourceAsStream(name)?.let { inputStream ->
            return Parser().parse(inputStream)
        }
    }

    fun jsonFileToJsonObj(filepath: String) : JsonObject = parse(filepath) as JsonObject
    fun jsonFileToJsonArr(filepath: String) : JsonArray<JsonObject> = parse(filepath) as JsonArray<JsonObject>
}