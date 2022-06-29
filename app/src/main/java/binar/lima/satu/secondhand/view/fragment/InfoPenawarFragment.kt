package binar.lima.satu.secondhand.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.databinding.FragmentAddProductBinding
import binar.lima.satu.secondhand.databinding.FragmentInfoPenawarBinding
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel

class InfoPenawarFragment : Fragment() {

    private var _binding: FragmentInfoPenawarBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var bottomSheetBehavior: BottomSheetDialogFragment
    private var idProduct : Int = 0
    val bottomsheet : InfoPenawarBottomSheet()
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoPenawarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("extra_id_order") as Int

    }

}