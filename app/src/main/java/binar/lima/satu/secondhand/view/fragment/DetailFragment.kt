package binar.lima.satu.secondhand.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentDetailBinding
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.model.buyer.wishlist.PostWishlistBody
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.model.seller.order.PutOrderBody
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback


class DetailFragment : Fragment() , View.OnClickListener{

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>
    private var idProduct : Int = 0

    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_homeFragment)
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        idProduct = arguments?.getInt(EXTRA_ID) as Int

        userViewModel.getToken().observe(viewLifecycleOwner){token ->
            setToken(token)
            getWishList(token)
            getOrder(token)

        }

        apiViewModel.getProduct(idProduct).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    val data = it.data!!

                    val txtPrice = "Rp ${data.basePrice}"

                    val category = data.categories

                    var txtCategory = ""

                    for (cat in category){
                        txtCategory += "${cat.name} "
                    }
                    binding.apply {
                        Glide.with(requireView()).load(data.imageUrl).into(imgProduct)
                        tvProduct.text = data.name
                        tvPrice.text = txtPrice
                        tvLocation.text = data.location
                        tvSellerCityBottomsheet.text = data.location
                        tvCategory.text = txtCategory
                        tvDescription.text = data.description
                        tvSeller.text = data.user.fullName
                        tvSellerBottomsheet.text = data.user.fullName
                        Glide.with(requireView()).load(data.user.imageUrl).into(imgSeller)
                        Glide.with(requireView()).load(data.user.imageUrl).into(imgSellerBottomsheet)
                    }

                    if (category.isNotEmpty()){
                        apiViewModel.getAllProduct(status = "available", category_id = category[0].id).observe(viewLifecycleOwner){ product ->
                            when(product.status){
                                SUCCESS -> {
                                    val dataProduct = product.data!!

                                    val listData = mutableListOf<GetProductResponseItem>()
                                    var listDataProduct = mutableListOf<GetProductResponseItem>()



                                    for (dp in dataProduct){
                                        if (dp.id == idProduct)continue
                                        else listData.add(dp)
                                    }

                                    if (listData.size > 10){
                                        for (i in 0..9){
                                            listDataProduct.add(listData[i])
                                        }
                                    }else{
                                        listDataProduct = listData
                                    }


                                    val adapter = ProductAdapter{

                                    }.apply {
                                        submitData(listDataProduct)
                                    }

                                    binding.apply {
                                        rvProduct.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                                        rvProduct.adapter = adapter
                                    }
                                }
                                ERROR -> {

                                }
                                LOADING -> {

                                }
                            }

                        }
                    }
                }
                ERROR -> {

                }
                LOADING -> {

                }
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.apply {
                        btnBack.isClickable = true
                        bg.visibility = View.GONE
                    }
                }
                else {
                    binding.bg.setOnClickListener {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        bottomSheetBehavior.setPeekHeight(0)
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.bg.visibility = View.VISIBLE
                binding.bg.alpha = slideOffset
                binding.apply {
                    btnBack.isClickable = false
                }
            }

        })

        binding.apply {
            btnTertarik.setOnClickListener(this@DetailFragment)
            btnKirim.setOnClickListener(this@DetailFragment)
            btnWishlist.setOnClickListener(this@DetailFragment)
            btnBack.setOnClickListener(this@DetailFragment)
        }
    }

    private fun getOrder(token: String) {
        apiViewModel.getBuyerOrder(token).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    val data = it.data!!

                    for (order in data){
                        if (order.productId == idProduct && order.status == "pending"){
                            val txtButton = "Menunggu respon penjual"
                            binding.apply {
                                btnTertarik.setBackgroundResource(R.drawable.style_button_order)
                                btnTertarik.isEnabled = false
                                btnTertarik.text = txtButton
                            }
                            break
                        }
                        if (order.productId == idProduct && order.status == "declined"){
                            val txtButton = "Berikan tawaran baru"
                            binding.apply {
                                btnTertarik.text = txtButton
                                btnKirim.setOnClickListener {
                                    val bid = binding.etNegoPrice.text.toString().toInt()
                                    apiViewModel.updateBuyerOrder(token, order.id, PutOrderBody(bid)).observe(viewLifecycleOwner){ order->
                                        when(order.status){
                                            SUCCESS -> {
                                                Toast.makeText(requireContext(), "Penawaran dikirim", Toast.LENGTH_SHORT).show()
                                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                                                Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_homeFragment)
                                            }
                                            ERROR -> {
                                                Toast.makeText(requireContext(), order.message, Toast.LENGTH_SHORT).show()
                                            }
                                            LOADING -> {
                                                Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                }
                            }
                            break
                        }
                    }

                }
                ERROR -> {

                }
                LOADING -> {

                }
            }
        }
    }

    private fun getWishList(token: String) {
        apiViewModel.getBuyerWishlist(token).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    val data = it.data!!

                    for(wish in data){
                        if (wish.productId == idProduct){
                            binding.apply {
                                btnWishlist.setCardBackgroundColor(Color.parseColor("#D0D0D0"))
                                btnWishlist.setOnClickListener {
                                    Toast.makeText(requireContext(), "Anda sudah menambahkan ke wishlist", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                ERROR -> {

                }
                LOADING -> {

                }
            }
        }
    }


    private fun setToken(token: String?) {
        this.token = token!!
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_kirim -> {
                binding.apply {
                    val price = etNegoPrice.text.toString().toInt()
                    val order = PostOrderBody(price, idProduct)

                    userViewModel.getToken().observe(viewLifecycleOwner){
                        apiViewModel.postOrder(it, order).observe(viewLifecycleOwner){ order ->
                            when(order.status){
                                SUCCESS -> {
                                    Toast.makeText(requireContext(), "Sukses", Toast.LENGTH_SHORT).show()
                                    getOrder(token)
                                }
                                ERROR -> {
                                    Toast.makeText(requireContext(), order.message, Toast.LENGTH_SHORT).show()
                                }
                                LOADING -> {
                                    Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
            R.id.btn_tertarik -> {
                userViewModel.getToken().observe(viewLifecycleOwner){ token ->
                    if(token != ""){
                        apiViewModel.getLoginUser(token).observe(viewLifecycleOwner){ user ->
                            when(user.status){
                                SUCCESS -> {
                                    val data = user.data!!

                                    if (data.address == ""){
                                        Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_editProfileFragment)
                                        Toast.makeText(requireContext(), "Lengkapi profile terlebih dahulu", Toast.LENGTH_SHORT).show()
                                    }else{
                                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                                    }
                                }
                                ERROR -> {

                                }
                                LOADING -> {

                                }
                            }
                        }
                    }else{
                        Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_loginFragment)
                        Toast.makeText(requireContext(), "Login terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.btn_wishlist -> {
                apiViewModel.postBuyerWishlist(token, PostWishlistBody(idProduct)).observe(viewLifecycleOwner){
                    when(it.status){
                        SUCCESS -> {
                            Toast.makeText(requireContext(), "Ditambahkan ke wishlist", Toast.LENGTH_SHORT).show()
                            getWishList(token)
                        }
                        ERROR -> {
                            Log.d("ERR MSG", it.message.toString())
                        }
                        LOADING -> {
                            Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            R.id.btn_back -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_homeFragment)
            }
        }
    }

}