package binar.lima.satu.secondhand.view.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
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
                            setListSelected(list)
                        } else {
                            list.add(category)
                            setListSelected(list)
                        }
                        setList(list)
                    }
                    adapter.listData = list
                    adapter.submitData(data)
                    binding.rvChooseKategori.adapter = adapter
                    binding.rvChooseKategori.layoutManager = LinearLayoutManager(requireContext())
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


    private fun setList(list: MutableList<GetSellerCategoryResponseItem>) {

        val newList = list

        val adapter = SelectedCategoryAdapter(){
            newList.remove(it)
            setList(newList)
        }
        adapter.submitData(list)

        binding.apply {
            rvCategory.adapter = adapter
            rvCategory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

    }

    companion object{
        private var listSelected = mutableListOf<GetSellerCategoryResponseItem>()

        fun setListSelected(list : MutableList<GetSellerCategoryResponseItem>){
            listSelected = list
        }

        fun getListSelected(): List<GetSellerCategoryResponseItem>{
            return listSelected
        }
    }

}