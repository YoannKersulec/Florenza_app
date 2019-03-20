package e.dcr000_9k5dsovvojml.florenza

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FieldToken {
    @SerializedName("access_token")
    @Expose
    val access_token : String? = null

    @SerializedName("refresh_token")
    @Expose
    val refresh_token : String? = null
}