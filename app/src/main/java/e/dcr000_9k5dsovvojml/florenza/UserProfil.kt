package e.dcr000_9k5dsovvojml.florenza

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserProfil {
    @SerializedName("username")
    @Expose
    val username : String? = null

    @SerializedName("latitude")
    @Expose
    val latitude : Float = 0.0f

    @SerializedName("longitude")
    @Expose
    val longitude : Float = 0.0f
}