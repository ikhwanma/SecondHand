package binar.lima.satu.secondhand.view.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentDaftarJualSayaBinding
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.view.adapter.SellerOrderAdapter

import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.bumptech.glide.Glide

class DaftarJualSayaFragment : Fragment() {
    private var _binding: FragmentDaftarJualSayaBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDaftarJualSayaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getToken().observe(viewLifecycleOwner) {
            if (it == "") {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_daftarJualSayaFragment_to_loginFragment)
            }
        }

        binding.btnProduct.setOnClickListener {
            productSelected()
            diminatiNotSelected()
            terjualNotSelected()
            getDataProduct()
        }
        binding.btnDiminati.setOnClickListener {
            productNotSelected()
            diminatiSelected()
            terjualNotSelected()
            getDataDiminati()
        }
        binding.btnTerjual.setOnClickListener {
            productNotSelected()
            diminatiNotSelected()
            terjualSelected()

        }

        getDataProduct()
        binding.btnEdit.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_daftarJualSayaFragment_to_editProfileFragment)
        }
    }

    private fun productSelected() {
        binding.apply {
            btnProduct.setBackgroundResource(R.drawable.style_button)
            tvProduct.setTextColor(Color.WHITE)
        }
    }

    private fun productNotSelected() {
        binding.apply {
            btnProduct.setBackgroundResource(R.drawable.style_btn_category_unselected)
            tvProduct.setTextColor(Color.BLACK)
        }
    }

    private fun diminatiSelected() {
        binding.apply {
            btnDiminati.setBackgroundResource(R.drawable.style_button)
            tvDiminati.setTextColor(Color.WHITE)
            imgDiminati.setImageResource(R.drawable.ic_white_favorite_border_24)
        }
    }

    private fun diminatiNotSelected() {
        binding.apply {
            btnDiminati.setBackgroundResource(R.drawable.style_btn_category_unselected)
            tvDiminati.setTextColor(Color.BLACK)
            imgDiminati.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun terjualSelected() {
        binding.apply {
            btnTerjual.setBackgroundResource(R.drawable.style_button)
            tvTerjual.setTextColor(Color.WHITE)
            imgTerjual.setTextColor(Color.WHITE)
        }
    }

    private fun terjualNotSelected() {
        binding.apply {
            btnTerjual.setBackgroundResource(R.drawable.style_btn_category_unselected)
            tvTerjual.setTextColor(Color.BLACK)
            imgTerjual.setTextColor(Color.BLACK)
        }
    }


    private fun getDataDiminati() {
        userViewModel.getToken().observe(viewLifecycleOwner){ token ->
            apiViewModel.getSellerOrder(token).observe(viewLifecycleOwner){
                when(it.status){
                    SUCCESS -> {
                        val data = it.data

                        val adapter = SellerOrderAdapter{

                        }
                        adapter.submitData(data)
                        binding.apply {
                            rvDaftarJual.adapter = adapter
                            rvDaftarJual.layoutManager = LinearLayoutManager(requireContext())
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

    private fun getDataProduct() {
        userViewModel.getToken().observe(viewLifecycleOwner) {
            apiViewModel.getSellerProduct(it).observe(viewLifecycleOwner) { product ->
                when (product.status) {
                    SUCCESS -> {
                        binding.apply {
                            val data = product.data

                            val adapter = ProductAdapter {

                            }
                            adapter.submitData(data)

                            rvDaftarJual.adapter = adapter
                            rvDaftarJual.layoutManager = GridLayoutManager(requireContext(), 2)

                        }
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }

            apiViewModel.getLoginUser(it).observe(viewLifecycleOwner) { user ->
                when (user.status) {
                    SUCCESS -> {
                        val data = user.data!!
                        binding.tvSellerName.text = data.fullName
                        binding.tvSellerCity.text = data.city

                        if (data.imageUrl.isNotEmpty()) {
                            Glide.with(requireView()).load(data.imageUrl)
                                .into(binding.imgSeller)
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