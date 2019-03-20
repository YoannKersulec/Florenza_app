package e.dcr000_9k5dsovvojml.florenza

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponsePredict {
    @SerializedName("ok")
    @Expose
    val ok : Boolean = false

    @SerializedName("result")
    @Expose
    val result : Array<PredictFlower>? = null
}