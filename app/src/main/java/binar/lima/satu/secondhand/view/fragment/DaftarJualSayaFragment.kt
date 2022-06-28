package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentAddProductBinding
import binar.lima.satu.secondhand.databinding.FragmentDaftarJualSayaBinding
import binar.lima.satu.secondhand.view.adapter.ProductAdapter

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
            } else {
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


        binding.btnEdit.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_daftarJualSayaFragment_to_editProfileFragment)
        }
    }


}