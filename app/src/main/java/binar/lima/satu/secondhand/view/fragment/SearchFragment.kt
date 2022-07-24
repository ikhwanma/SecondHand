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
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.view.adapter.ProductAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)

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
                                binding.progressCircular.visibility = View.GONE
                                val dataSearch = data.data!!
                                val listSearch = mutableListOf<GetProductResponseItem>()

                                val txtEmpty = "\"$p0\" belum tersedia:( Yuk, cari barang lain yang serupa!"

                                if (dataSearch.isEmpty()){
                                    binding.llSize.visibility = View.GONE
                                    binding.btnAll.visibility = View.GONE
                                    binding.llListKosong.visibility = View.VISIBLE
                                    binding.tvListKosong.text = txtEmpty
                                }else{
                                    binding.llListKosong.visibility = View.GONE
                                    if (dataSearch.size < 14){
                                        listSearch.addAll(dataSearch)
                                        val txtSize = "Menampilkan ${dataSearch.size} dari ${dataSearch.size} produk dalam pencarian"
                                        binding.apply {
                                            btnAll.visibility = View.GONE
                                            llSize.visibility = View.VISIBLE
                                            tvSize.text = txtSize
                                        }
                                    }else{
                                        for (i in 0..13){
                                            listSearch.add(dataSearch[i])
                                        }
                                        val txtSize = "Menampilkan 14 dari ${dataSearch.size} produk dalam pencarian"
                                        binding.apply {
                                            btnAll.visibility = View.VISIBLE
                                            llSize.visibility = View.VISIBLE
                                            tvSize.text = txtSize
                                        }
                                    }
                                }

                                binding.btnAll.setOnClickListener {
                                    val mBundle = bundleOf(ProductSearchFragment.EXTRA_SEARCH to binding.etSearch.text.toString())
                                    Navigation.findNavController(requireView())
                                        .navigate(R.id.action_searchFragment_to_productSearchFragment, mBundle)
                                }

                                val adapter = ProductAdapter{
                                    val mBundle = bundleOf(DetailFragment.EXTRA_ID to it.id)
                                    Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_detailFragment, mBundle)
                                }
                                adapter.submitData(listSearch)

                                binding.rvProduct.adapter = adapter
                                binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
                            }
                            ERROR -> {
                                binding.llSize.visibility = View.GONE
                                binding.progressCircular.visibility = View.GONE
                            }
                            LOADING -> {
                                binding.progressCircular.visibility = View.VISIBLE
                                binding.llSize.visibility = View.GONE
                            }
                        }
                    }
                }
                if (p0 == "") {
                    binding.rvProduct.visibility = View.GONE
                    binding.llSize.visibility = View.GONE
                    binding.progressCircular.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

}