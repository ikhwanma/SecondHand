package binar.lima.satu.secondhand.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.data.local.room.HistoryEntity
import binar.lima.satu.secondhand.data.utils.Converter
import binar.lima.satu.secondhand.databinding.ItemProductBinding
import com.bumptech.glide.Glide

class HistoryDbAdapter(val onItemClick: (HistoryEntity) -> Unit) : RecyclerView.Adapter<HistoryDbAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding: ItemProductBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HistoryEntity){
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

                root.setOnClickListener {
                    onItemClick(data)
                }
                val txtPrice = "Rp ${Converter.converterMoney(data.price.toString())}"
                tvCategory.text = txtCategoryFix
                tvPrice.text = txtPrice
                tvProduct.text = txtProduct
                tvCity.text = data.city
                Glide.with(itemView).load(data.imageUrl).into(imgProduct)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<HistoryEntity>(){
        override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<HistoryEntity>?) = differ.submitList(value)
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