package binar.lima.satu.secondhand.view.dialogfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentCategoryBinding
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.view.adapter.ChooseCategoryAdapter
import binar.lima.satu.secondhand.view.adapter.SelectedCategoryAdapter
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel


class CategoryFragment : DialogFragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    val listCategory: MutableList<GetSellerCategoryResponseItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = mutableListOf<GetSellerCategoryResponseItem>()

        apiViewModel.getAllCategory().observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    val data = it.data

                    val adapter = ChooseCategoryAdapter() { category ->
                        if (list.contains(category)) {
                            list.remove(category)
                        } else {
                            if (list.size < 3) {
                                list.add(category)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Anda hanya dapat memilih maksimal 3 kategori",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        setTextChoosed(list)
                    }
                    adapter.listData = list
                    adapter.submitData(data)
                    adapter.notifyDataSetChanged()
                    binding.rvChooseKategori.adapter = adapter
                    binding.rvChooseKategori.layoutManager = GridLayoutManager(requireContext(), 2)
                }
                ERROR -> {

                }
                LOADING -> {

                }
            }
        }
        binding.btnPilih.setOnClickListener {
            userViewModel.listCategorySelected.postValue(list)
            dialog?.dismiss()
        }
    }

    private fun setTextChoosed(list: MutableList<GetSellerCategoryResponseItem>) {
        var i = 1
        var txtCategory = ""
        for (category in list) {
            txtCategory += if (i != list.size) {
                "${category.name}, "
            } else {
                category.name
            }
            i++
        }
        binding.tvChoosed.text = txtCategory
    }

}