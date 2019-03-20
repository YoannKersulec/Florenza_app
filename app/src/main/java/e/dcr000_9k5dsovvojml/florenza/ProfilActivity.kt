package e.dcr000_9k5dsovvojml.florenza

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.predict_screen.*
import kotlinx.android.synthetic.main.profil.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilActivity : AppCompatActivity() {

    private var service: APIService? = null
    var username : String? = null
    var access_token : String? = null
    var refresh_token : String? = null
    var files_url : MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profil)
        service = ApiUtils.apiService
        val intent = getIntent()
        username = intent.getStringExtra("username")
        access_token = intent.getStringExtra("access_token")
        refresh_token = intent.getStringExtra("refresh_token")
        files_url = intent.getStringArrayListExtra("files_url")
        val test = files_url
        updateGarden()
    }

    fun selectImage(view: View) {
        val id_case = when(view.id) {
            R.id.imageProfil1 -> 0
            R.id.imageProfil2 -> 1
            R.id.imageProfil3 -> 2
            R.id.imageProfil4 -> 3
            R.id.imageProfil5 -> 4
            R.id.imageProfil6 -> 5
            R.id.imageProfil7 -> 6
            R.id.imageProfil8 -> 7
            R.id.imageProfil9 -> 8
            else -> 22
        }
        Toast.makeText(this@ProfilActivity, "Id : $id_case", Toast.LENGTH_LONG).show()
        val intent = Intent(this@ProfilActivity, PickPhotoActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("access_token", access_token)
        intent.putExtra("refresh_token", refresh_token)
        val urls = arrayListOf<String>()
        if (files_url != null) {
            for (file in files_url!!) {
                urls.add(file)
            }
        }
        intent.putStringArrayListExtra("files_url", urls)
        intent.putExtra("id", id_case)
        startActivityForResult(intent, 10)
    }

    fun updateGarden() {
        imageProfil1.setImageResource(0)
        imageProfil2.setImageResource(0)
        imageProfil3.setImageResource(0)
        imageProfil4.setImageResource(0)
        imageProfil5.setImageResource(0)
        imageProfil6.setImageResource(0)
        imageProfil7.setImageResource(0)
        imageProfil8.setImageResource(0)
        imageProfil9.setImageResource(0)
        val newService = ApiUtils.getSpecial(access_token!!)
        newService.getGarden().enqueue(object : Callback<ResponseGarden> {
            override fun onResponse(call: Call<ResponseGarden>, response: Response<ResponseGarden>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfilActivity, "Send successfull", Toast.LENGTH_SHORT).show()
                    Log.i("State", "post submitted to API." + response.body().toString())
                    val test = response.body()!!.result
                    for (flower in response.body()!!.result!!) {
                        updatePicture(flower.flower, flower.pos)
                    }
//                    switchToPredict(response.body()!!.result!!)
                }
            }

            override fun onFailure(call: Call<ResponseGarden>, t: Throwable) {
                Toast.makeText(this@ProfilActivity, "Error ; " + t.message, Toast.LENGTH_SHORT).show()
                Log.e("state", "Unable to submit post to API.")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                val id = data!!.getIntExtra("id", 0)
                val name = data.getStringExtra("name")
                val flower_id = data.getIntExtra("flower_id", 0)
                addFlower(flower_id, id)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                val id = data!!.getIntExtra("id", 0)
                delete(id)
            }
        }
    }

    fun delete(id : Int) {
        val newService = ApiUtils.getSpecial(access_token!!)
        newService.deleteFromGarden(DeleteBody(id)).enqueue(object : Callback<ResponseGarden> {
            override fun onResponse(call: Call<ResponseGarden>, response: Response<ResponseGarden>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfilActivity, "Delete successfull", Toast.LENGTH_SHORT).show()
                    Log.i("State", "post submitted to API." + response.body().toString())
                    updateGarden()
                }
            }

            override fun onFailure(call: Call<ResponseGarden>, t: Throwable) {
                Toast.makeText(this@ProfilActivity, "Error ; " + t.message, Toast.LENGTH_SHORT).show()
                Log.e("state", "Unable to submit post to API.")
            }
        })
    }

    fun addFlower(id : Int, pos : Int) {
        val newService = ApiUtils.getSpecial(access_token!!)
        newService.addToGarden(FlowerBody(flower = id, pos = pos)).enqueue(object : Callback<ResponseGarden> {
            override fun onResponse(call: Call<ResponseGarden>, response: Response<ResponseGarden>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfilActivity, "Send successfull", Toast.LENGTH_SHORT).show()
                    Log.i("State", "post submitted to API." + response.body().toString())
                    val test = response.body()!!.result
                    updateGarden()
//                    switchToPredict(response.body()!!.result!!)
                }
            }

            override fun onFailure(call: Call<ResponseGarden>, t: Throwable) {
                Toast.makeText(this@ProfilActivity, "Error ; " + t.message, Toast.LENGTH_SHORT).show()
                Log.e("state", "Unable to submit post to API.")
            }
        })
    }

    fun updatePicture(id : Int, pos : Int) {
        val newService = ApiUtils.getSpecial(access_token!!)
        newService.getInfoFlower(token = access_token!!, id = id).enqueue(object : Callback<ResponseGetFlower> {
            override fun onResponse(call: Call<ResponseGetFlower>, response: Response<ResponseGetFlower>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfilActivity, "Send successfull", Toast.LENGTH_SHORT).show()
                    Log.i("State", "post submitted to API." + response.body().toString())
                    val test = response.body()!!.result
                    val oui = pos
                    val image = when(pos) {
                        0 -> imageProfil1
                        1 -> imageProfil2
                        2 -> imageProfil3
                        3 -> imageProfil4
                        4 -> imageProfil5
                        5 -> imageProfil6
                        6 -> imageProfil7
                        7 -> imageProfil8
                        8 -> imageProfil9
                        else -> imageProfil1
                    }
                    if (response.body()!!.result.images.isNotEmpty()) {
                        val newUrl = ApiUtils.BASE_URL + "/flowers" + response.body()!!.result.images[0]
                        Picasso.with(this@ProfilActivity).load(newUrl).into(image)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseGetFlower>, t: Throwable) {
                Toast.makeText(this@ProfilActivity, "Error ; " + t.message, Toast.LENGTH_SHORT).show()
                Log.e("state", "Unable to submit post to API.")
            }
        })

    }
}
