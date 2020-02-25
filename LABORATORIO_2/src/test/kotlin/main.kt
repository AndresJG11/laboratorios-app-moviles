import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


fun httpPostAsync(url: String, data: String): Deferred<String>{
    return GlobalScope.async{
        httpPost(url, data)
    }
}

suspend fun main(){
    var url: String = "https://postman-echo.com/post"
    var data: String = "/hi/there?hand=wave"
    var response:String = httpPost(url, data)
    println(response)

    println("--------------------------------")
    data = "nombre=andres&apellido=jim"
    var asincResponse = httpPostAsync(url, data)
    println(asincResponse.await())
}
