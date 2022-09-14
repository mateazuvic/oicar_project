package hr.algebra.esouvenir


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import hr.algebra.esouvenir.api.PopularLocationFetcher
import hr.algebra.esouvenir.databinding.ActivityPopularLocationEditBinding
import hr.algebra.esouvenir.framework.getIntPreference
import hr.algebra.esouvenir.framework.startActivity
import hr.algebra.esouvenir.jsonModel.AccommodationJson
import hr.algebra.esouvenir.jsonModel.PictureJson
import hr.algebra.esouvenir.jsonModel.PopularItemJson
import hr.algebra.esouvenir.model.Picture
import hr.algebra.esouvenir.model.PopularLocationItem
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList


const val LOCATION_ID = "hr.algebra.esouvenir.locationId"
const val LOCATION_NAME = "hr.algebra.esouvenir.locationName"
private const val IMAGE_TYPE = "image/*"

class PopularLocationEdit : AppCompatActivity() {

    private lateinit var binding: ActivityPopularLocationEditBinding
    private lateinit var popularItem: PopularLocationItem
    private lateinit var byteImage : ByteArray
    private lateinit var picture : Picture


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularLocationEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title=""

        //popularItem.pictures = ArrayList<Picture>()
        popularItem = PopularLocationItem()
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = intent.getStringExtra(LOCATION_NAME)
        fetchLocation()
        setupListeners()

        //callDelayed(3000L) {getPicture}

    }



    private fun fetchLocation() {
        val locationId = intent.getIntExtra(LOCATION_ID, 0)
        val idQr = this.getIntPreference(ID_QRCODE)
        val pics= mutableListOf<Picture>()
        //val idQr=4
        if(locationId != null && idQr != null) {
            //get iz baze popular loc item where geoLoc = id and qrcodeid = id
            //ako je idPopularLoc == -1 onda radi novi i bacaj u bazu
                GlobalScope.launch(Dispatchers.Main) {
                    PopularLocationFetcher(applicationContext).fetchPopularItem(locationId, idQr) {

                        it.pictures?.forEach { pj->
                            pics.add(Picture(pj.iDPicture, pj.name, pj.contentType, pj.content, pj.picturePath, pj.accomodationID,
                                pj.walletID, pj.popularLocationID))
                        }

                        popularItem = PopularLocationItem(
                            it.iDPopularLocationItem,
                            it.description,
                            it.geoLocationID,
                            it.qRCodeID,
                            it.isVisited,
                            pics

                        )
                        bindPopularItem()
                    }

                }
        }
    }

    private fun bindPopularItem() {
        if (popularItem.pictures!= null && popularItem.pictures!!.isNotEmpty()) {
            popularItem.pictures!!.forEach {
                Picasso.get()
                    .load(File(it.picturePath))
                    .transform(RoundedCornersTransformation(50, 5)) // radius, margin
                    .into(binding.ivImage)
            }

        } else {
            binding.ivImage.setImageResource(R.drawable.picture)
        }
        binding.ivVisited.setImageResource(if (popularItem.isVisited) R.drawable.success_full else R.drawable.success)
        binding.etDesc.setText(popularItem.description ?: "")

    }

    private fun setupListeners() {
        binding.ivImage.setOnLongClickListener {
            handleImage()
            true
        }

        binding.etDesc.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                popularItem.description = text?.toString()
            }
        })


        binding.ivVisited.setOnClickListener {
            popularItem.isVisited = !popularItem.isVisited
            bindPopularItem()

        }

        binding.btnSave.setOnClickListener {
            commit()

        }
    }

    private fun commit() {

        //update popular item
        var picsJson = mutableListOf<PictureJson>()
        if (popularItem.pictures != null) {
            popularItem.pictures?.forEach {
                picsJson.add(
                    PictureJson(
                        it._id,
                        it.name,
                        it.contentType,
                        it.content,
                        it.picturePath,
                        it.accomodationID,
                        it.popularLocationID,
                        it.walletID
                    )
                )
            }
        }

        var pJson = PopularItemJson(picsJson, popularItem.idPopularLocationItem, popularItem.description, popularItem.geoLocationId, popularItem.qrCodeID,
            popularItem.isVisited)

        PopularLocationFetcher(this).updatePopularItem(pJson)


        startActivity<GoogleMapLocationsActivity>()

        /*if (picture != null) {
            PictureFetcher(this).insertPicture(picture) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }*/




    }

    private fun handleImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = IMAGE_TYPE
            imageResult.launch(this)
        }


    }

    private val imageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                /*if (person.picturePath != null) {
                    File(person.picturePath).delete()
                }*/
                val dir = this?.applicationContext?.getExternalFilesDir(null)
                val file = File(dir, File.separator.toString() + UUID.randomUUID().toString() + ".jpg")
                this?.contentResolver?.openInputStream(it.data?.data as Uri).use { inputStream ->
                    FileOutputStream(file).use { fos ->
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val bos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                        fos.write(bos.toByteArray())
                        val picPath = file.absolutePath
                        if(popularItem.pictures == null) {
                            popularItem.pictures = ArrayList()
                        }
                        picture = Picture(null, UUID.randomUUID().toString(), null, null, picPath, null, null, popularItem.idPopularLocationItem)
                        if(popularItem.pictures!!.count() >= 1){
                            popularItem.pictures!!.clear()
                        }
                        popularItem.pictures?.add(picture) //ovo isprobati, staviti picture u bazu
                        bindPopularItem()

                    }
                }
            }

        }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<HomeActivity>()
    }
}