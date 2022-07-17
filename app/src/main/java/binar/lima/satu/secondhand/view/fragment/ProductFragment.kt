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
import binar.lima.satu.secondhand.databinding.FragmentProductBinding
import binar.lima.satu.secondhand.databinding.FragmentRegisterBinding
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel


class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!


    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

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

        apiViewModel.getAllProduct(status = "available", category_id = idCategory).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    val data = it.data

                    val adapter = ProductAdapter{ it1 ->
                        val mBundle = bundleOf(DetailFragment.EXTRA_ID to it1.id)
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_productFragment_to_detailFragment, mBundle)
                    }.apply {
                        submitData(data)
                    }

                    val layoutManager = GridLayoutManager(requireContext(), 2)
                    binding.apply {
                        rvProduct.adapter = adapter
                        rvProduct.layoutManager = layoutManager
                    }

                    binding.apply {
                        rvProduct.visibility = View.VISIBLE
                        tvKategori.visibility = View.VISIBLE
                        progressCircular.visibility = View.GONE
                    }
                }
                ERROR -> {
                    binding.apply {
                        rvProduct.visibility = View.VISIBLE
                        tvKategori.visibility = View.VISIBLE
                        progressCircular.visibility = View.GONE
                    }
                }
                LOADING -> {
                    binding.apply {
                        rvProduct.visibility = View.GONE
                        tvKategori.visibility = View.GONE
                    }
                }
            }
        }

        binding.apply {
            etSearch.setOnClickListener{
                it.findNavController().navigate(R.id.action_productFragment_to_searchFragment)
            }
        }
    }

    companion object{
        const val EXTRA_ID_CATEGORY = "extra_id_category"
    }
}