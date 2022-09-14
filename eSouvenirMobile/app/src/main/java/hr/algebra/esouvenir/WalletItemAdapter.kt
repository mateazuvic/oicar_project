package hr.algebra.esouvenir

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.esouvenir.api.WalletFetcher
import hr.algebra.esouvenir.model.WalletItem
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class WalletItemAdapter(private val context: Context, private val items: MutableList<WalletItem>, private val navigableFragment: NavigableFragment)
    : RecyclerView.Adapter<WalletItemAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val ivImage = itemView.findViewById<ImageView>(R.id.ivImage)
        val ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        fun bind(item: WalletItem) {
            tvTitle.text = item.name.toString()
            if(item.pictures != null) {
                item.pictures!!.forEach {
                    Picasso.get()
                        .load(File(it.picturePath))
                        .transform(RoundedCornersTransformation(50, 5)) // radius, margin
                        .into(ivImage)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = ViewHolder(itemView = LayoutInflater.from(context).inflate(R.layout.wallet_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //delete i long click go to edit

        holder.ivDelete.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to delete wallet item?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _,_ ->
                    // Delete from database

                    GlobalScope.launch(Dispatchers.Main) { // dispatch in MAIN thread
                        WalletFetcher(context).deleteItem(items[position].idWalletItem!!)
                        items.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        holder.itemView.setOnLongClickListener {
            navigableFragment.navigate(Bundle().apply {
                putInt(ITEM_ID, items[position].idWalletItem!!)
            })
            true
        }

        holder.bind(items[position])
    }

    override fun getItemCount()= items.size
}