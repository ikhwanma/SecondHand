package binar.lima.satu.secondhand.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Converter
import binar.lima.satu.secondhand.databinding.ItemHistoryBinding
import binar.lima.satu.secondhand.model.history.GetHistoryResponseItem
import com.bumptech.glide.Glide

class HistoryAdapter(val onItemClick: (GetHistoryResponseItem) -> Unit) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GetHistoryResponseItem) {
            binding.apply {
                icNotif.setBackgroundResource(R.drawable.ic_baseline_history_24)
                icNotif.visibility = View.VISIBLE
                val txtBelanja = "History"
                var txtPrice = ""
                var txtStatus = ""

                when (data.status) {
                    "success" -> {
                        txtStatus = "Tawaran Diterima"
                        txtPrice = "Menawar Rp ${Converter.converterMoney(data.price.toString())}"
                        cvStatus.setCardBackgroundColor(Color.parseColor("#7ED3FF"))
                        tvStatus.setTextColor(Color.parseColor("#003346"))
                    }
                    "declined" -> {
                        txtStatus = "Penawaran Dibatalkan"
                        txtPrice = "Menawar Rp ${Converter.converterMoney(data.price.toString())}"
                        cvStatus.setCardBackgroundColor(Color.parseColor("#FF0061"))
                        tvStatus.setTextColor(Color.parseColor("#3C0000"))
                    }
                    "tolak" -> {
                        txtStatus = "Tawaran Ditolak"
                        txtPrice = "Menawar Rp ${Converter.converterMoney(data.price.toString())}"
                        cvStatus.setCardBackgroundColor(Color.parseColor("#FF0061"))
                        tvStatus.setTextColor(Color.parseColor("#3C0000"))
                    }
                    "accepted" -> {
                        txtStatus = "Produk Dibeli"
                        txtPrice = "Menawar Rp ${Converter.converterMoney(data.price.toString())}"
                    }
                }

                tvDate.text = Converter.convertDate(data.transactionDate)
                tvBelanja.text = txtBelanja
                tvStatus.text = txtStatus
                tvPrice.text = Converter.converterMoney(data.product.basePrice.toString())
                tvBid.text = txtPrice
                tvProduct.text = data.productName
                root.setOnClickListener {
                    onItemClick(data)
                }
                Glide.with(itemView).load(data.imageUrl).into(imgProduct)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GetHistoryResponseItem>() {
        override fun areItemsTheSame(
            oldItem: GetHistoryResponseItem,
            newItem: GetHistoryResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetHistoryResponseItem,
            newItem: GetHistoryResponseItem
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<GetHistoryResponseItem>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemHistoryBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[itemCount - 1 - position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}