package com.seregaklim.androidqr

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.WriterException

class MainActivity : AppCompatActivity() {
    var im: ImageView? = null
    var bGenerate: Button? = null
    var bScanner: Button? = null
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        im = findViewById(R.id.imageView)
        bGenerate = findViewById(R.id.button)
        bScanner = findViewById(R.id.bScan)

        registerPermissionListener()

        bScanner?.setOnClickListener {
            checkCameraPermission()
        }

        bGenerate?.setOnClickListener {
            generateQrCode("Генерируем код!!")
        }
    }

    private fun generateQrCode(text: String){
        val qrGenerator = QRGEncoder(text, null, QRGContents.Type.TEXT, 500)
        try {
            val bMap = qrGenerator.getBitmap()
            im?.setImageBitmap(bMap)
        } catch (e: WriterException){

        }
    }

//если разрешение есть делаем сканер
    private fun checkCameraPermission(){
        when{
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED ->{
                Toast.makeText(this,"Camera run", Toast.LENGTH_LONG).show()

                startActivity(Intent(this, ScannerActivity::class.java))
            }

            else -> {
                pLauncher.launch(arrayOf(Manifest.permission.CAMERA))
            }
        }
    }
//разрешение на камеру
    private fun registerPermissionListener(){
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
            if(it[Manifest.permission.CAMERA] == true){
                Toast.makeText(this,"Camera run", Toast.LENGTH_LONG).show()


            } else {
                Toast.makeText(this,"Permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }



}