package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentHomeBinding
import binar.lima.satu.secondhand.view.adapter.CategoryAdapter
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel


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

        val list = ArrayList<SlideModel>()

        list.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/market-final-project.appspot.com/o/products%2FPR-1655773426711-pexels-christian-heitz-842711.jpg?alt=media", ScaleTypes.FIT))
        list.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/market-final-project.appspot.com/o/products%2FPR-1655773426711-pexels-christian-heitz-842711.jpg?alt=media", ScaleTypes.FIT))

        binding.imgSlider.setImageList(list)
        apiViewModel.getAllCategory().observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    val adapter = CategoryAdapter{ data ->
                        category = data.id
                        apiViewModel.getAllProduct(category_id = category, status = "available").observe(viewLifecycleOwner){ product ->
                            when(product.status){
                                SUCCESS -> {
                                    val adapter = ProductAdapter{ data ->
                                        val mBundle = bundleOf(DetailFragment.EXTRA_ID to data.id)
                                        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_detailFragment, mBundle)
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
        binding.etSearch.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun getData() {
        apiViewModel.getAllProduct(status = "available").observe(viewLifecycleOwner){ product ->
            when(product.status){
                SUCCESS -> {
                    val adapter = ProductAdapter{ data ->
                        val mBundle = bundleOf(DetailFragment.EXTRA_ID to data.id)
                        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_detailFragment, mBundle)
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