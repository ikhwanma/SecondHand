package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentHomeBinding
import binar.lima.satu.secondhand.databinding.FragmentLoginBinding
import binar.lima.satu.secondhand.view.adapter.CategoryAdapter
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var category = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiViewModel.getAllCategory().observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    val adapter = CategoryAdapter{ data ->
                        category = data.id
                        apiViewModel.getAllProduct(category_id = category).observe(viewLifecycleOwner){ product ->
                            when(product.status){
                                SUCCESS -> {
                                    val adapter = ProductAdapter{ data ->

                                    }
                                    adapter.submitData(product.data)

                                    binding.apply {
                                        rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
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
                    adapter.submitData(it.data)


                    binding.apply {
                        rvKategory.layoutManager = LinearLayoutManager( requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        rvKategory.adapter = adapter
                    }
                }
                ERROR ->  {

                }
                LOADING -> {

                }
            }
        }
        getData()

        binding.btnAll.setOnClickListener {
            getData()
        }
    }

    private fun getData() {
        apiViewModel.getAllProduct().observe(viewLifecycleOwner){ product ->
            when(product.status){
                SUCCESS -> {
                    val adapter = ProductAdapter{ data ->

                    }
                    adapter.submitData(product.data)

                    binding.apply {
                        rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
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