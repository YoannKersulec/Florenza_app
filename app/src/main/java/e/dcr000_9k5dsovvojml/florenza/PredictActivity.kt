package e.dcr000_9k5dsovvojml.florenza

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.predict_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.view.View
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.net.URL


class PredictActivity : AppCompatActivity() {

    var newUrl = ""

    private var service: APIService? = null

    var username : String? = null
    var access_token : String? = null
    var refresh_token : String? = null

    var ids : ArrayList<Int>? = null
    var names : MutableList<String>? = null
    var images : MutableList<String>? = null

    var index : Int = 0
    var indexImages : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.predict_screen)
        service = ApiUtils.apiService
        newUrl = ApiUtils.BASE_URL
        val intent = getIntent()
        username = intent.getStringExtra("username")
        access_token = intent.getStringExtra("access_token")
        refresh_token = intent.getStringExtra("refresh_token")
        ids = intent.getIntegerArrayListExtra("ids")
        names = intent.getStringArrayListExtra("names")
        text_predict.text = names!![index].toUpperCase()
        text_predict.textSize = 15.0f
        text_info.text = "Informations :\n"
        text_info2.text = "Practical informations"
        getInfoFlower(ids!![index])
    }

    fun changeFlower(view: View) {
        if (index + 1 >= ids!!.size) {
            index = 0
        }
        else
            index ++
        indexImages = 0
        image_predict.setImageDrawable(null)
        getInfoFlower(ids!![index])
        text_predict.text = names!![index].toUpperCase()
        text_info.text = "Informations :\n"
        text_info2.text = "Practical informations"
        images!!.clear()
    }

    fun getInfoFlower(id : Int) {
        service!!.getInfoFlower(access_token!!, id).enqueue(object : Callback<ResponseGetFlower> {
            override fun onResponse(call: Call<ResponseGetFlower>, response: Response<ResponseGetFlower>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PredictActivity, "Get successful", Toast.LENGTH_SHORT).show()
                    Log.i("State", "post submitted to API." + response.body().toString())
                    val oui = response.body()
                    text_info.text ="Informations :\n" + createInfo(response.body()!!.result.infos)
                    text_info2.text = "Practical informations :\n" + createInfoUse(response.body()!!.result.infos)
                    if (response.body()!!.result.images.isNotEmpty()) {
                        val test = newUrl + "/flowers"+ response.body()!!.result.images[0]
                        getImagesUrl(response.body()!!.result.images)
                        if (images!!.size > 0) {
                            val realUrl = newUrl + "/flowers" + images!![indexImages]
                            Picasso.with(this@PredictActivity).load(realUrl).into(image_predict)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseGetFlower>, t: Throwable) {
                Toast.makeText(this@PredictActivity, "Error ; " + t.message, Toast.LENGTH_SHORT).show()
                Log.e("state", "Unable to submit post to API.")
            }
        })
    }

    fun switchImage(view: View) {
        if (images != null) {
            if (indexImages + 1 < images!!.size) {
                indexImages++
                val realUrl = newUrl + "/flowers" + images!![indexImages]
                Picasso.with(this@PredictActivity).load(realUrl).into(image_predict)
            }
            else
                indexImages = 0
        }
    }

    fun getImagesUrl(urls : Array<String>) {
        if (images != null) {
            images!!.clear()
        }
        images = ArrayList()
        for (url in urls) {
            images!!.add(url)
        }
    }

    fun createInfo(infos : PossibleInfo) : String {
        var return_infos = ""
        if (!infos.flowers.isNullOrEmpty()) {
            return_infos += "Flowers :\n"
            for (flower in infos.flowers)
                return_infos += "- $flower\n"
        }
        if (!infos.flower_color.isNullOrEmpty()) {
            return_infos += "Colors :\n"
            for (color in infos.flower_color)
                return_infos += "- $color\n"
        }
        if (!infos.plant_height.isNullOrEmpty()) {
            return_infos += "Height :\n"
            for (height in infos.plant_height)
                return_infos += "- $height\n"
        }
        if (!infos.flower_time.isNullOrEmpty()) {
            return_infos += "Flower time :\n"
            for (time in infos.flower_time)
                return_infos += "- $time\n"
        }
        if (!infos.fruiting_time.isNullOrEmpty()) {
            return_infos += "Fruiting time :\n"
            for (fruiting in infos.fruiting_time)
                return_infos += "- $fruiting\n"
        }
        if (!infos.bloom_time.isNullOrEmpty()) {
            return_infos += "Bloom time :\n"
            for (bloom in infos.bloom_time)
                return_infos += "-$bloom\n"
        }
        return return_infos
    }

    fun createInfoUse(infos : PossibleInfo) : String {
        var return_infos = ""
        if (!infos.life_cycle.isNullOrEmpty()) {
            return_infos += "Life cycle :\n"
            for (cycle in infos.life_cycle)
                return_infos += "- $cycle\n"
        }
        if (!infos.sun_requirements.isNullOrEmpty()) {
            return_infos += "Sun requirements :\n"
            for (sun in infos.sun_requirements)
                return_infos += "- $sun\n"
        }
        if (!infos.suitable_locations.isNullOrEmpty()) {
            return_infos += "Suitable locations :\n"
            for (location in infos.suitable_locations)
                return_infos += "- $location\n"
        }
        if (!infos.containers.isNullOrEmpty()) {
            return_infos += "Containers :\n"
            for (container in infos.containers)
                return_infos += "- $container\n"
        }
        if (!infos.water_preferences.isNullOrEmpty()) {
            return_infos += "Water preferences :\n"
            for (water in infos.water_preferences)
                return_infos += "- $water\n"
        }
        if (!infos.toxicity.isNullOrEmpty()) {
            return_infos += "Toxicity :\n"
            for (toxicity in infos.toxicity)
                return_infos += "- $toxicity"
        }
        return return_infos
    }
}