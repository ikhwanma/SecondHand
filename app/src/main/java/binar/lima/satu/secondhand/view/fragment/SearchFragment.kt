package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentSearchBinding
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearch.requestFocus()

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != ""){
                    apiViewModel.getAllProduct(status = "available", search = p0.toString()).observe(viewLifecycleOwner){ data->
                        when(data.status){
                            SUCCESS -> {
                                val adapter = ProductAdapter{
                                    val mBundle = bundleOf(DetailFragment.EXTRA_ID to it.id)
                                    Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_detailFragment, mBundle)
                                }
                                adapter.submitData(data.data)

                                binding.rvProduct.adapter = adapter
                                binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
                            }
                            ERROR -> {

                            }
                            LOADING -> {

                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

}