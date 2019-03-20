package e.dcr000_9k5dsovvojml.florenza

object ApiUtils {

    val BASE_URL = "http://10.41.170.216:8080"

    val apiService: APIService
        get() = RetrofitClient.getClient(BASE_URL).create<APIService>(APIService::class.java!!)

    fun getSpecial(token : String) : APIService {
        return RetrofitClient.createSpecialClient(BASE_URL, token).create<APIService>(APIService::class.java)
    }
}