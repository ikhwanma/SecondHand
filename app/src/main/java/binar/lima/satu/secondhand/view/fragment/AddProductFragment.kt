package binar.lima.satu.secondhand.view.fragment

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentAddProductBinding
import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import binar.lima.satu.secondhand.view.dialogfragment.CategoryFragment
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.ByteArrayOutputStream
import java.io.File


class AddProductFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var image: Uri
    private lateinit var listCategory: List<GetSellerCategoryResponseItem>

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var token: String
    private lateinit var user: GetLoginResponse



    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            binding.imgProduct.setImageURI(result)
            image = result!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgProduct.setOnClickListener {
            openGallery()
        }

        userViewModel.listCategorySelected.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                setCategory(it)
                var txtCategory = ""

                for (cat in it){
                    txtCategory += "${cat.name} "
                }
                binding.tvSelectCategory.text = txtCategory
                binding.tvSelectCategory.setTextColor(Color.BLACK)
            }
        }

        userViewModel.getToken().observe(viewLifecycleOwner) {
            if (it == ""){
                Navigation.findNavController(requireView()).navigate(R.id.action_addProductFragment_to_loginFragment)
            }else{
                setToken(it)
                apiViewModel.getLoginUser(it).observe(viewLifecycleOwner) { it1 ->
                    when (it1.status) {
                        SUCCESS -> {
                            setUser(it1.data!!)
                        }
                        ERROR -> {

                        }
                        LOADING -> {

                        }
                    }
                }
            }
        }

        binding.btnTerbitkan.setOnClickListener(this)
        binding.btnPreview.setOnClickListener {
            toProductPreview()
        }
        binding.btnCategory.setOnClickListener {
            CategoryFragment().show(requireActivity().supportFragmentManager, null)
        }
    }

    private fun setUser(data: GetLoginResponse) {
        user = data
    }

    private fun setToken(it: String) {
        token = it
    }

    private fun setCategory(listCategory: List<GetSellerCategoryResponseItem>) {
        this.listCategory = listCategory
    }

    private fun openGallery() {
        galleryResult.launch("image/*")
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_terbitkan -> {
                binding.apply {
                    val name = etProduct.text.toString()
                    val price = etPrice.text.toString()
                    val description = etDescription.text.toString()

                    addProduct(name, price , description)
                }
            }
        }
    }

    private fun addProduct(name: String, price: String, description: String) {
        val contentResolver = requireActivity().applicationContext.contentResolver

        val type = contentResolver.getType(image)
        val tempFile = File.createTempFile("temp-", null, null)
        val inputStream = contentResolver.openInputStream(image)

        tempFile.outputStream().use {
            inputStream?.copyTo(it)
        }

        var listCat = ""

        for (i in listCategory.indices){
            listCat += if(i == listCategory.size-1){
                "${listCategory[i].id}"
            }else{
                "${listCategory[i].id},"
            }

        }

        Toast.makeText(requireContext(), listCat, Toast.LENGTH_SHORT).show()

        val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())

        val imageUpload =
            MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
        val nameUpload =
            name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val priceUpload =
            price.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val categoryUpload =
            listCat.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val descriptionUpload =
            description.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val addressUpload =
            user.address.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        apiViewModel.addSellerProduct(token, nameUpload, descriptionUpload, priceUpload, categoryUpload, addressUpload, imageUpload).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    Toast.makeText(requireContext(), "Sukses", Toast.LENGTH_SHORT).show()
                    userViewModel.listCategorySelected.postValue(mutableListOf())
                }
                ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                LOADING -> {
                    Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun toProductPreview() {
        binding.apply {
            val name = etProduct.text.toString()
            val price = etPrice.text.toString().toInt()

            val description = etDescription.text.toString()

            val product = ProductBody(name, description, price, listOf("1".toInt()), user.city, image)
            val mBundle = bundleOf(ProductPreviewFragment.EXTRA_PRODUCT to product)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_addProductFragment_to_productPreviewFragment, mBundle)
        }
    }

}

