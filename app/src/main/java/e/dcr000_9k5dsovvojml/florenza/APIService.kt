package e.dcr000_9k5dsovvojml.florenza

import retrofit2.Call
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query


interface APIService {
    @PUT("/user")
    fun register(
        @Body body: LoginBody): Call<ResponseRegister>

    @POST("/login")
    fun login(
        @Body body : LoginBody): Call<ResponseLogin>

    @GET("/user/me/profile")
    fun getProfil(
        @Header("Authorization: Bearer ") token : String): Call<ResponseProfil>

    @Headers("Accept: application/json",
        "Authorization: Bearer "
        )
    @POST("/predict/flower")
    fun predictFlower(
        @Body body : PredictBody,
        @Header("Authorization") token : String
    ) : Call<ResponsePredict>

    @Headers("Accept: application/json",
        "Authorization: Bearer "
    )
    @GET("/information/flower")
    fun getInfoFlower(
        @Header("Authorization") token: String,
        @Query("id") id : Int
    ) : Call<ResponseGetFlower>

    @GET("/user/me/garden")
    fun getGarden() : Call<ResponseGarden>

    @PUT("/user/me/garden")
    fun addToGarden(@Body body : FlowerBody): Call<ResponseGarden>

    @HTTP(method = "DELETE", path = "/user/me/garden", hasBody = true)
    fun deleteFromGarden(@Body body : DeleteBody): Call<ResponseGarden>
}