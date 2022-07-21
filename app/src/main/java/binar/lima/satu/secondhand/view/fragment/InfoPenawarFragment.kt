package binar.lima.satu.secondhand.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Converter
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentInfoPenawarBinding
import binar.lima.satu.secondhand.model.seller.order.PatchOrderBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar

class InfoPenawarFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentInfoPenawarBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var bottomSheetHubungiBehavior: BottomSheetBehavior<CardView>
    private lateinit var bottomSheetStatusBehavior: BottomSheetBehavior<CardView>
    private var idOrder: Int = 0

    private var token = ""
    private var phoneNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoPenawarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idOrder = arguments?.getInt(EXTRA_ORDER_ID) as Int

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_infoPenawarFragment_to_daftarJualSayaFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        bottomSheetHubungiBehavior = BottomSheetBehavior.from(binding.bottomSheetHubungi)
        bottomSheetStatusBehavior = BottomSheetBehavior.from(binding.bottomSheetStatus)

        userViewModel.getToken().observe(viewLifecycleOwner){ token ->
            setToken(token)
            apiViewModel.getDetailSellerOrder(token, idOrder).observe(viewLifecycleOwner){
                when(it.status){
                    SUCCESS -> {
                        val data = it.data!!
                        val user = data.user
                        val product = data.product
                        setNoHp(user.phoneNumber)
                        binding.apply {

                            if (data.status == "success"){
                                llButton.visibility = View.GONE
                                llButtonSuccess.visibility = View.VISIBLE
                            }

                            val txtPrice = "Rp ${product.basePrice}"
                            val txtBid = "Ditawar Rp ${data.price}"
                            val txtDate = Converter.convertDate(data.updatedAt)

                            tvBid.text = txtBid
                            tvPrice.text = txtPrice
                            tvProduct.text = product.name
                            tvDate.text = txtDate
                            Glide.with(requireView()).load(product.imageUrl).into(imgProduct)

                            tvBuyerCity.text = user.city
                            tvBuyerName.text = user.fullName

                            tvProductMatchBuyerCity.text = user.city
                            tvProductMatchBuyerName.text = user.fullName
                            tvProductMatchName.text = product.name
                            tvProductMatchPrice.text = txtBid
                            Glide.with(requireView()).load(product.imageUrl).into(productMatchProductImg)

                            Glide.with(requireView()).load(data.user.imageUrl).into(imgBuyer)
                        }
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
        }

        bottomSheetHubungiBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.apply {
                        btnStatus.isClickable = true
                        btnHubungi.isClickable = true
                        bg.visibility = View.GONE
                    }
                }
                else binding.root.setOnClickListener {
                    bottomSheetHubungiBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.bg.visibility = View.VISIBLE
                binding.bg.alpha = slideOffset
                binding.apply {
                    btnStatus.isClickable = false
                    btnHubungi.isClickable = false
                }
            }

        })

        bottomSheetStatusBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.apply {
                        btnStatus.isClickable = true
                        btnHubungi.isClickable = true
                        bg.visibility = View.GONE
                    }
                }
                else binding.root.setOnClickListener {
                    bottomSheetStatusBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.bg.visibility = View.VISIBLE
                binding.bg.alpha = slideOffset
                binding.apply {
                    btnStatus.isClickable = false
                    btnHubungi.isClickable = false
                }
            }
        })

        binding.apply {
            btnTolak.setOnClickListener(this@InfoPenawarFragment)
            btnTerima.setOnClickListener(this@InfoPenawarFragment)
            btnHubungi.setOnClickListener(this@InfoPenawarFragment)
            btnStatus.setOnClickListener(this@InfoPenawarFragment)
            btnToWhatsapp.setOnClickListener(this@InfoPenawarFragment)
            btnKirimStatusPenjualan.setOnClickListener(this@InfoPenawarFragment)
            llBerhasil.setOnClickListener {
                radioBerhasil.isChecked = true
            }
            llBatal.setOnClickListener {
                radioBatal.isChecked = true
            }
        }

    }


    private fun setNoHp(phoneNumber: String) {
        this.phoneNumber = "62$phoneNumber"
    }

    private fun setToken(token: String){
        this.token = token
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_tolak ->{
                apiViewModel.patchSellerOrder(token,idOrder, PatchOrderBody("tolak")).observe(viewLifecycleOwner){
                    when(it.status){
                        SUCCESS -> {
                            Navigation.findNavController(requireView()).navigate(R.id.action_infoPenawarFragment_to_daftarJualSayaFragment)
                            Snackbar.make(requireView(), "Penawaran ditolak", Snackbar.LENGTH_SHORT).show()
                        }
                        ERROR -> {
                            if (it.message!!.contains("400")){
                                Snackbar.make(requireView(), "Penawaran ditolak", Snackbar.LENGTH_SHORT).show()
                                Navigation.findNavController(requireView()).navigate(R.id.action_infoPenawarFragment_to_daftarJualSayaFragment)
                            }else{
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        LOADING -> {
                            Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            R.id.btn_terima -> {
                apiViewModel.patchSellerOrder(token, idOrder, PatchOrderBody("success")).observe(viewLifecycleOwner){
                    when(it.status){
                        SUCCESS -> {
                            val mBundle = bundleOf(EXTRA_ORDER_ID to id)
                            Navigation.findNavController(requireView()).navigate(R.id.infoPenawarFragment, mBundle)
                            Snackbar.make(requireView(), "Penawaran diterima", Snackbar.LENGTH_SHORT).show()
                        }
                        ERROR -> {
                            if (it.message!!.contains("400")){
                                Snackbar.make(requireView(), "Penawaran diterima", Snackbar.LENGTH_SHORT).show()
                                val mBundle = bundleOf(EXTRA_ORDER_ID to id)
                                Navigation.findNavController(requireView()).navigate(R.id.infoPenawarFragment, mBundle)
                            }else{
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        LOADING -> {
                            Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            R.id.btn_hubungi -> {
                bottomSheetHubungiBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            R.id.btn_status -> {
                bottomSheetStatusBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            R.id.btn_to_whatsapp -> {
                val url = "https://wa.me/$phoneNumber?text=Saya%20tertarik%20membeli%20barang%20anda"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
            R.id.btn_kirim_status_penjualan -> {
                if (binding.radioBatal.isChecked){
                    apiViewModel.patchSellerOrder(token, idOrder, PatchOrderBody("declined")).observe(viewLifecycleOwner){
                        when(it.status){
                            SUCCESS -> {
                                Navigation.findNavController(requireView()).navigate(R.id.action_infoPenawarFragment_to_daftarJualSayaFragment)
                                Toast.makeText(requireContext(), "Penawaran berhasil dibatalkan", Toast.LENGTH_SHORT).show()
                            }
                            ERROR -> {
                                if (it.message!!.contains("400")){
                                    Snackbar.make(requireView(), "Penawaran dibatalkan", Snackbar.LENGTH_SHORT).show()
                                    Navigation.findNavController(requireView()).navigate(R.id.action_infoPenawarFragment_to_daftarJualSayaFragment)
                                }else{
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            LOADING -> {
                                Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }else if(binding.radioBerhasil.isChecked){
                    apiViewModel.patchSellerOrder(token, idOrder, PatchOrderBody("accepted")).observe(viewLifecycleOwner){
                        when(it.status){
                            SUCCESS -> {
                                Navigation.findNavController(requireView()).navigate(R.id.action_infoPenawarFragment_to_daftarJualSayaFragment)
                                Toast.makeText(requireContext(), "Barang berhasil terjual", Toast.LENGTH_SHORT).show()
                            }
                            ERROR -> {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            }
                            LOADING -> {
                                Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "Pilih salah satu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    companion object{
        const val EXTRA_ORDER_ID = "extra_order_id"
    }
}