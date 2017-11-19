import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.net.URL

const val COINDESK_API : String = "https://api.coindesk.com/v1/bpi/currentprice.json"

fun main(args: Array<String>) {
    val currentPriceNodeTree = currentPriceNodeTree(fetchCurrentPriceJSON())

    println("Bitcoin price as of ${fetchUKTime(currentPriceNodeTree)} is $${fetchUSD(currentPriceNodeTree)}")
}

fun fetchCurrentPriceJSON() : String {
    val url = URL(COINDESK_API)
    val urlConnection = url.openConnection()
    val inputStream = urlConnection.getInputStream()

    val response = inputStream.bufferedReader().use { it.readText() }
    return response
}

fun fetchUKTime(currentPriceNodeTree : JsonNode) : String {
    return currentPriceNodeTree.get("time").get("updateduk").textValue()
}

fun fetchUSD(currentPriceNodeTree : JsonNode) : String {
    return currentPriceNodeTree.get("bpi").get("USD").get("rate").textValue()
}

fun currentPriceNodeTree(currentPriceJSON : String) : JsonNode {
    val mapper = ObjectMapper().registerModule(KotlinModule())
    return mapper.readTree(currentPriceJSON)
}
