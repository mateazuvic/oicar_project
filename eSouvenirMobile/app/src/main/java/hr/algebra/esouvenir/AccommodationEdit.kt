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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import hr.algebra.esouvenir.api.AccommodationFetcher
import hr.algebra.esouvenir.api.PopularLocationFetcher
import hr.algebra.esouvenir.databinding.ActivityAccommodationEditBinding
import hr.algebra.esouvenir.framework.getIntPreference
import hr.algebra.esouvenir.framework.startActivity
import hr.algebra.esouvenir.jsonModel.AccommodationJson
import hr.algebra.esouvenir.jsonModel.PictureJson
import hr.algebra.esouvenir.model.AccommodationItem
import hr.algebra.esouvenir.model.Picture
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

private const val IMAGE_TYPE = "image/*"

class AccommodationEdit : AppCompatActivity() {

    private lateinit var binding: ActivityAccommodationEditBinding
    private lateinit var accommodationItem: AccommodationItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccommodationEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title=""

        accommodationItem = AccommodationItem()
        insertAccommodationItem()
        setUpListeners()
    }

    private fun insertAccommodationItem() {
        val pics= mutableListOf<Picture>()
        val idQr = this.getIntPreference(ID_QRCODE)
        GlobalScope.launch(Dispatchers.Main) {
            AccommodationFetcher(applicationContext).insertAccommodationItem(idQr) {

                it.pictures?.forEach { pj->
                    pics.add(Picture(pj.iDPicture, pj.name, pj.contentType, pj.content, pj.picturePath, pj.accomodationID,
                        pj.walletID, pj.popularLocationID))
                }

                accommodationItem = AccommodationItem(
                    it.iDAccomodationItem,
                    it.name,
                    it.link,
                    it.description,
                    it.qRCodeID,
                    pics
                )

                bindItem()
            }
        }
    }

    private fun bindItem() {
        if(accommodationItem.pictures != null) {
            accommodationItem.pictures!!.forEach {
                Picasso.get()
                    .load(File(it.picturePath))
                    .transform(RoundedCornersTransformation(50, 5)) // radius, margin
                    .into(binding.ivImage)
            }

        } else {
            binding.ivImage.setImageResource(R.drawable.picture)
        }
        binding.etName.setText(accommodationItem.name ?: "")
        binding.etLink.setText(accommodationItem.link ?: "")
        binding.etDesc.setText(accommodationItem.description ?: "")
    }

    private fun setUpListeners() {
        binding.etDesc.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                accommodationItem.description = text?.toString()
            }
        })

        binding.etLink.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                accommodationItem.link = text?.toString()
            }
        })

        binding.etName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                accommodationItem.name = text?.toString()
            }
        })

        binding.ivImage.setOnLongClickListener {
            handleImage()
            true
        }

        binding.btnSave.setOnClickListener {
            commit()

        }
    }

    private fun commit() {

        //update acc item
        var picsJson = mutableListOf<PictureJson>()
        if (accommodationItem.pictures != null) {
            accommodationItem.pictures?.forEach {
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

        var aJson = AccommodationJson(picsJson, accommodationItem.idAccommodationItem, accommodationItem.name, accommodationItem.link,
        accommodationItem.description, accommodationItem.qrId)
        AccommodationFetcher(this).updateAccommodation(aJson)

        Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show()

        startActivity<HomeActivity>()


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
                        if(accommodationItem.pictures == null) {
                            accommodationItem.pictures = ArrayList()
                        }
                        val picPath = file.absolutePath
                        val picture = Picture(null, UUID.randomUUID().toString(), null, null, picPath, accommodationItem.idAccommodationItem, null, null)
                        if(accommodationItem.pictures!!.count() >= 1){
                            accommodationItem.pictures!!.clear()
                        }
                        accommodationItem.pictures?.add(picture) //ovo isprobati, staviti picture u bazu
                        bindItem()

                    }
                }
            }

        }
}