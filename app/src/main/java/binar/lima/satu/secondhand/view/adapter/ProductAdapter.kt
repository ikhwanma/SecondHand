package binar.lima.satu.secondhand.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.databinding.ItemProductBinding
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import com.bumptech.glide.Glide

class ProductAdapter(val onItemClick : (GetProductResponseItem) -> Unit) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : GetProductResponseItem){
            binding.apply {
                tvProduct.text = data.name
                Glide.with(itemView).load(data.imageUrl).into(imgProduct)
                if (data.categories.isNotEmpty()){
                    for (category in data.categories){
                        tvCategory.append("${category.name} ")
                    }
                }
                val txtPrice = "Rp ${data.basePrice}"
                tvPrice.text = txtPrice
                root.setOnClickListener {
                    onItemClick(data)
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GetProductResponseItem>(){
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

    fun submitData(value : List<GetProductResponseItem>?) = differ.submitList(value)

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