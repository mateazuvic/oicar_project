package hr.algebra.esouvenir

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import hr.algebra.esouvenir.api.WalletFetcher
import hr.algebra.esouvenir.databinding.FragmentWalletEditBinding
import hr.algebra.esouvenir.framework.getIntPreference
import hr.algebra.esouvenir.jsonModel.PictureJson
import hr.algebra.esouvenir.jsonModel.WalletJson
import hr.algebra.esouvenir.model.Picture
import hr.algebra.esouvenir.model.WalletItem
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

const val ITEM_ID = "itemId"
private const val IMAGE_TYPE = "image/*"

class WalletEditFragment : Fragment() {

    private var _binding: FragmentWalletEditBinding? = null
    private lateinit var walletItem: WalletItem
    private lateinit var picture : Picture


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWalletEditBinding.inflate(inflater, container, false)
        walletItem = WalletItem()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getWalletItem()
        setUpListeners()
    }

    private fun getWalletItem() {
        val itemId = arguments?.getInt(ITEM_ID)
        val pics= mutableListOf<Picture>()
        if (itemId != null) {
            GlobalScope.async {
                WalletFetcher(requireContext()).getWalletItem(itemId) {
                    it.pictures?.forEach { pj->
                        pics.add(Picture(pj.iDPicture, pj.name, pj.contentType, pj.content, pj.picturePath, pj.accomodationID,
                            pj.walletID, pj.popularLocationID))
                    }
                    walletItem = WalletItem(it.iDWalletItem, it.name, it.description, it.price, it.qRCodeID, pics)

                    bindItem()
                }

            }

        } else {
            walletItem = WalletItem()
            bindItem()
        }
    }

    private fun bindItem() {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN

        binding.etName.setText(walletItem.name ?: "")
        binding.etDesc.setText(walletItem.description ?: "")
        binding.etPrice.setText(df.format(walletItem.price).toString() ?: "0")
        if (walletItem.pictures!= null && walletItem.pictures!!.isNotEmpty()) {
            walletItem.pictures!!.forEach {
                Picasso.get()
                    .load(File(it.picturePath))
                    .transform(RoundedCornersTransformation(50, 5)) // radius, margin
                    .into(binding.ivImage)
            }

        } else {
            binding.ivImage.setImageResource(R.drawable.picture)
        }

    }

    private fun setUpListeners() {

        binding.ivImage.setOnLongClickListener {
            handleImage()
            true
        }
        binding.btnSave.setOnClickListener {
            if (formValid()) {
                commit()
            }
        }

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                walletItem.name = text?.toString()?.trim()
            }
        })

        binding.etDesc.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                walletItem.description = text?.toString()?.trim()
            }
        })

        binding.etPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var priceString = text?.toString()?.trim()
                var numeric = true

                numeric = priceString?.matches("-?\\d+(\\.\\d+)?".toRegex())!!
                if (!numeric) {
                    Toast.makeText(context, "Price is not in validate format!", Toast.LENGTH_SHORT)
                }
                walletItem.price = priceString?.toDoubleOrNull()
            }
        })

    }

    private fun formValid(): Boolean {
        var ok = true
        arrayOf(binding.etName, binding.etPrice).forEach {
            if (it.text.isNullOrEmpty()) {
                it.error = getString(R.string.please_insert_value)
                ok = false
            }
        }
        return ok
    }

    private fun commit() {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
            val pics = mutableListOf<PictureJson>()
                walletItem.pictures?.forEach { pj->
                    pics.add(PictureJson(pj._id, pj.name, pj.contentType, pj.content, pj.picturePath, pj.accomodationID, pj.popularLocationID, pj.walletID))
                }
                val wJson = WalletJson(pics, walletItem.idWalletItem, walletItem.name, walletItem.description, walletItem.price, walletItem.qrId)


                if (walletItem.idWalletItem == null) {
                    val qrId = context?.getIntPreference(ID_QRCODE)
                    walletItem.qrId=qrId
                    WalletFetcher(requireContext()).insertWalletItem(wJson) {
                        walletItem.idWalletItem = it
                    }

                } else {

                    WalletFetcher(requireContext()).updateWalletItem(wJson)
                }

            }
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

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
                val dir = context?.applicationContext?.getExternalFilesDir(null)
                val file = File(dir, File.separator.toString() + UUID.randomUUID().toString() + ".jpg")
                context?.contentResolver?.openInputStream(it.data?.data as Uri).use { inputStream ->
                    FileOutputStream(file).use { fos ->
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val bos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                        fos.write(bos.toByteArray())
                        val picPath = file.absolutePath
                        if(walletItem.pictures == null) {
                            walletItem.pictures = ArrayList()
                        }
                        picture = Picture(null, UUID.randomUUID().toString(), null, null, picPath, null, walletItem.idWalletItem, null)
                        if(walletItem.pictures!!.count() >= 1){
                            walletItem.pictures!!.clear()
                        }
                        walletItem.pictures?.add(picture) //ovo isprobati, staviti picture u bazu
                        bindItem()
                        //SPREMANJE SLIKA U BAZUU?????
                    }
                }
            }

        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}