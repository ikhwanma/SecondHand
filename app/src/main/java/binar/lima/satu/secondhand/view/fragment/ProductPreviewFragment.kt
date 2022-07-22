package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentProductPreviewBinding
import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.model.seller.product.ProductBody
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


class ProductPreviewFragment : Fragment() {
    private var _binding: FragmentProductPreviewBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var token : String
    private lateinit var user : GetLoginResponse

    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductPreviewBinding.inflate(inflater, container, false)
        dialog = Dialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = arguments?.getParcelable<ProductBody>(EXTRA_PRODUCT) as ProductBody

        binding.apply {
            tvProduct.text = product.name
            tvPrice.text = product.basePrice.toString()
            tvDescription.text = product.description
            imgProduct.setImageURI(product.image)
            tvLocation.text = product.location
        }

        userViewModel.getToken().observe(viewLifecycleOwner){
            setToken(it)
            apiViewModel.getLoginUser(it).observe(viewLifecycleOwner){ it1 ->
                when(it1.status){
                    SUCCESS -> {
                        setUser(it1.data)
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
        }

        binding.btnTerbitkan.setOnClickListener {
            addProduct(product)
        }
    }

    private fun addProduct(product: ProductBody) {
        val contentResolver = requireActivity().applicationContext.contentResolver

        val type = contentResolver.getType(product.image)
        val tempFile = File.createTempFile("temp-", null, null)
        val inputStream = contentResolver.openInputStream(product.image)

        tempFile.outputStream().use {
            inputStream?.copyTo(it)
        }

        val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())

        val imageUpload =
            MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
        val nameUpload =
            product.name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val priceUpload =
            product.basePrice.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val categoryUpload =
            product.category_ids.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val descriptionUpload =
            product.description.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val addressUpload =
            user.city.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        apiViewModel.addSellerProduct(token, nameUpload, descriptionUpload, priceUpload, categoryUpload, addressUpload, imageUpload).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    dialog.dismissDialog()
                    Snackbar.make(
                        requireView(),
                        "Produk Berhasil Ditambahkan",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Navigation.findNavController(requireView()).navigate(R.id.action_productPreviewFragment_to_daftarJualSayaFragment)
                }
                ERROR -> {
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                LOADING -> {
                    dialog.startDialog()
                }
            }
        }
    }

    private fun setUser(data: GetLoginResponse?) {
        user = data!!
        binding.apply {
            tvSeller.text = user.fullName
            tvLocation.text = user.city
            Glide.with(requireView()).load(user.imageUrl).into(imgSeller)
        }
    }

    private fun setToken(it: String) {
        token = it
    }

    companion object{
        const val EXTRA_PRODUCT = "extra_product"
    }
}