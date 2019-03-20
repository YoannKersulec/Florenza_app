package e.dcr000_9k5dsovvojml.florenza

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private var service: APIService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        service = ApiUtils.apiService
        executeCommand()
    }

    fun singUp(view: View) {
        val username = username_text.text.toString()
        val password = password_text.text.toString()
        val login = LoginBody()
        login.username = username
        login.password = password
        service!!.register(login).enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(call: Call<ResponseRegister>, response: Response<ResponseRegister>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Sign up successful", Toast.LENGTH_SHORT).show()
                    Log.i("State", "post submitted to API." + response.body().toString())
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error ; " + t.message, Toast.LENGTH_SHORT).show()
                Log.e("state", "Unable to submit post to API.")
            }
        })

    }

    fun goToAccueil(view: View) {
        val username = username_text.text
        val password = password_text.text
        username_text.setText("")
        password_text.setText("")
        checkLogin(username = username.toString(), password = password.toString())
    }

    fun checkLogin(username: String, password: String) {
        val login = LoginBody()
        login.username = username
        login.password = password
        service!!.login(login).enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {

                if (response.isSuccessful()) {
                    val body = response.body()
                    Toast.makeText(this@MainActivity, "Loged to $username", Toast.LENGTH_SHORT).show()
                    Log.i("State", "post submitted to API." + response.body()!!.result)
                    val intent = Intent(this@MainActivity, AccueilActivity::class.java)
                    intent.putExtra("username", username)
                    intent.putExtra("password", password)
                    intent.putExtra("access_token", body!!.result!!.access_token)
                    intent.putExtra("refresh_token", body!!.result!!.refresh_token)
                    startActivity(intent)
                }
                else {
                    val test = response.code()
                    Toast.makeText(this@MainActivity, response.errorBody().toString(), Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error ; " + t.message, Toast.LENGTH_SHORT).show()
                Log.e("state", "Unable to submit post to API.")
            }
        })
    }

    private fun executeCommand(): Boolean {
        println("executeCommand")
        val runtime = Runtime.getRuntime()
        try {
            val mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 10.41.165.212")
            val mExitValue = mIpAddrProcess.waitFor()
            println(" mExitValue $mExitValue")
            return if (mExitValue == 0) {
                true
            } else {
                false
            }
        } catch (ignore: InterruptedException) {
            ignore.printStackTrace()
            println(" Exception:$ignore")
        } catch (e: IOException) {
            e.printStackTrace()
            println(" Exception:$e")
        }

        return false
    }
}