package binar.lima.satu.secondhand.view.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.databinding.FragmentAddProductBinding
import binar.lima.satu.secondhand.databinding.FragmentProductPreviewBinding
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File


class ProductPreviewFragment : Fragment() {
    private var _binding: FragmentProductPreviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var image: Uri
    private lateinit var listCategory: List<GetSellerCategoryResponseItem>

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var imgFile: File
    private lateinit var easyImage: EasyImage

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
//            binding.imgProduct.setImageURI(result)
            image = result!!


        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_preview, container, false)
        getPreviewData()
    }

    private fun getPreviewData(){
        val args = this.arguments
        val productName = args?.get("name")
        val productPrice = args?.get("price")
        val productCategory = args?.get("category")
        val productDesc = args?.get("description")
        binding.tvProductPreviewName.text = productName.toString()
        binding.tvProductPreviewPrice.text = productPrice.toString()
        binding.tvProductPreviewType.text = productCategory.toString()
        binding.tvProductPreviewDescriptionLorem.text = productDesc.toString()
    }
}