package hr.algebra.esouvenir

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.esouvenir.framework.startActivity
import hr.algebra.esouvenir.model.Category

class CategoryAdapter(private val context: Context, private val categories: MutableList<Category>)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    class ViewHolder(categoryView: View) : RecyclerView.ViewHolder(categoryView) {

        val cardView = itemView.findViewById<CardView>(R.id.cardView)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.linear)
        private val ivCategory = itemView.findViewById<ImageView>(R.id.ivCategory)
        val tvCategory = itemView.findViewById<TextView>(R.id.tvCategory)

        fun bind(item: Category) {
            tvCategory.text = item.name
            ivCategory.setImageResource(item.icon)
            linearLayout.setBackgroundResource(item.background)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(categoryView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            //start novi activity (ako je prva cat onda to, ako je druga...)
            if (position == 0) {
                context.startActivity<GoogleMapLocationsActivity>()
            }
            else if(position == 1) {
                context.startActivity<AccommodationEdit>()
            }
            else if(position == 2) {
                context.startActivity<WalletActivity>()
            }

            //context.startActivity<MealPagerActivity>(ITEM_POSITION, position, CATEGORY, holder.tvCategory.text)
        }


        holder.bind(categories[position])
    }


    override fun getItemCount() = categories.size


}