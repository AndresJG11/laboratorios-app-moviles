import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalTime

//Function that connects to URL in url argument and returns server response
fun wGet(url:String): String {
    //Builds URL object
    val url = URL(url)

    //String to return response
    val response: String

    //Creates HTTP connection
    var conn= url.openConnection() as HttpURLConnection

    //Objects to manage response from server
    val inputStream : InputStream = conn.getInputStream()
    val inputStreamReader = InputStreamReader(inputStream)
    val bufferedReader = BufferedReader(inputStreamReader)

    //Reads response from server
    response=bufferedReader.readLine()

    //Returns response
    return response
}


fun getTemperatureByLatLong(lat: Double, long: Double, key: String):Double{
    var temperature: Double
    val url: String = "http://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$long&&appid=$key"

    // Requests
    val response = wGet(url)

    // Get temperature from json string
    temperature = response.split("temp\":")[1].split(",")[0].toDouble()

    // Kelvin to celsius
    temperature -= 273.15

    return temperature
}


fun getTemperatureByCity(city: String, key: String):Double{
    // http://api.openweathermap.org/data/2.5/weather?q=armenia,co&appid=e29ade20cdf7267cf26e5ba45927e641
    var temperature: Double = -1.0
    val url: String = "http://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key"

    // Requests
    val response = wGet(url)

    // Get temperature from json string
    temperature = response.split("temp\":")[1].split(",")[0].toDouble()

    // Kelvin to celsius
    temperature -= 273.15

    return temperature
}

fun main(){
    var key: String = "e29ade20cdf7267cf26e5ba45927e641"
    var temperature: Double

    // Armenia
    var city: String = "Armenia,co"
    var lat: Double = 4.53
    var long: Double = -75.68

    temperature = getTemperatureByLatLong(lat, long, key)
    println("Temperature: \t $temperature")

    var avgTemperature: Double = 0.0
    var histTemperature: Double = 0.0


    for(loop in 0..60){
        temperature = getTemperatureByCity(city, key)
        avgTemperature = (temperature+histTemperature).div(loop.toDouble()+1.0)
        histTemperature += temperature
        println("City: $city \t Temperature: ${temperature.format(2)} \t Average Temperature: $avgTemperature")
        Thread.sleep(10000)
    }
}
