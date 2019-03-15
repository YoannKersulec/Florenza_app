package e.dcr000_9k5dsovvojml.florenza

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login_page.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
    }

    fun goToAccueil(view: View) {
        val username = username_text.text
        val password = password_text.text
        username_text.setText("")
        password_text.setText("")
/*
        if (username.toString() == "admin" && password.toString() == "admin") {
*/
            val intent = Intent(this, AccueilActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("password", password)
            startActivity(intent)
/*
        }
        else {
            Toast.makeText(this.applicationContext, "Wrong password", Toast.LENGTH_SHORT).show()
        }
*/
    }
}