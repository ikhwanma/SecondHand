package binar.lima.satu.secondhand.view.fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentAddProductBinding
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import java.io.ByteArrayOutputStream


class AddProductFragment : Fragment() , View.OnClickListener{

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var image : Uri

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

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

        binding.imgProduct.setOnClickListener{
            openGallery()
        }
        binding.btnTerbitkan.setOnClickListener(this)
    }

    private fun openGallery() {
        galleryResult.launch("image/*")
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_terbitkan -> {
                binding.apply {
                    val name = etProduct.text.toString()
                    val price = etPrice.text.toString()
                    val category = etCategory.text.toString()
                    val description = etDescription.text.toString()

                    userViewModel.getToken().observe(viewLifecycleOwner){token->
                        apiViewModel.getLoginUser(token).observe(viewLifecycleOwner){
                            when(it.status){
                                SUCCESS -> {
                                    val data = it.data!!

                                    val bitmap = (imgProduct.drawable as BitmapDrawable).bitmap
                                    val bitmaps = Bitmap.createScaledBitmap(bitmap, 720, 720, true)
                                    val baos = ByteArrayOutputStream()
                                    bitmaps.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                    val dataImage = baos.toByteArray()

/*                                   val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, image)
                                    val baos = ByteArrayOutputStream()
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                    val bitmaps = Bitmap.createScaledBitmap(bitmap, 720, 720, true)*/
                                    val product = ProductBody(name, description, price.toInt(), listOf(category.toInt()), data.address, dataImage)
                                    Toast.makeText(requireContext(), image.toString(), Toast.LENGTH_SHORT).show()
                                    addProcuct(product, token)
                                }
                                ERROR -> {
                                    Toast.makeText(requireContext(), "Err", Toast.LENGTH_SHORT).show()
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
        apiViewModel.addSellerProduct(token, product).observe(viewLifecycleOwner){
            when(it.status){
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