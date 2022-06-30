package binar.lima.satu.secondhand.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.databinding.ItemChooseCategoryBinding
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem

class ChooseCategoryAdapter(val onItemClick: (GetSellerCategoryResponseItem) -> Unit): RecyclerView.Adapter<ChooseCategoryAdapter.ViewHolder>() {

    var listData = mutableListOf<GetSellerCategoryResponseItem>()

    val listPosition = mutableListOf<GetSellerCategoryResponseItem>()

    inner class ViewHolder (private val binding : ItemChooseCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: GetSellerCategoryResponseItem, position: Int){
            binding.apply {
                tvCategory.text = data.name

                root.setOnClickListener {
                    onItemClick(data)

                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GetSellerCategoryResponseItem>(){
        override fun areItemsTheSame(
            oldItem: GetSellerCategoryResponseItem,
            newItem: GetSellerCategoryResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetSellerCategoryResponseItem,
            newItem: GetSellerCategoryResponseItem
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value : List<GetSellerCategoryResponseItem>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemChooseCategoryBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data, position)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}