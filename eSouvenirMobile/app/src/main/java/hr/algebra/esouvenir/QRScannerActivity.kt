package hr.algebra.esouvenir

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import hr.algebra.esouvenir.api.CityFetcher
import hr.algebra.esouvenir.api.QrCodeFetcher
import hr.algebra.esouvenir.framework.*
import hr.algebra.esouvenir.model.City
import hr.algebra.esouvenir.model.QRCode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val DEL=';'
const val ID_QRCODE="hr.algebra.esouvenir.idqrcode"
const val ID_CITY="hr.algebra.esouvenir.idcity"

class QRScannerActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private var idQr : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)

        if (getStringPreference(TOKEN).isNullOrEmpty()) {
            startActivity<LoginActivity>()   //AKO ISTEKNE STA ONDA, probaj smanjit u backendu trajanje tokena pa isprobaj
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)

        } else{
            startScanning()
        }
    }

    private fun startScanning() {
        val scannerView: CodeScannerView =findViewById(R.id.scannerView)
        codeScanner = CodeScanner(this, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS

        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled= false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_SHORT).show()
            }
                //pokupi podatke s qr koda
                val result = it.text.split(DEL)
                val cityName = result[0]
                idQr = result[1].toInt()
                //idQr = 7


                //insert city ako ne postoji, uvijek vrati id
                val city = City(null, cityName)
               insertCity(city)

                //spremi u bazu ako ne postoji qr code
                //u backendu insertQrCode(QRCode qr)
                //val qrCode = QRCode(null, null, 1, 3 )
                //val cityid = 0
                //var userid = 3
                //val qrCode= QRCode(idQr, null, userid, cityid)

                //insertQRCode(qrCode)
                 setStringPreference(CITY_NAME, cityName)

                //SPREMAJU SE DUPLIIII
                startActivity<HomeActivity>()

                //ako je prvi put baci ga u bazu
            //}
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun insertCity(city: City){

            CityFetcher(this).insertCity(city) {
                //Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
                if (it != null) {
                    var cityid = it
                    setIntPreference(ID_CITY, cityid)
                    //var userid = 5
                    var userid = getIntPreference(USER_ID)
                    val qrCode = QRCode(idQr, null, userid, cityid)

                    insertQRCode(qrCode, city.name)
                }

            }



    }

    private fun insertQRCode(qrCode: QRCode, cityName: String) {
        QrCodeFetcher(this).insertQrCode(qrCode) {
            Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
            if (it != 0) {
                idQr = it!!
            }

            this.setIntPreference(ID_QRCODE, idQr)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner?.startPreview()
        }
    }

    override fun onPause() {
        if (::codeScanner.isInitialized) {
            codeScanner?.releaseResources()
        }
        super.onPause()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<QRScannerActivity>()
    }


    }