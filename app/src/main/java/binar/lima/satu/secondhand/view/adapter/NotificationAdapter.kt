package binar.lima.satu.secondhand.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.data.utils.Converter
import binar.lima.satu.secondhand.databinding.ItemNotificationNewBinding
import binar.lima.satu.secondhand.model.notification.GetNotificationResponseItem
import com.bumptech.glide.Glide

class NotificationAdapter(val onItemClick: (GetNotificationResponseItem) -> Unit) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemNotificationNewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            data: GetNotificationResponseItem,
            position: Int
        ){
            binding.apply {
                val formattedBidPrice = Converter.converterMoney(data.bidPrice.toString())

                var txtBid = "Ditawar Rp $formattedBidPrice"
                var txtStatus = "Produk Ditawar"
                root.setOnClickListener {
                    onItemClick(data)
                }
                when (data.status) {
                    "create" -> {
                        txtStatus = "Produk Dibuat"
                        tvBid.text = ""
                    }
                    "success" -> {
                        txtStatus = "Tawaran Diterima"
                        txtBid = "Menawar Rp $formattedBidPrice"
                        tvBid.text = txtBid
                        cvStatus.setCardBackgroundColor(Color.parseColor("#7ED3FF"))
                        tvStatus.setTextColor(Color.parseColor("#003346"))
                    }
                    "declined" -> {
                        txtStatus = "Tawaran Ditolak"
                        txtBid = "Menawar Rp $formattedBidPrice"
                        tvBid.text = txtBid
                        cvStatus.setCardBackgroundColor(Color.parseColor("#FF0061"))
                        tvStatus.setTextColor(Color.parseColor("#3C0000"))
                    }
                    "accepted" -> {
                        txtStatus = "Produk Dibeli"
                        txtBid = "Dibeli Rp $formattedBidPrice"
                        tvBid.text = txtBid
                    }
                    else -> {
                        tvBid.text = txtBid
                    }
                }
                val product = data.product
                val txtPrice = "Rp ${Converter.converterMoney(product.basePrice.toString())}"
                tvStatus.text = txtStatus
                tvProduct.text = product.name
                tvPrice.text = txtPrice
                if (data.transactionDate != null){
                    tvDate.text = Converter.convertDate(data.transactionDate)
                }else{
                    tvDate.text = ""
                }
                Glide.with(itemView).load(data.imageUrl).into(imgProduct)
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
        return ViewHolder(ItemNotificationNewBinding.inflate(inflater, parent, false))
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