package binar.lima.satu.secondhand.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.lima.satu.secondhand.data.utils.Converter
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.ItemWishlistBinding
import binar.lima.satu.secondhand.model.buyer.wishlist.GetWishlistResponseItem
import binar.lima.satu.secondhand.view.fragment.WishlistFragment
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import com.bumptech.glide.Glide


class WishlistAdapter(
    val apiViewModel: ApiViewModel,
    val token: String,
    val viewLifecycleOwner: LifecycleOwner,
    val dataWl: MutableList<GetWishlistResponseItem>?
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemWishlistBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(
            data: GetWishlistResponseItem,
            position: Int
        ){
            binding.apply {
                Glide.with(itemView).load(data.product.imageUrl).into(binding.imgProduct)
                tvProduct.text = data.product.name
                val txtPrice = "Rp ${Converter.converterMoney(data.product.basePrice.toString())}"
                tvPrice.text = txtPrice

                btnDelete.setOnClickListener {
                    apiViewModel.deleteBuyerWishlist(token, data.id).observe(viewLifecycleOwner){
                        when(it.status){
                            SUCCESS -> {
                                Log.d("sukses", "sukses")
                                val wishlistFragment = WishlistFragment()
                                notifyDataSetChanged()
                                dataWl!!.remove(data)
                                wishlistFragment.getData(dataWl, token)
                            }
                            ERROR -> {

                            }
                            LOADING -> {

                            }
                        }
                    }


                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GetWishlistResponseItem>(){
        override fun areItemsTheSame(
            oldItem: GetWishlistResponseItem,
            newItem: GetWishlistResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetWishlistResponseItem,
            newItem: GetWishlistResponseItem
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value : List<GetWishlistResponseItem>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemWishlistBinding.inflate(inflater, parent, false))
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