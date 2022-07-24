package binar.lima.satu.secondhand.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.data.utils.Converter
import binar.lima.satu.secondhand.databinding.ItemProductBinding
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import coil.load
import com.bumptech.glide.Glide

class ProductPagerAdapter(val onItemClick: (GetProductResponseItem) -> Unit) :
    PagingDataAdapter<GetProductResponseItem, ProductPagerAdapter.ViewHolder>(ProductComparator) {
    class ViewHolder(val view: ItemProductBinding) : RecyclerView.ViewHolder(view.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)!!
        holder.view.apply {
            val dataName = data.name
            var txtCategory = ""
            var txtProduct = ""
            var txtCategoryFix = ""
            if (data.categories.isNotEmpty()) {
                var i = 1
                for (category in data.categories) {
                    txtCategory += if (i != data.categories.size){
                        "${category.name}, "
                    }else{
                        category.name
                    }
                    i++
                }
            }
            if (dataName.length >= 32){
                for (i in 0..32){
                    txtProduct += dataName[i]
                }
                txtProduct += "..."
            }else{
                txtProduct = dataName
            }

            if (txtCategory.length >= 32){
                for (i in 0..32){
                    txtCategoryFix += txtCategory[i]
                }
                txtCategoryFix += "..."
            }else{
                txtCategoryFix = txtCategory
            }

            val txtPrice = "Rp ${Converter.converterMoney(data.basePrice.toString())}"
            tvCategory.text = txtCategoryFix
            tvPrice.text = txtPrice
            tvProduct.text = txtProduct
            tvCity.text = data.location
            imgProduct.load(data.imageUrl)
            Glide.with(holder.itemView).load(data.imageUrl).into(imgProduct)
            root.setOnClickListener {
                Log.d("tes position", position.toString())
                onItemClick(data)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    object ProductComparator : DiffUtil.ItemCallback<GetProductResponseItem>() {
        override fun areItemsTheSame(
            oldItem: GetProductResponseItem,
            newItem: GetProductResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetProductResponseItem,
            newItem: GetProductResponseItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}