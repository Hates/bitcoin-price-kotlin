import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.net.URL

fun main(args: Array<String>) {
    val currentPriceJSON = fetchCurrentPriceJSON()

    println("Bitcoin price as of ${fetchUKTime(currentPriceJSON)} is $${fetchUSD(currentPriceJSON)}")
}

fun fetchCurrentPriceJSON() : String {
    val url = URL("https://api.coindesk.com/v1/bpi/currentprice.json")
    val urlConnection = url.openConnection()
    val inputStream = urlConnection.getInputStream()

    val response = inputStream.bufferedReader().use { it.readText() }
    return response
}

fun fetchUKTime(currentPriceJSON : String) : String {
    val mapper = ObjectMapper().registerModule(KotlinModule())
    val jsonNode = mapper.readTree(currentPriceJSON)

    return jsonNode.get("time").get("updateduk").textValue()
}

fun fetchUSD(currentPriceJSON : String) : String {
    val mapper = ObjectMapper().registerModule(KotlinModule())
    val jsonNode = mapper.readTree(currentPriceJSON)

    return jsonNode.get("bpi").get("USD").get("rate").textValue()
}
