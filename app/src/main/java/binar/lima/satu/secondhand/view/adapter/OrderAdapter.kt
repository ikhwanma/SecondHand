package binar.lima.satu.secondhand.view.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Converter
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.databinding.ItemOrderBinding
import binar.lima.satu.secondhand.model.seller.order.GetSellerOrderResponseItem
import binar.lima.satu.secondhand.view.fragment.OrderFragment
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import com.bumptech.glide.Glide

class OrderAdapter(val apiViewModel: ApiViewModel,
                   val token: String,
                   val viewLifecycleOwner: LifecycleOwner,
                   val dataOrder: MutableList<GetSellerOrderResponseItem>?,
                   val onItemClick: (GetSellerOrderResponseItem) -> Unit): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged")
        fun bind(data: GetSellerOrderResponseItem){
            binding.apply {
                var txtPrice = ""
                var txtStatus = ""
                when (data.status) {
                    "success" -> {
                        txtStatus = "Diterima"
                        txtPrice = "Menawar Rp ${Converter.converterMoney(data.price.toString())}"
                        cvStatus.setCardBackgroundColor(Color.parseColor("#7ED3FF"))
                        tvStatus.setTextColor(Color.parseColor("#003346"))
                    }
                    "declined" -> {
                        txtStatus = "Dibatalkan"
                        txtPrice = "Menawar Rp ${Converter.converterMoney(data.price.toString())}"
                        cvStatus.setCardBackgroundColor(Color.parseColor("#FF0061"))
                        tvStatus.setTextColor(Color.parseColor("#3C0000"))
                    }
                    "tolak" -> {
                        txtStatus = "Ditolak"
                        txtPrice = "Menawar Rp ${Converter.converterMoney(data.price.toString())}"
                        cvStatus.setCardBackgroundColor(Color.parseColor("#FF0061"))
                        tvStatus.setTextColor(Color.parseColor("#3C0000"))
                    }
                    "pending" -> {
                        txtStatus = "Menunggu"
                        txtPrice = "Menawar Rp ${Converter.converterMoney(data.price.toString())}"
                    }
                }
                root.setOnClickListener {
                    onItemClick(data)
                }
                tvDate.text = Converter.convertDate(data.transactionDate)
                tvStatus.text = txtStatus
                tvPrice.text = Converter.converterMoney(data.product.basePrice.toString())
                tvBid.text = txtPrice
                tvProduct.text = data.productName
                tvBatalkanTransaksi.setOnClickListener {
                    AlertDialog.Builder(root.context).setTitle("Batalkan Transaksi")
                        .setMessage("Apakah Anda Yakin?")
                        .setIcon(R.drawable.ic_logo_secondhand)
                        .setPositiveButton("Iya"){ _, _ ->
                            apiViewModel.deleteBuyerOrder(token, data.id).observe(viewLifecycleOwner){
                                when(it.status){
                                    Status.SUCCESS -> {
                                        val orderFragment = OrderFragment()
                                        notifyDataSetChanged()
                                        dataOrder!!.remove(data)
                                        orderFragment.getData(dataOrder, token)
                                    }
                                    Status.ERROR -> {

                                    }
                                    Status.LOADING -> {

                                    }
                                }
                            }
                        }.setNegativeButton("Tidak"){ _, _ ->

                        }.show()

                }
                Glide.with(itemView).load(data.imageProduct).into(imgProduct)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GetSellerOrderResponseItem>(){
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemOrderBinding.inflate(inflater, parent, false))
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