package binar.lima.satu.secondhand.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.MainRepository
import binar.lima.satu.secondhand.data.utils.Resource
import binar.lima.satu.secondhand.databinding.FragmentInfoPenawarBinding
import binar.lima.satu.secondhand.databinding.InfoPenawarBottomSheetBinding
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoPenawarBottomSheet : Fragment() {
    private var _binding: InfoPenawarBottomSheetBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.info_penawar_bottom_sheet,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnToWhatsapp.setOnClickListener {
//            val sendIntent = Intent().setAction(Intent.ACTION_SEND)
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "Saya ingin membeli produk ini.").setType("text/plain")
//            sendIntent.setPackage("com.whatsapp")
//            startActivity(sendIntent)

            val contact = Resource
            val url = "https://wa.me/$contact?text=Saya%20tertarik%20membeli%20barang%20anda"
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        }
    }
}