package e.dcr000_9k5dsovvojml.florenza

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_accueil.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

class AccueilActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var fileUri: Uri? = null

    var username : String? = null
    var access_token : String? = null
    var refresh_token : String? = null

    private var service: APIService? = null

    object AppConstants{
        val TAKE_PHOTO_REQUEST: Int = 2
        val PICK_PHOTO_REQUEST: Int = 1
    }

    val offset = 60
    var border_left : Int = 0
    var border_right : Int = 0
    var border_top : Int = 0
    var border_bot : Int = 0

    var files : ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)
        files = ArrayList()
        setSupportActionBar(toolbar)
        service = ApiUtils.apiService
        val intent = getIntent()
        username = intent.getStringExtra("username")
        access_token = intent.getStringExtra("access_token")
        refresh_token = intent.getStringExtra("refresh_token")
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                askCameraPermission()
            }
            R.id.nav_gallery -> {
                pickPhotoFromGallery()
            }
            R.id.nav_slideshow -> {
                val intent = Intent(this@AccueilActivity, ProfilActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("access_token", access_token)
                intent.putExtra("refresh_token", refresh_token)
                val files_url = arrayListOf<String>()
                if (files != null) {
                    for (file in files!!) {
                        files_url.add(file)
                    }
                }
                intent.putStringArrayListExtra("files_url", files_url)
                startActivity(intent)
            }
            R.id.nav_manage -> {
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(pickImageIntent, AppConstants.PICK_PHOTO_REQUEST)
    }

    //launch the camera to take photo via intent
    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        fileUri = contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        Toast.makeText(this.applicationContext, fileUri.toString(), Toast.LENGTH_LONG).show()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, AppConstants.TAKE_PHOTO_REQUEST)
        }
    }

    //ask for permission to take photo
    fun askCameraPermission(){
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                    if(report.areAllPermissionsGranted()){
                        //once permissions are granted, launch the camera
                        launchCamera()
                    }else{
                        Toast.makeText(this@AccueilActivity, "All permissions need to be granted to take photo", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {/* ... */
                    //show alert dialog with permission options
                    AlertDialog.Builder(this@AccueilActivity)
                        .setTitle(
                            "Permissions Error!")
                        .setMessage(
                            "Please allow permissions to take photo with camera")
                        .setNegativeButton(
                            android.R.string.cancel,
                            { dialog, _ ->
                                dialog.dismiss()
                                token.cancelPermissionRequest()
                            })
                        .setPositiveButton(android.R.string.ok,
                            { dialog, _ ->
                                dialog.dismiss()
                                token.continuePermissionRequest()
                            })
                        .setOnDismissListener({
                            token.cancelPermissionRequest() })
                        .show()
                }

            }).check()

    }

    //override function that is called once the photo has been taken
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.TAKE_PHOTO_REQUEST) {
            //photo from camera
            pictureTaken.setImageURI(fileUri)
            files!!.add(fileUri.toString())
            drawRectangleOnMiddle(fileUri!!, pictureTaken, Color.WHITE)
        }
        else if(resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.PICK_PHOTO_REQUEST){
            //photo from gallery
            fileUri = data?.data
            pictureTaken.setImageURI(fileUri)
            files!!.add(fileUri.toString())
            drawRectangleOnMiddle(fileUri!!, pictureTaken, Color.WHITE)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    fun drawRectangleOnMiddle(uri: Uri, imageView: ImageView, color : Int) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.color = color
        paint.strokeWidth = 20.0f
        paint.style = Paint.Style.STROKE
        val middleX = bitmap.width / 2
        val middleY = bitmap.height / 2
        val left = middleX - 200
        val top = middleY - 200
        val right = middleX + 200
        val bot = middleY + 200
        border_bot = bot
        border_top = top
        border_right = right
        border_left = left
        Log.i("State", "Left : ${left} right : ${right} top : ${top} bot : ${bot}")
        Log.i("State", "Width : ${bitmap.width} Height : ${bitmap.height}")
        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bot.toFloat(), paint)
        imageView.setImageBitmap(mutableBitmap)
    }

    fun move_to_right(view: View) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 20.0f
        paint.style = Paint.Style.STROKE
        border_right += offset
        border_left += offset
        canvas.drawRect(border_left.toFloat(), border_top.toFloat(), border_right.toFloat(), border_bot.toFloat(), paint)
        pictureTaken.setImageBitmap(mutableBitmap)
    }

    fun move_to_left(view: View) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 20.0f
        paint.style = Paint.Style.STROKE
        border_right -= offset
        border_left -= offset
        canvas.drawRect(border_left.toFloat(), border_top.toFloat(), border_right.toFloat(), border_bot.toFloat(), paint)
        pictureTaken.setImageBitmap(mutableBitmap)
    }

    fun move_to_top(view: View) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 20.0f
        paint.style = Paint.Style.STROKE
        border_top -= offset
        border_bot -= offset
        canvas.drawRect(border_left.toFloat(), border_top.toFloat(), border_right.toFloat(), border_bot.toFloat(), paint)
        pictureTaken.setImageBitmap(mutableBitmap)
    }

    fun move_to_bot(view: View) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 20.0f
        paint.style = Paint.Style.STROKE
        border_top += offset
        border_bot += offset
        canvas.drawRect(border_left.toFloat(), border_top.toFloat(), border_right.toFloat(), border_bot.toFloat(), paint)
        pictureTaken.setImageBitmap(mutableBitmap)
    }

    private class Axis(x : Int, y : Int)

    fun sendPredict(view : View) {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, fileUri)
//        val bitmap = (pictureTaken.drawable as BitmapDrawable).bitmap
        if (bitmap != null) {
            val leftCorner = Axis(1, 1)
            val body = PredictBody(
                data = encodeTobase64(bitmap),
                height = bitmap.height,
                width = bitmap.width,
                n = 5,
                rect = RectBody(border_left, border_top, border_right, border_bot))
            service!!.predictFlower(body,  access_token!!).enqueue(object : Callback<ResponsePredict> {
                override fun onResponse(call: Call<ResponsePredict>, response: Response<ResponsePredict>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AccueilActivity, "Send successfull", Toast.LENGTH_SHORT).show()
                            Log.i("State", "post submitted to API." + response.body().toString())
                        val test = response.body()!!.result
                        switchToPredict(response.body()!!.result!!)
                    }
                }

                override fun onFailure(call: Call<ResponsePredict>, t: Throwable) {
                    Toast.makeText(this@AccueilActivity, "Error ; " + t.message, Toast.LENGTH_SHORT).show()
                    Log.e("state", "Unable to submit post to API.")
                }
            })
            Toast.makeText(this@AccueilActivity, "Loading...", Toast.LENGTH_LONG).show()
        }
    }

    fun switchToPredict(flowers : Array<PredictFlower>) {
        val intent = Intent(this@AccueilActivity, PredictActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra("access_token", access_token)
        intent.putExtra("refresh_token", refresh_token)
        var ids = arrayListOf<Int>()
        var names = arrayListOf<String>()
        for (flower in flowers) {
            ids.add(flower.id)
            names.add(flower.name!!)
        }
        intent.putIntegerArrayListExtra("ids", ids)
        intent.putStringArrayListExtra("names", names)
        startActivity(intent)
    }

    fun agrandirLePenis(laMethodAmericaine : View) {
        border_bot += 20
        border_top -= 20
        border_left -= 20
        border_right += 20
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 20.0f
        paint.style = Paint.Style.STROKE
        canvas.drawRect(border_left.toFloat(), border_top.toFloat(), border_right.toFloat(), border_bot.toFloat(), paint)
        pictureTaken.setImageBitmap(mutableBitmap)
    }

    fun diminuerLePenis(laMethodAmericaine: View) {
        border_bot -= 20
        border_top += 20
        border_left += 20
        border_right -= 20
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.color = Color.WHITE
        paint.strokeWidth = 20.0f
        paint.style = Paint.Style.STROKE
        canvas.drawRect(border_left.toFloat(), border_top.toFloat(), border_right.toFloat(), border_bot.toFloat(), paint)
        pictureTaken.setImageBitmap(mutableBitmap)
    }

    fun encodeTobase64(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        val imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)

        return imageEncoded
    }

}
