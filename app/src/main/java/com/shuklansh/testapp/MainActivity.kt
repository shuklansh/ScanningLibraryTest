package com.shuklansh.testapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var galleryButton : Button
    lateinit var cameraButton : Button
    lateinit var view : View

    val CAMERA_RQ = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        galleryButton = findViewById(R.id.gallery)
        cameraButton = findViewById(R.id.camera)
        galleryButton.setOnClickListener{
            openGallery()
        }

        cameraButton.setOnClickListener{
            checkForPermissionCamera(android.Manifest.permission.CAMERA, name = "camera", CAMERA_RQ)
            //openCamera()
        }


    }


    fun openCamera(){
        val REQUEST_CODE = 99
        val preference = ScanConstants.OPEN_CAMERA
        val intent = Intent(this, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        startActivityForResult(intent, REQUEST_CODE)
    }

    fun openGallery(){
        val REQUEST_CODE = 99
        val preference = ScanConstants.OPEN_MEDIA
        val intent = Intent(this, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 99 && resultCode == RESULT_OK) {
            val uri = data?.extras?.getParcelable<Uri>(ScanConstants.SCANNED_RESULT)
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    //contentResolver.delete(uri!!, null, null)
                //scannedImageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        else if (requestCode == 100 && resultCode == RESULT_OK) {
            val uri = data?.extras?.getParcelable<Uri>(ScanConstants.SCANNED_RESULT)
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                //contentResolver.delete(uri!!, null, null)
                //scannedImageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    private fun checkForPermissionCamera(permission : String, name : String, requestCode: Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
               ContextCompat.checkSelfPermission(applicationContext,permission) == PackageManager.PERMISSION_GRANTED->{
                   openCamera()
               }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission,name,requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name : String){
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED ){
                Toast.makeText(applicationContext,"$name permission refused", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext,"$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }

        when(requestCode){
           CAMERA_RQ -> innerCheck("camera")
        }
    }

    private fun showDialog(permission : String, name : String, requestCode: Int){
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("grant permission for $name")
            setTitle("Camera Permission")
            setPositiveButton("OK"){dialog,which->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission),requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }








}