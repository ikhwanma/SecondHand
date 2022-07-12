package binar.lima.satu.secondhand.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.databinding.ItemProductBinding
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import com.bumptech.glide.Glide

class ProductAdapter(val onItemClick: (GetProductResponseItem) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GetProductResponseItem) {
            binding.apply {
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

                val txtPrice = "Rp ${data.basePrice}"
                tvCategory.text = txtCategoryFix
                tvPrice.text = txtPrice
                tvProduct.text = txtProduct
                tvCity.text = data.location
                Glide.with(itemView).load(data.imageUrl).into(imgProduct)
                root.setOnClickListener {
                    onItemClick(data)
                }

                if (data.id == -1) {
                    tvProduct.visibility = View.GONE
                    imgProduct.visibility = View.GONE
                    tvCategory.visibility = View.GONE
                    tvPrice.visibility = View.GONE
                    tvAdd.visibility = View.VISIBLE
                    imgAdd.visibility = View.VISIBLE
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GetProductResponseItem>() {
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
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<GetProductResponseItem>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemProductBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}