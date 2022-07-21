package binar.lima.satu.secondhand.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.data.local.room.ProductEntity
import binar.lima.satu.secondhand.data.utils.Converter
import binar.lima.satu.secondhand.databinding.ItemProductBinding

class ProductDbAdapter : RecyclerView.Adapter<ProductDbAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding: ItemProductBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductEntity) {
            binding.apply {
                val dataName = data.name!!
                val txtCategory = data.category!!
                var txtProduct = ""
                var txtCategoryFix = ""

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

                val txtPrice = "Rp ${Converter.converterMoney(data.price.toString())}"
                tvCategory.text = txtCategoryFix
                tvPrice.text = txtPrice
                tvProduct.text = txtProduct
                tvCity.text = data.city
            }
        }
    }
    private val diffCallback = object : DiffUtil.ItemCallback<ProductEntity>(){
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitData(value: List<ProductEntity>?) = differ.submitList(value)

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