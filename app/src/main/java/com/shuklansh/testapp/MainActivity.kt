package com.shuklansh.testapp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openCamera(view: View){
        val REQUEST_CODE = 99
        val preference = ScanConstants.OPEN_CAMERA
        val intent = Intent(this, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        startActivityForResult(intent, REQUEST_CODE)
    }

    fun openGallery(view: View){
        val REQUEST_CODE = 99
        val preference = ScanConstants.OPEN_MEDIA
        val intent = Intent(this, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 99 && resultCode == RESULT_OK) {
            val uri = data?.extras!!.getParcelable<Uri>(ScanConstants.SCANNED_RESULT)
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                contentResolver.delete(uri!!, null, null)
                //scannedImageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}