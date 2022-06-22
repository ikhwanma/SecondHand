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
import binar.lima.satu.secondhand.data.utils.Status
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentAddProductBinding
import binar.lima.satu.secondhand.databinding.FragmentHomeBinding
import binar.lima.satu.secondhand.databinding.FragmentProductPreviewBinding
import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.model.seller.product.ProductBody
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

    private lateinit var token : String
    private lateinit var user : GetLoginResponse


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductPreviewBinding.inflate(inflater, container, false)
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
            tvSellerCity.text = product.location
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
    }

    private fun setUser(data: GetLoginResponse?) {
        user = data!!
        binding.apply {
            tvSellerName.text = user.fullName

        }
    }

    private fun setToken(it: String) {
        token = it
    }

    companion object{
        const val EXTRA_PRODUCT = "extra_product"
    }
}