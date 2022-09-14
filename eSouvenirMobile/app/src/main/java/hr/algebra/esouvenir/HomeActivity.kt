package hr.algebra.esouvenir


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.esouvenir.databinding.ActivityHomeBinding
import hr.algebra.esouvenir.framework.getStringPreference
import hr.algebra.esouvenir.framework.setIntPreference
import hr.algebra.esouvenir.framework.setStringPreference
import hr.algebra.esouvenir.framework.startActivity
import hr.algebra.esouvenir.model.Category

const val CITY_NAME = "hr.algebra.esouvenir.cityName"


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var categories: MutableList<Category>

    private var city: String = ""
    //private var idQrCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title=""


        val tvHome = findViewById<TextView>(R.id.tvHome)
        //idQrCode = intent.getIntExtra(ID_QR, 0)
        city = getStringPreference(CITY_NAME).toString()

        tvHome.text = city

        categories = ArrayList<Category>()
        categories.add(Category(0,"Popular locations", R.drawable.favorite, R.drawable.city))
        categories.add(Category(1,"Accommodation", R.drawable.accomodation, R.drawable.hotel))
        categories.add(Category(2,"Wallet", R.drawable.wallet, R.drawable.hotel_wallet))


        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CategoryAdapter(context, categories)
        }

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuLogout -> {
                setAlert()
                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }

    private fun setAlert() {
        AlertDialog.Builder(this, R.style.AlertDialog).apply {
            setTitle(R.string.logout)
            setMessage(getString(R.string.logout_question))
            setIcon(R.drawable.logout)
            setCancelable(true)
            setNegativeButton("Cancel", null)
            setPositiveButton("Ok") { _, _ -> logout()}
            show()
        }
    }

    private fun logout() {
        setStringPreference(TOKEN, "")
        setStringPreference(TOKEN_VALID_TIME, "")
        setIntPreference(USER_ID, 0)
        setIntPreference(ID_QRCODE, 0)
        setIntPreference(ID_CITY, 0)

        startActivity<LoginActivity>()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<HomeActivity>()
    }


}