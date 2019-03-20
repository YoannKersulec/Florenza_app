package e.dcr000_9k5dsovvojml.florenza

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView


class PickPhotoActivity : AppCompatActivity() {

    var username: String? = null
    var access_token: String? = null
    var refresh_token: String? = null

    var files_url: ArrayList<String>? = null
    var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pick_photo)
        val intent = getIntent()
        username = intent.getStringExtra("username")
        access_token = intent.getStringExtra("access_token")
        refresh_token = intent.getStringExtra("refresh_token")
        files_url = intent.getStringArrayListExtra("files_url")
        id = intent.getIntExtra("id", 0)
    }

    fun clickOnPhoto(view : View) {
        val name = (view as TextView).text.toString().toLowerCase()
        val flower_id = when(view.id) {
            R.id.textPick1 -> 1
            R.id.textPick2 -> 2
            R.id.textPick3 -> 3
            R.id.textPick4 -> 4
            R.id.textPick5 -> 5
            R.id.textPick6 -> 6
            R.id.textPick7 -> 7
            R.id.textPick8 -> 8
            R.id.textPick9 -> 9
            R.id.textPick10 -> 10
            R.id.textPick11 -> 11
            R.id.textPick12 -> 12
            R.id.textPick13 -> 13
            R.id.textPick14 -> 14
            R.id.textPick15 -> 15
            R.id.textPick16 -> 16
            R.id.textPick17 -> 17
            else -> 22
        }
        val data = Intent()
        data.putExtra("id", id)
        data.putExtra("flower_id", flower_id)
        data.putExtra("name", name)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    fun deleteFlower(view: View) {
        val data = Intent()
        data.putExtra("id", id)
        setResult(Activity.RESULT_CANCELED, data)
        finish()

    }
}
