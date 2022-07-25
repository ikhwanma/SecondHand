package binar.lima.satu.secondhand.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.databinding.FragmentEditProdukBinding
import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.model.product.GetDetailProductResponse
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import binar.lima.satu.secondhand.view.dialogfragment.CategoryFragment
import binar.lima.satu.secondhand.view.dialogfragment.Dialog
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class EditProdukFragment : Fragment() , View.OnClickListener {

    private var _binding: FragmentEditProdukBinding? = null
    private val binding get() = _binding!!

    private var image: Uri? = null
    private lateinit var listCategory: List<GetSellerCategoryResponseItem>

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var token: String
    private lateinit var user: GetLoginResponse

    private var detailProduct: GetDetailProductResponse? = null

    private lateinit var dialog: Dialog


    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            binding.imgProduct.setImageURI(result)
            image = result!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProdukBinding.inflate(inflater, container, false)
        dialog = Dialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailProduct = arguments?.getParcelable<GetDetailProductResponse>(EXTRA_DETAIL) as GetDetailProductResponse

        var txtCategory = ""
        val size = detailProduct!!.categories.size - 1

        for ((i, data) in detailProduct!!.categories.withIndex()){
            txtCategory += if (i != size){
                "${data.name}, "
            }else{
                data.name
            }
        }

        val listCategory = mutableListOf<GetSellerCategoryResponseItem>()

        for (cat in detailProduct!!.categories){
            listCategory.add(GetSellerCategoryResponseItem("", cat.id, cat.name, ""))
        }

        setCategory(listCategory)

        binding.apply {
            etPrice.setText(detailProduct!!.basePrice.toString())
            etDescription.setText(detailProduct!!.description)
            etProduct.setText(detailProduct!!.name)
            tvSelectCategory.text = txtCategory
            tvSelectCategory.setTextColor(Color.BLACK)


            Glide.with(requireView()).load(detailProduct!!.imageUrl).into(imgProduct)
        }

        binding.imgProduct.setOnClickListener {
            checkingPermissions()
        }

        userViewModel.listCategorySelected.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                setCategory(it)
                var txtCategories = ""

                var i = 1
                for (cat in it) {
                    txtCategories += if (i != it.size) {
                        "${cat.name}, "
                    } else {
                        cat.name
                    }
                    i++
                }
                binding.tvSelectCategory.text = txtCategories
                binding.tvSelectCategory.setTextColor(Color.BLACK)
            }
        }

        userViewModel.getToken().observe(viewLifecycleOwner) {
            if (it == "") {
                Toast.makeText(requireContext(), "Login terlebih dahulu", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_addProductFragment_to_loginFragment)
            } else {
                setToken(it)
                apiViewModel.getLoginUser(it).observe(viewLifecycleOwner) { it1 ->
                    when (it1.status) {
                        Status.SUCCESS -> {
                            val data = it1.data!!

                            if (data.address == "") {
                                Toast.makeText(
                                    requireContext(),
                                    "Lengkapi profile terlebih dahulu",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Navigation.findNavController(requireView())
                                    .navigate(R.id.action_addProductFragment_to_editProfileFragment)
                            } else {
                                setUser(data)
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

        binding.btnTerbitkan.setOnClickListener(this)
        binding.btnPreview.setOnClickListener {
            binding.apply {
                val name = etProduct.text.toString()
                val price = etPrice.text.toString()
                val description = etDescription.text.toString()

                if (name != "" && price != "" && description != ""){
                    toProductPreview()
                }else{
                    Toast.makeText(requireContext(), "Isi semua field yang tersedia", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnCategory.setOnClickListener {
            CategoryFragment().show(requireActivity().supportFragmentManager, null)
        }
        binding.btnBack.setOnClickListener {
            it.findNavController().navigate(R.id.action_addProductFragment_to_homeFragment)
        }
    }

    private fun checkingPermissions() {
        if (isGranted(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                EditProfileFragment.REQUEST_CODE_PERMISSION,
            )
        ) {
            galleryResult.launch("image/*")
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", "packageName", null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
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


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_terbitkan -> {
                binding.apply {
                    val name = etProduct.text.toString()
                    val price = etPrice.text.toString()
                    val description = etDescription.text.toString()

                    if (name != "" && price != "" && description != ""){
                        addProduct(name, price, description)
                    }else{
                        Toast.makeText(requireContext(), "Isi semua field yang tersedia", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun addProduct(name: String, price: String, description: String) {
        val contentResolver = requireActivity().applicationContext.contentResolver

        val imageUpload: MultipartBody.Part? =
            if (image != null){
            val type = contentResolver.getType(image!!)
            val tempFile = File.createTempFile("temp-", null, null)
            val inputStream = contentResolver.openInputStream(image!!)

            tempFile.outputStream().use {
                inputStream?.copyTo(it)
            }
            val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())

            MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
        }else{
            null
        }


        var listCat = ""

        if (listCategory.isEmpty()){
            for (i in detailProduct!!.categories.indices){
                listCat = if (i == detailProduct!!.categories.size - 1) {
                    "${detailProduct!!.categories[i].id}"
                } else {
                    "${detailProduct!!.categories[i].id},"
                }
            }

        }
        else{
            for (i in listCategory.indices) {
                listCat += if (i == listCategory.size - 1) {
                    "${listCategory[i].id}"
                } else {
                    "${listCategory[i].id},"
                }

            }
        }
        val nameUpload =
            name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val priceUpload =
            price.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val categoryUpload =
            listCat.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val descriptionUpload =
            description.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val addressUpload =
            user.city.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        apiViewModel.updateSellerProduct(
            token,
            detailProduct!!.id,
            nameUpload,
            descriptionUpload,
            priceUpload,
            categoryUpload,
            addressUpload,
            imageUpload
        ).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    dialog.dismissDialog()
                    userViewModel.listCategorySelected.postValue(mutableListOf())
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_editProdukFragment_to_daftarJualSayaFragment)
                    Snackbar.make(requireView(), "Data berhasil diupdate", Snackbar.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    dialog.startDialog()
                }
            }
        }
    }

    private fun toProductPreview() {
        binding.apply {
            val name = etProduct.text.toString()
            val price = etPrice.text.toString().toInt()
            val description = etDescription.text.toString()

            var listCat = ""

            for (i in listCategory.indices) {
                listCat += if (i == listCategory.size - 1) {
                    "${listCategory[i].id}"
                } else {
                    "${listCategory[i].id},"
                }

            }

            val product =
                ProductBody(name, description, price, listCat, user.city, image!!)
            val mBundle = bundleOf(
                ProductPreviewFragment.EXTRA_PRODUCT to product
            )
            Navigation.findNavController(requireView())
                .navigate(R.id.action_addProductFragment_to_productPreviewFragment, mBundle)
        }
    }

    companion object{
        const val EXTRA_DETAIL = "extra_detail"
    }

}