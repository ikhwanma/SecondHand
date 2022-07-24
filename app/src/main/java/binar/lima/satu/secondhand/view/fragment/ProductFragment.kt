package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentProductBinding
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.view.adapter.ProductPagerAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import kotlinx.coroutines.launch


class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!


    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    val list = ArrayList<GetProductResponseItem>()
    var i = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idCategory = arguments?.getInt(EXTRA_ID_CATEGORY) as Int

        if (idCategory != 0){
            apiViewModel.getDetailCategory(idCategory).observe(viewLifecycleOwner){
                when(it.status){
                    SUCCESS -> {
                        val data = it.data!!

                        binding.tvKategori.text = data.name
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
        }else{
            val txtCategory = "Semua Produk"
            binding.tvKategori.text = txtCategory
        }


        val adapter = ProductPagerAdapter {
            val mBundle = bundleOf(DetailFragment.EXTRA_ID to it.id)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_productFragment_to_detailFragment, mBundle)
        }

        if (adapter.itemCount == 100){
            Toast.makeText(requireContext(), "Ini tes", Toast.LENGTH_SHORT).show()
        }

        binding.apply {
            rvProduct.adapter = adapter
            rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        }

        apiViewModel.errorMessage.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }


        lifecycleScope.launch {
            apiViewModel.getAllProductPaging(idCategory).observe(viewLifecycleOwner){
                it.let {
                    adapter.submitData(lifecycle, it)
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }


        adapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading){
                binding.progressPaging.isVisible = true
                finishLoading(idCategory)
            }
            else {
                binding.progressPaging.isVisible = false
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_LONG).show()
                }

            }
        }


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

        binding.apply {
            etSearch.setOnClickListener{
                it.findNavController().navigate(R.id.action_productFragment_to_searchFragment)
            }
            btnWishlist.setOnClickListener {
                it.findNavController().navigate(R.id.action_productFragment_to_wishlistFragment)
            }
            tvSize.setOnClickListener {
                it.findNavController().navigate(R.id.action_productFragment_to_searchFragment)
            }
        }

        getAddress()
    }

    private fun finishLoading(idCategory: Int) {
        if (idCategory == 0){
            if (binding.progressPaging.isVisible){
                this.i++
            }
            if (i==11){
                binding.llNotFound.visibility = View.VISIBLE
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
        const val EXTRA_ID_CATEGORY = "extra_id_category"
    }
}