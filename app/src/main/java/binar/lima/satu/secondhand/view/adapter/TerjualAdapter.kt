package binar.lima.satu.secondhand.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.data.utils.Converter
import binar.lima.satu.secondhand.databinding.ItemNotificationBinding
import binar.lima.satu.secondhand.model.seller.order.GetSellerOrderResponseItem
import com.bumptech.glide.Glide

class TerjualAdapter (val onItemClick: (GetSellerOrderResponseItem) -> Unit) :
    RecyclerView.Adapter<TerjualAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GetSellerOrderResponseItem, position: Int) {
            binding.apply {
                root.setOnClickListener {
                    onItemClick(data)
                }
                val txtStatus = "Berhasil Terjual"
                val txtBid = "Rp ${Converter.converterMoney(data.price.toString())}"
                tvBid.text = txtBid
                tvPrice.visibility = View.GONE
                tvProduct.text = data.product.name
                tvDate.text = Converter.convertDate(data.updatedAt)
                tvStatus.text = txtStatus
                Glide.with(itemView).load(data.product.imageUrl).into(imgProduct)
                if (position == differ.currentList.size - 1) {
                    viewBorder.visibility = View.INVISIBLE
                }
                imgAllert.visibility = View.GONE
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GetSellerOrderResponseItem>() {
        override fun areItemsTheSame(
            oldItem: GetSellerOrderResponseItem,
            newItem: GetSellerOrderResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetSellerOrderResponseItem,
            newItem: GetSellerOrderResponseItem
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<GetSellerOrderResponseItem>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerjualAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemNotificationBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TerjualAdapter.ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data, position)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}