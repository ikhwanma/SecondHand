package binar.lima.satu.secondhand.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.data.utils.DateConverter
import binar.lima.satu.secondhand.databinding.ItemNotificationBinding
import binar.lima.satu.secondhand.model.notification.GetNotificationResponseItem
import binar.lima.satu.secondhand.model.product.GetDetailProductResponse
import com.bumptech.glide.Glide

class NotificationAdapter(val onItemClick: (GetNotificationResponseItem) -> Unit) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            data: GetNotificationResponseItem,
            position: Int
        ){
            binding.apply {
                root.setOnClickListener {
                    onItemClick(data)
                }
                val product = data.product
                val txtBid = "Ditawar Rp ${data.bidPrice}"
                val txtPrice = "Rp ${product.basePrice}"
                tvBid.text = txtBid
                tvProduct.text = product.name
                tvPrice.text = txtPrice
                tvDate.text = DateConverter.convertDate(data.transactionDate)
                Glide.with(itemView).load(data.imageUrl).into(imgProduct)
                if (position == differ.currentList.size-1){
                    viewBorder.visibility = View.INVISIBLE
                }
                if (data.read){
                    imgAllert.visibility = View.GONE
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GetNotificationResponseItem>() {
        override fun areItemsTheSame(
            oldItem: GetNotificationResponseItem,
            newItem: GetNotificationResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetNotificationResponseItem,
            newItem: GetNotificationResponseItem
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value : List<GetNotificationResponseItem>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemNotificationBinding.inflate(inflater, parent, false))
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