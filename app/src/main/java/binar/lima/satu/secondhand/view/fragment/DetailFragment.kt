package binar.lima.satu.secondhand.view.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Converter
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentDetailBinding
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.model.buyer.wishlist.PostWishlistBody
import binar.lima.satu.secondhand.model.product.GetDetailProductResponse
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.model.seller.order.PutOrderBody
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar


class DetailFragment : Fragment() , View.OnClickListener{

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>
    private var idProduct : Int = 0

    private var token = ""
    private var detailProductResponse: GetDetailProductResponse? = null

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

        /*val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_homeFragment)
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)*/

        idProduct = arguments?.getInt(EXTRA_ID) as Int

        userViewModel.getToken().observe(viewLifecycleOwner){token ->
            setToken(token)
            getOrder(token)
        }
        getWishList()

        apiViewModel.getProduct(idProduct).observe(viewLifecycleOwner){ it ->
            when(it.status){
                SUCCESS -> {
                    val data = it.data!!

                    setDetail(data)

                    val txtPrice = "Rp ${Converter.converterMoney(data.basePrice.toString())}"

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

                        if (data.status == "sold"){
                            val txtButton = "Produk sudah terjual"
                            btnTertarik.isEnabled = false
                            btnTertarik.setBackgroundResource(R.drawable.style_button_order)
                            btnTertarik.text = txtButton
                            btnWishlist.visibility = View.GONE
                        }

                        userViewModel.getToken().observe(viewLifecycleOwner){token ->
                            if (token != ""){
                                apiViewModel.getLoginUser(token).observe(viewLifecycleOwner){ user ->
                                    when(user.status){
                                        SUCCESS -> {
                                            if (data.user.id == user.data!!.id) {
                                                if (data.status == "sold"){
                                                    val txtButton = "Produk sudah terjual"
                                                    btnTertarik.isEnabled = false
                                                    btnTertarik.setBackgroundResource(R.drawable.style_button_order)
                                                    btnTertarik.text = txtButton
                                                    btnWishlist.visibility = View.GONE
                                                }else{
                                                    llBtnTertarik.visibility = View.GONE
                                                    llBtnEdit.visibility = View.VISIBLE
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
                        }
                    }

                    if (category.isNotEmpty()){
                        apiViewModel.getAllProduct(status = "available", category_id = category[0].id).observe(viewLifecycleOwner){ product ->
                            when(product.status){
                                SUCCESS -> {
                                    val dataProduct = product.data!!

                                    binding.apply {
                                        detailItem.visibility = View.VISIBLE
                                        progressCircular.visibility = View.GONE
                                    }

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
                                        val mBundle = bundleOf(EXTRA_ID to it.id)
                                        Navigation.findNavController(requireView())
                                            .navigate(R.id.detailFragment, mBundle)
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
                    binding.apply {
                        detailItem.visibility = View.GONE
                    }
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
            btnEditProduk.setOnClickListener(this@DetailFragment)
            btnDeleteProduk.setOnClickListener(this@DetailFragment)
        }

    }

    private fun setDetail(data: GetDetailProductResponse) {
        this.detailProductResponse = data
    }

    private fun getOrder(token: String) {
        apiViewModel.getBuyerOrder(token).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    val data = it.data!!

                    for (order in data){
                        if (order.productId == idProduct && order.status == "pending" || order.status == "success"){
                            val txtButton = "Menunggu respon penjual"
                            binding.apply {
                                btnTertarik.setBackgroundResource(R.drawable.style_button_order)
                                btnTertarik.isEnabled = false
                                btnTertarik.text = txtButton
                            }
                            break
                        }
                        if (order.productId == idProduct && order.status == "declined" || order.status == "tolak"){
                            val txtButton = "Berikan tawaran baru"
                            binding.apply {
                                btnTertarik.text = txtButton
                                btnKirim.setOnClickListener {
                                    val bid = binding.etNegoPrice.text.toString().toInt()
                                    apiViewModel.updateBuyerOrder(token, order.id, PutOrderBody(bid)).observe(viewLifecycleOwner){ order->
                                        when(order.status){
                                            SUCCESS -> {
                                                Snackbar.make(requireView(), "Berhasil memberikan penawaran, Tunggu respon penjual", Snackbar.LENGTH_LONG).show()
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

    private fun getWishList() {
        userViewModel.getToken().observe(viewLifecycleOwner) { token ->
            apiViewModel.getBuyerWishlist(token).observe(viewLifecycleOwner){
                when(it.status){
                    SUCCESS -> {
                        val data = it.data!!

                        binding.apply {
                            if (data.isEmpty()) {
                                cvBadge.visibility = View.VISIBLE
                                tvWishlist.text = 0.toString()
                            } else {
                                cvBadge.visibility = View.VISIBLE
                                tvWishlist.text = data.size.toString()
                            }
                        }

                        for(wish in data){
                            if (wish.productId == idProduct){
                                binding.apply {
                                    tvStatusWishlist.text = "-"

                                    btnWishlist.setOnClickListener {
                                        val animation = AnimationUtils.loadAnimation(context, R.anim.bounce_anim)
                                        btnWishlist.startAnimation(animation)
                                        Toast.makeText(requireContext(), "Dihapus dari wishlist", Toast.LENGTH_SHORT).show()
                                        btnWishlist.setCardBackgroundColor(Color.parseColor("#061957"))
                                        tvStatusWishlist.text = "+"
                                        apiViewModel.deleteBuyerWishlist(token, wish.id).observe(viewLifecycleOwner){ it1 ->
                                            when(it1.status){
                                                SUCCESS -> {
                                                    getWishList()
                                                    btnWishlist.setOnClickListener(this@DetailFragment)
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
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
        }

        binding.btnToWishlist.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailFragment_to_wishlistFragment)
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
                                    Snackbar.make(requireView(), "Berhasil memberikan penawaran, Tunggu respon penjual", Snackbar.LENGTH_LONG).show()
                                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                                    Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_homeFragment)
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
                val animation = AnimationUtils.loadAnimation(context, R.anim.bounce_anim)
                binding.btnWishlist.startAnimation(animation)
                binding.tvStatusWishlist.text = "-"
                Toast.makeText(requireContext(), "Ditambahkan ke wishlist", Toast.LENGTH_SHORT).show()
                apiViewModel.postBuyerWishlist(token, PostWishlistBody(idProduct)).observe(viewLifecycleOwner){
                    when(it.status){
                        SUCCESS -> {
                            getWishList()
                        }
                        ERROR -> {
                            Log.d("ERR MSG", it.message.toString())
                        }
                        LOADING -> {

                        }
                    }
                }
            }
            R.id.btn_back -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_homeFragment)
            }
            R.id.btn_edit_produk -> {
                val mBundle = bundleOf(EditProdukFragment.EXTRA_DETAIL to detailProductResponse)
                Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_editProdukFragment, mBundle)
            }
            R.id.btn_delete_produk -> {
                apiViewModel.deleteSellerProduct(token, detailProductResponse!!.id).observe(viewLifecycleOwner){
                    when(it.status){
                        SUCCESS -> {
                            AlertDialog.Builder(requireContext()).setTitle("Hapus Produk")
                                .setMessage("Apakah Anda Yakin?")
                                .setIcon(R.mipmap.ic_launcher_round)
                                .setPositiveButton("Yes") { _, _ ->
                                    Snackbar.make(requireView(), "Produk berhasil dihapus", Snackbar.LENGTH_LONG).show()
                                    Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_homeFragment2)
                                }.setNegativeButton("No") { _, _ ->

                                }
                                .show()
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