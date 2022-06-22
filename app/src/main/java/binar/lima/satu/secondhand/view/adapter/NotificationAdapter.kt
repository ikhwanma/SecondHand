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
            dataProduct: GetDetailProductResponse,
            position: Int
        ){
            binding.apply {
                root.setOnClickListener {
                    onItemClick(data)
                }
                val txtBid = "Ditawar Rp ${data.bidPrice}"
                val txtPrice = "Rp ${dataProduct.basePrice}"
                tvBid.text = txtBid
                tvProduct.text = dataProduct.name
                tvPrice.text = txtPrice
                tvDate.text = DateConverter.convertDate(data.updatedAt)
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

    private val diffCallbackProduct = object : DiffUtil.ItemCallback<GetDetailProductResponse>(){
        override fun areItemsTheSame(
            oldItem: GetDetailProductResponse,
            newItem: GetDetailProductResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetDetailProductResponse,
            newItem: GetDetailProductResponse
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    private val differProduct = AsyncListDiffer(this, diffCallbackProduct)

    fun submitData(value : List<GetNotificationResponseItem>?) = differ.submitList(value)
    fun submitDataProduct(value : List<GetDetailProductResponse>?) = differProduct.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemNotificationBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        val dataProduct = differProduct.currentList[position]
        data.let {
            holder.bind(data, dataProduct, position)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}