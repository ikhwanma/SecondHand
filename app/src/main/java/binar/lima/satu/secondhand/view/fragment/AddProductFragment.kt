package binar.lima.satu.secondhand.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.data.utils.URIPathHelper
import binar.lima.satu.secondhand.databinding.FragmentAddProductBinding
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
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

    private lateinit var imgFile: File
    private lateinit var easyImage: EasyImage

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

        easyImage = EasyImage.Builder(requireContext()).build()

        binding.imgProduct.setOnClickListener {
            openGallery()
//            easyImage.openGallery(requireActivity())
        }
        binding.btnTerbitkan.setOnClickListener(this)
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
                    val category = etCategory.text.toString()
                    val description = etDescription.text.toString()

                    userViewModel.getToken().observe(viewLifecycleOwner) { token ->
                        apiViewModel.getLoginUser(token).observe(viewLifecycleOwner) {
                            when (it.status) {
                                SUCCESS -> {
                                    val data = it.data!!
//                                    imgFile = File("/storage/emulated/0/Download/tes2.jpg")
                                    imgFile = File("/storage/emulated/0/Download/Ellipse 1.jpg")

                                    val requestBody = imgFile.asRequestBody(null)

                                    val requestFile = imgFile
                                        .asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                    val nameUpload = name
                                        .toRequestBody(MultipartBody.FORM)
                                    val priceUpload = price
                                        .toRequestBody(MultipartBody.FORM)
                                    val categoryUpload = category
                                        .toRequestBody(MultipartBody.FORM)
                                    val descriptionUpload = description
                                        .toRequestBody(MultipartBody.FORM)
                                    val addressUpload = data.address
                                        .toRequestBody(MultipartBody.FORM)
                                    val imageUpload = MultipartBody.Part.createFormData(
                                        "foto",
                                        imgFile.name,
                                        requestFile
                                    )

                                    apiViewModel.testAddSellerProduct(
                                        token,
                                        name,
                                        description,
                                        price.toInt(),
                                        listOf(category.toInt()),
                                        data.address,
                                        imageUpload
                                    ).observe(viewLifecycleOwner) {
                                        when (it.status) {
                                            SUCCESS -> {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Sukses",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            ERROR -> {
                                                Toast.makeText(
                                                    requireContext(),
                                                    it.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            LOADING -> {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Load",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }


/*
                                    val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

                                    builder.addFormDataPart("name", name)
                                    builder.addFormDataPart("description", description)
                                    builder.addFormDataPart("base_price", price)
                                    builder.addFormDataPart("category_ids", category)
                                    builder.addFormDataPart("location", data.city)

                                    val bmp = BitmapFactory.decodeFile(imgFile.absolutePath)
                                    val baos = ByteArrayOutputStream()
                                    bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos)

                                    builder.addFormDataPart("image", imgFile.name, RequestBody.create(MultipartBody.FORM, baos.toByteArray()))

                                    val requestBody = builder.build()

                                    apiViewModel.testAddSellerProduct(token, requestBody).observe(viewLifecycleOwner){ addProduct ->
                                        when(addProduct.status){
                                            SUCCESS -> {
                                                Toast.makeText(requireContext(), "Sukses", Toast.LENGTH_SHORT).show()
                                            }
                                            ERROR -> {
                                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                                            }
                                            LOADING -> {
                                                Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }*/


                                    /*val pathHelper = URIPathHelper()

                                    imgFile = File(pathHelper.getPath(requireContext(),image)!!)

                                    val bitmap = (imgProduct.drawable as BitmapDrawable).bitmap
                                    val bitmaps = Bitmap.createScaledBitmap(bitmap, 720, 720, true)
                                    val baos = ByteArrayOutputStream()
                                    bitmaps.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                    val dataImage = baos.toByteArray()

*//*                                   val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, image)
                                    val baos = ByteArrayOutputStream()
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                    val bitmaps = Bitmap.createScaledBitmap(bitmap, 720, 720, true)*//*
                                    val product = ProductBody(name, description, price.toInt(), listOf(category.toInt()), data.address,
                                        bitMapToString(bitmaps)!!
                                    )
                                    Toast.makeText(requireContext(), imgFile.toString(), Toast.LENGTH_SHORT).show()*/
//                                    addProcuct(product, token)
                                }
                                ERROR -> {
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                LOADING -> {

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addProcuct(product: ProductBody, token: String) {
        apiViewModel.addSellerProduct(token, product).observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    Toast.makeText(requireContext(), "Sukses", Toast.LENGTH_SHORT).show()
                }
                ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                LOADING -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bitMapToString(bitmap: Bitmap): String? {
        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
        val b: ByteArray = byteArray.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

}