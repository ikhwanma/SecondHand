package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentDetailBinding
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior


class DetailFragment : Fragment() , View.OnClickListener{

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>
    private var idProduct : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        idProduct = arguments?.getInt(EXTRA_ID) as Int

        apiViewModel.getProduct(idProduct).observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    val data = it.data!!

                    val txtPrice = "Rp ${data.basePrice}"

                    val category = data.categories

                    var txtCategory = ""

                    for (cat in category){
                        txtCategory += "${cat.name} "
                    }
                    binding.apply {
                        Glide.with(requireView()).load(data.imageUrl).into(imgProduct)
                        tvProduct.text = data.name
                        tvPrice.text = txtPrice
                        tvLocation.text = data.location
                        tvCategory.text = txtCategory
                        tvDescription.text = data.description
                    }

                }
                ERROR -> {

                }
                LOADING -> {

                }
            }
        }

        binding.btnTertarik.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.btnKirim.setOnClickListener(this)
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_kirim -> {
                binding.apply {
                    val price = etNegoPrice.text.toString().toInt()
                    val order = PostOrderBody(price, idProduct)

                    userViewModel.getToken().observe(viewLifecycleOwner){
                        apiViewModel.postOrder(it, order).observe(viewLifecycleOwner){ order ->
                            when(order.status){
                                SUCCESS -> {
                                    Toast.makeText(requireContext(), "Sukses", Toast.LENGTH_SHORT).show()
                                    Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_homeFragment)
                                }
                                ERROR -> {
                                    Toast.makeText(requireContext(), order.message, Toast.LENGTH_SHORT).show()
                                }
                                LOADING -> {
                                    Toast.makeText(requireContext(), "Load", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}