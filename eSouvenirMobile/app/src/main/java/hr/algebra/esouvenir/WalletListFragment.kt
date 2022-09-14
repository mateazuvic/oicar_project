package hr.algebra.esouvenir

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.whenCreated
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.internal.zzagr.runOnUiThread
import hr.algebra.esouvenir.api.WalletFetcher
import hr.algebra.esouvenir.databinding.FragmentWalletListBinding
import hr.algebra.esouvenir.framework.startActivity
import hr.algebra.esouvenir.jsonModel.WalletJson
import hr.algebra.esouvenir.model.Picture
import hr.algebra.esouvenir.model.WalletItem
import kotlinx.coroutines.*


class WalletListFragment : Fragment(), NavigableFragment {

    private var _binding: FragmentWalletListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var walletItems : MutableList<WalletItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWalletListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadItems()
        setupListeners()
    }



    private fun loadItems() {
        //dohvati iz baze
        //val walletItems : MutableList<WalletJson>

        walletItems = mutableListOf()

        getItems()


    }

    private fun getItems()  {
        val items = mutableListOf<WalletItem>()

        GlobalScope.async{
            WalletFetcher(requireContext()).fetchItems { list ->

                list?.forEach {
                    val pics= mutableListOf<Picture>()
                    it.pictures?.forEach { pj->
                        pics.add(Picture(pj.iDPicture, pj.name, pj.contentType, pj.content, pj.picturePath, pj.accomodationID,
                            pj.walletID, pj.popularLocationID))
                    }
                    items.add(
                        WalletItem(
                            it.iDWalletItem,
                            it.name,
                            it.description,
                            it.price,
                            it.qRCodeID,
                            pics
                        )
                    )


                }

                binding.rvItems.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = WalletItemAdapter(
                        requireContext(),
                        items,
                        this@WalletListFragment
                    ) //makni negdje drugdje da se izvrsava nakon dohvata podataka
                }


            }
        }


    }

    private fun setupListeners() {
        binding.fbEdit.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigate(bundle: Bundle) {
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
    }




}