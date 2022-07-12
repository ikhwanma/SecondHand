package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentDetailBinding
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.model.seller.order.PutOrderBody
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
                                                    Toast.makeText(requireContext(), "Sukses", Toast.LENGTH_SHORT).show()
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
                        binding.apply {
                            rlDetail.visibility = View.VISIBLE
                            progressCircular.visibility = View.GONE
                        }
                    }
                    ERROR -> {
                        binding.apply {
                            rlDetail.visibility = View.GONE
                            progressCircular.visibility = View.GONE
                        }
                    }
                    LOADING -> {
                        binding.apply {
                            rlDetail.visibility = View.GONE
                            progressCircular.visibility = View.VISIBLE
                        }
                    }
                }
            }
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

                }
                ERROR -> {

                }
                LOADING -> {

                }
            }
        }

        binding.btnTertarik.setOnClickListener {
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

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) binding.bg.visibility = View.GONE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.bg.visibility = View.VISIBLE
                binding.bg.alpha = slideOffset
            }
        })

        binding.btnKirim.setOnClickListener(this)
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
            }
        }
    }

}