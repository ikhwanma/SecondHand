package binar.lima.satu.secondhand.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.databinding.ItemCategoryBinding
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem

class CategoryAdapter(val context : Context, val onItemClick: (GetSellerCategoryResponseItem) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var index : Int = 0

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(data: GetSellerCategoryResponseItem, position: Int) {
            binding.apply {
                tvCategory.text = data.name

                if (index == position){
                    llKategori.setBackgroundResource(R.drawable.style_button)
                    tvCategory.setTextColor(Color.WHITE)
                    imgIcon.setImageResource(R.drawable.ic_white_search_24)
                }else{
                    llKategori.setBackgroundResource(R.drawable.style_btn_category_unselected)
                    tvCategory.setTextColor(Color.BLACK)
                    imgIcon.setImageResource(R.drawable.ic_baseline_search_24)
                }

                val animation = AnimationUtils.loadAnimation(context, R.anim.bounce_anim)
                root.setOnClickListener {
                    onItemClick(data)
                    btnCategory.startAnimation(animation)
                    index = position
                    notifyDataSetChanged()
                    Log.d("ini index", "ini index : $position")
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GetSellerCategoryResponseItem>() {
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

    fun submitData(value: List<GetSellerCategoryResponseItem>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemCategoryBinding.inflate(inflater, parent, false))
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