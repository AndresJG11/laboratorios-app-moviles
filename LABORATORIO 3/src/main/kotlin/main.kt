/*

You have to create RESTClient class which has to behave in a such way it can be used as described in main.

*/

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import kotlin.random.Random
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

suspend fun main() {

    //Creates REST client object, argument is API URL 
    val restClient = RESTClient("http://localhost:8080")

    //User API key
    val auth= "5e5c546164b24c2c0bc47fbe"

    //Registers new user
    restClient.httpPostAsync("/register/user","user=miguel&password=123")
    println("You registered a new user ${restClient.wait()}")

    //Gets list of devices
    restClient.httpGetAsync("/devices?auth=$auth")

    //Parses JSON array
    var response = restClient.wait()

    /******        By using Klaxon, parse JSON object to obtain devices array ************/
    /******	   this has to be coded by you 				      ************/

    val parser: Parser = Parser.default()
    val stringBuilder: StringBuilder = StringBuilder(response)
    val devices=parser.parse(stringBuilder) as JsonObject
    val devices1 = devices.get("devices") as JsonArray<String>
    val device=devices1[0]

    println("You have registered ${devices1.size} devices")

    //Registers new device
    restClient.httpPostAsync("/register/device?auth=$auth","")
    println("You registered a new device ${restClient.wait()}")


    //Writes device value
    restClient.httpPutAsync("/device/${device}?auth=$auth", "value=${Random.nextDouble()}")
    println(restClient.wait())

    //Reads device value
    restClient.httpGetAsync("/device/${device}/value?auth=$auth")
    println("Value of device $device is ${restClient.wait()}")
}

class RESTClient(private var s: String) {

    lateinit var defer: Deferred<String>

    fun httpGetAsync(url: String) {

        //Returns response
         defer= GlobalScope.async {

            //Co-routine computes factorial
            get(s+url)
        }
    }

    fun httpPutAsync(url: String, data: String) {
        //Returns response
        defer= GlobalScope.async {

            //Co-routine computes factorial
            put(s+url, data)
        }
    }

    fun httpPostAsync(url: String, data: String) {
        defer = GlobalScope.async {

            //Co-routine computes factorial
            post(s+url, data)
        }
    }

    fun get(url: String): String{
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

        return response
    }

    fun put(url: String, data: String): String{
        //Builds URL object
        val url = URL(url)

        //String to return response
        val response: String
        //Creates HTTP connection
        var conn = url.openConnection() as HttpURLConnection


        //Parameters of connection
        conn.readTimeout = 10000
        conn.connectTimeout = 15000
        conn.requestMethod = "PUT"
        conn.doInput = true
        conn.doOutput = true
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded")

        //Objects to manage data to server
        val outputStream: OutputStream = conn.getOutputStream()
        val outputStreamWriter = OutputStreamWriter(outputStream)
        val bufferedWriter = BufferedWriter(outputStreamWriter)

        //Writes POST data to server
        bufferedWriter.write(data)
        bufferedWriter.flush()

        //Objects to manage response from server
        val inputStream: InputStream = conn.getInputStream()
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)


        //Returns server response
        return bufferedReader.readLine()
    }

    fun post(url: String, data: String): String{
        //Builds URL object
        val url = URL(url)

        //String to return response
        val response: String
        //Creates HTTP connection
        var conn = url.openConnection() as HttpURLConnection

        //Parameters of connection
        conn.readTimeout = 10000
        conn.connectTimeout = 15000
        conn.requestMethod = "POST"
        conn.doInput = true
        conn.doOutput = true

        //Objects to manage data to server
        val outputStream: OutputStream = conn.getOutputStream()
        val outputStreamWriter = OutputStreamWriter(outputStream)
        val bufferedWriter = BufferedWriter(outputStreamWriter)

        //Writes POST data to server
        bufferedWriter.write(data)
        bufferedWriter.flush()

        //Objects to manage response from server
        val inputStream: InputStream = conn.getInputStream()
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)


        //Returns server response
        return bufferedReader.readLine()
    }

    suspend fun wait(): String {

       return defer.await()
    }

}
