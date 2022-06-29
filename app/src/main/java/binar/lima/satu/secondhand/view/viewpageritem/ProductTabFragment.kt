package binar.lima.satu.secondhand.view.viewpageritem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.databinding.FragmentDaftarJualSayaBinding
import binar.lima.satu.secondhand.databinding.FragmentProductTabBinding
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.bumptech.glide.Glide


class ProductTabFragment : Fragment() {

    private var _binding: FragmentProductTabBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductTabBinding.inflate(inflater, container, false)
        getDataProduct()
        return binding.root
    }

    private fun getDataProduct() {
        userViewModel.getToken().observe(viewLifecycleOwner) {
            apiViewModel.getSellerProduct(it).observe(viewLifecycleOwner) { product ->
                when (product.status) {
                    Status.SUCCESS -> {
                        binding.apply {
                            val data = product.data!!

                            val listProduct = mutableListOf(
                                GetProductResponseItem(
                                    0,
                                    listOf(),
                                    "temp",
                                    -1,
                                    "temp",
                                    "temp",
                                    "temp",
                                    "temp",
                                    "temp",
                                    -1
                                )
                            )

                            for (dataProduk in data){
                                listProduct.add(dataProduk)
                            }

                            val adapter = ProductAdapter { productData ->
                                if (productData.id == -1){
                                    Navigation.findNavController(requireView()).navigate(R.id.action_daftarJualSayaFragment_to_addProductFragment)
                                }
                            }
                            adapter.submitData(listProduct)

                            rvProduct.adapter = adapter
                            rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
                        }
                    }
                    Status.ERROR -> {

                    }
                    Status.LOADING -> {

                    }
                }
            }

        }
    }

}