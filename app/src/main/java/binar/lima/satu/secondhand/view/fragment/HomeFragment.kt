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
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.view.adapter.CategoryAdapter
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)

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

        apiViewModel.getSellerBanner().observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    val data = it.data!!
                    val list = ArrayList<SlideModel>()

                    for (img in data) {
                        list.add(SlideModel(img.imageUrl, ScaleTypes.FIT))
                    }

                    binding.imgSlider.setImageList(list)
                }
                ERROR -> {

                }
                LOADING -> {

                }
            }
        }
        apiViewModel.getAllCategory().observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    val adapter = CategoryAdapter(requireContext()) { data ->
                        category = data.id
                        if (category == 0) {
                            getData()
                        } else {
                            apiViewModel.getAllProduct(category_id = category, status = "available")
                                .observe(viewLifecycleOwner) { product ->
                                    when (product.status) {
                                        SUCCESS -> {
                                            binding.rvProduct.visibility = View.VISIBLE
                                            binding.progressCircular.visibility = View.GONE
                                            val adapter = ProductAdapter { data ->
                                                val mBundle =
                                                    bundleOf(DetailFragment.EXTRA_ID to data.id)
                                                Navigation.findNavController(requireView())
                                                    .navigate(
                                                        R.id.action_homeFragment_to_detailFragment,
                                                        mBundle
                                                    )
                                            }
                                            adapter.submitData(product.data)

                                            binding.apply {
                                                rvProduct.layoutManager =
                                                    GridLayoutManager(requireContext(), 2)
                                                rvProduct.adapter = adapter
                                            }
                                        }
                                        ERROR -> {

                                        }
                                        LOADING -> {
                                            binding.rvProduct.visibility = View.INVISIBLE
                                            binding.progressCircular.visibility = View.VISIBLE
                                        }
                                    }
                                }
                        }
                    }


                    val listCat = mutableListOf(
                        GetSellerCategoryResponseItem(
                            "24-06-2022", 0, "Semua", "24-06-2022"
                        )
                    )
                    val category = it.data!!
                    for (cat in category) {
                        listCat.add(cat)
                    }
                    adapter.submitData(listCat)


                    binding.apply {
                        rvKategory.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        rvKategory.adapter = adapter
                    }
                }
                ERROR -> {

                }
                LOADING -> {

                }
            }
        }
        getData()

        binding.etSearch.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun getData() {
        apiViewModel.getAllProduct(status = "available").observe(viewLifecycleOwner) { product ->
            when (product.status) {
                SUCCESS -> {
                    binding.rvProduct.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                    val adapter = ProductAdapter { data ->
                        val mBundle = bundleOf(DetailFragment.EXTRA_ID to data.id)
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_homeFragment_to_detailFragment, mBundle)
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
                    binding.rvProduct.visibility = View.INVISIBLE
                    binding.progressCircular.visibility = View.VISIBLE
                }
            }
        }
    }


}