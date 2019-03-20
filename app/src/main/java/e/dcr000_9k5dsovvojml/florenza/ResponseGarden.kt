package e.dcr000_9k5dsovvojml.florenza

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseGarden {
    @SerializedName("ok")
    @Expose
    val ok : Boolean = false

    @SerializedName("result")
    @Expose
    val result : Array<PosGarden>? = null
}

class PosGarden(
    val flower : Int,

    val pos : Int
)