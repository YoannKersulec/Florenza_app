package e.dcr000_9k5dsovvojml.florenza

import com.google.gson.annotations.SerializedName

class PossibleInfo (
    @SerializedName("Plant habit")
    val plant_habit : Array<String>,

    @SerializedName("Life cycle")
    val life_cycle : Array<String>,

    @SerializedName("Sun Requirements")
    val sun_requirements : Array<String>,

    @SerializedName("Flowers")
    val flowers : Array<String>,

    @SerializedName("Flower Color")
    val flower_color : Array<String>,

    @SerializedName("Flower Time")
    val flower_time : Array<String>,

    @SerializedName("Suitable Locations")
    val suitable_locations : Array<String>,

    @SerializedName("Uses")
    val uses : Array<String>,

    @SerializedName("Propagation Other methods")
    val propagation : Array<String>,

    @SerializedName("Containers")
    val containers : Array<String>,

    @SerializedName("Plant Height")
    val plant_height : Array<String>,

    @SerializedName("Toxicity")
    val toxicity : Array<String>,

    @SerializedName("Water Preferences")
    val water_preferences : Array<String>,

    @SerializedName("Fruiting Time")
    val fruiting_time : Array<String>,

    @SerializedName("Bloom time")
    val bloom_time : Array<String>
)
