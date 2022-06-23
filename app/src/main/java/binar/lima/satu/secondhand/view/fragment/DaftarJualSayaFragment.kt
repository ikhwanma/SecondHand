package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentAddProductBinding
import binar.lima.satu.secondhand.databinding.FragmentDaftarJualSayaBinding
import binar.lima.satu.secondhand.view.adapter.ProductAdapter

import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel

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

        userViewModel.getToken().observe(viewLifecycleOwner){
            apiViewModel.getSellerProduct(it).observe(viewLifecycleOwner){ product ->
                when(product.status){
                    SUCCESS -> {
                        binding.apply {
                            val data = product.data

                            val adapter = ProductAdapter{

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
        }
    }

}