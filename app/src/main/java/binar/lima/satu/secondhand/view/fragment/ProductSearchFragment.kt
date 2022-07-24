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
import binar.lima.satu.secondhand.databinding.FragmentHomeBinding
import binar.lima.satu.secondhand.databinding.FragmentProductSearchBinding
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.view.activity.MainActivity
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel


class ProductSearchFragment : Fragment() {

    private var _binding: FragmentProductSearchBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchQuery = arguments?.getString(EXTRA_SEARCH) as String

        val txtSearch = "Pencarian : \"$searchQuery\""
        binding.tvKategori.text = txtSearch

        apiViewModel.getAllProduct("available", search = searchQuery).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    val data = it.data!!
                    val listData = mutableListOf<GetProductResponseItem>()

                    if (data.size > 50){
                        var i = 0
                        for (d in data){
                            if (i == 50) break
                            listData.add(d)
                            i++
                        }
                    }else{
                        listData.addAll(data)
                    }


                    binding.progressCircular.visibility = View.GONE

                    binding.apply {
                        val adapter = ProductAdapter(){ it1 ->
                            val mBundle = bundleOf(DetailFragment.EXTRA_ID to it1.id)
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_productSearchFragment_to_detailFragment, mBundle)
                        }
                        adapter.submitData(listData)

                        rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
                        rvProduct.adapter = adapter
                    }
                }
                ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                }
                LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
            }
        }

        binding.apply {
            etSearch.setOnClickListener{
                it.findNavController().navigate(R.id.action_productFragment_to_searchFragment)
            }
            btnWishlist.setOnClickListener {
                it.findNavController().navigate(R.id.action_productSearchFragment_to_wishlistFragment)
            }
        }

        getAddress()
        getWishlist()
    }

    private fun getWishlist() {
        userViewModel.getToken().observe(viewLifecycleOwner) { token ->
            apiViewModel.getBuyerWishlist(token).observe(viewLifecycleOwner) {
                when (it.status) {
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
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }

        }
    }

    private fun getAddress() {
        userViewModel.getToken().observe(viewLifecycleOwner) { token ->
            if (token == "") {
                val txtAddress = "Anda belum login"
                binding.tvAddress.text = txtAddress
            } else {
                apiViewModel.getLoginUser(token).observe(viewLifecycleOwner) {
                    when (it.status) {
                        SUCCESS -> {
                            val city = it.data!!.city

                            val txtAddress =
                                if (city == "") "Lengkapi Profile terlebih dahulu" else city

                            binding.tvAddress.text = txtAddress
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

    companion object{
        const val EXTRA_SEARCH = "extra_search"
    }

}