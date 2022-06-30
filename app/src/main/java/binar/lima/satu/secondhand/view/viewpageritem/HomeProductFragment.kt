package binar.lima.satu.secondhand.view.viewpageritem

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
import binar.lima.satu.secondhand.databinding.FragmentHomeBinding
import binar.lima.satu.secondhand.databinding.FragmentHomeProductBinding
import binar.lima.satu.secondhand.view.adapter.DaftarJualPageAdapter
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.view.fragment.DetailFragment
import binar.lima.satu.secondhand.viewmodel.ApiViewModel


class HomeProductFragment(private val categoryId : Int) : Fragment() {

    private var _binding: FragmentHomeProductBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiViewModel.getAllProduct(category_id = categoryId, status = "available").observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    binding.rvProduct.visibility = View.VISIBLE
                    val adapter = ProductAdapter{ data ->
                        val mBundle = bundleOf(DetailFragment.EXTRA_ID to data.id)
                        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_detailFragment, mBundle)
                    }
                    adapter.submitData(it.data)

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