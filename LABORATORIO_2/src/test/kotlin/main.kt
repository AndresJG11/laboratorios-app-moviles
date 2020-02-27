import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


fun httpPostAsync(url: String, data: String): Deferred<String>{
    return GlobalScope.async{
        httpPost(url, data)
    }
}

fun httpPut(url: String, data: String): String {

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

    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

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


suspend fun main(){
    /*
    var url: String = "http://localhost:8080/device/5e581fa84a270436298e8dc8?auth=5e581e184a270436298e8dc3"
    var data: String = "value=0.001" // 5e581e184a270436298e8dc3 -- Dispositivo: 5e581fa84a270436298e8dc8

    var putResponse = httpPut(url, data)

    println(putResponse)*/

    println(wGet("http://localhost:8080/device/5e581fa84a270436298e8dc8/value?auth=5e581e184a270436298e8dc3"))
}
