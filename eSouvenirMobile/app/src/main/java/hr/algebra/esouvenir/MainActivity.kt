package hr.algebra.esouvenir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import hr.algebra.esouvenir.databinding.ActivityMainBinding
import hr.algebra.esouvenir.framework.*
import java.lang.System.exit
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val DELAY = 4500L
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title=""

        progressBar = findViewById(R.id.progressBar)
        callDelayed(1000L) {progressBar.setProgress(20, false)}
        hideSystemBars()
        callDelayed(1000L) {progressBar.setProgress(40, false)}
        redirect()

    }


    private fun redirect() {


        //callDelayed(1500L) {progressBar.setProgress(40, false)}
        callDelayed(1000L) {progressBar.setProgress(80, false)}
        callDelayed(1000L) {progressBar.setProgress(98, false)}
        if (getStringPreference(TOKEN_VALID_TIME) != "") {
            val tokenTime = getStringPreference(TOKEN_VALID_TIME)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val token = LocalDateTime.parse(tokenTime, formatter)
            val current = LocalDateTime.now()
            if (token > current) {
                callDelayed(DELAY) {startActivity<QRScannerActivity>()}
            } else {
                callDelayed(DELAY) {startActivity<LoginActivity>()}
            }
        } else {
            callDelayed(DELAY) {startActivity<LoginActivity>()}
        }



        //callDelayed(DELAY) {startActivity<PopularLocationEdit>()} //nasa ekstenzijska metoda
        //callDelayed(DELAY) {startActivity<HomeActivity>()} //nasa ekstenzijska metoda
    }


    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}