package e.dcr000_9k5dsovvojml.florenza

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseNearGarden {
    @SerializedName("ok")
    @Expose
    val ok : Boolean = false

    @SerializedName("result")
    @Expose
    val result : NearGarden? = null
}