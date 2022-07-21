package binar.lima.satu.secondhand.view.fragment

import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentEditProfileBinding
import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

class EditProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var token: String
    private lateinit var user: GetLoginResponse

    private var image: Uri? = null
    private var address = ""
    private var city = ""

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            binding.imgUser.setImageURI(result)
            image = result!!
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSimpan.setOnClickListener(this)

        userViewModel.getToken().observe(viewLifecycleOwner) {
            setToken(it)
            apiViewModel.getLoginUser(it).observe(viewLifecycleOwner) { user ->
                when (user.status) {
                    SUCCESS -> {
                        binding.apply {
                            val data = user.data!!

                            if (data.address == "") {
                                etNamaEditProfile.setText(data.fullName)
                            } else {
                                etNamaEditProfile.setText(data.fullName)
                                etNoHandphoneEditProfile.setText(data.phoneNumber)
                                if (this@EditProfileFragment.address == ""){
                                    tvAddress.text = data.address
                                    tvAddress.setTextColor(Color.BLACK)
                                    this@EditProfileFragment.address = data.address
                                    this@EditProfileFragment.city = splitCity(data.address.split(",").toTypedArray())
                                }else{
                                    tvAddress.text = address
                                }
                                Glide.with(requireView()).load(data.imageUrl).into(imgUser)
                            }

                            setUser(data)
                        }
                    }
                    ERROR -> {

                    }
                    LOADING -> {

                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_editProfileFragment_to_profileFragment)
        }

        binding.imgUser.setOnClickListener {
            galleryResult.launch("image/*")
        }

        binding.tvAddress.setOnClickListener {
            val intent = LocationPickerActivity.Builder()
                .withSearchZone("id_ID")
                .withSatelliteViewHidden()
                .withGoogleTimeZoneEnabled()
                .withLegacyLayout()
                .withGooglePlacesApiKey("AIzaSyBZdgmI20l3ZRll31z0Nb2mA7gTOzwU8jw")
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(requireContext())
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            if (requestCode == 1) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                val address = addresses[0].getAddressLine(0)

                val splitAddress = address.split(",").toTypedArray()

                val city = splitCity(splitAddress)

                this.city = city
                this.address = address

                binding.tvAddress.text = address
                binding.tvAddress.setTextColor(Color.BLACK)
                Log.d("Address", city)
            }
        }
    }

    private fun splitCity(splitAddress: Array<String>) : String{
        var city = ""

        when (splitAddress.size) {
            8 -> {
                city = splitAddress[5]
            }
            7 -> {
                city = splitAddress[4]
            }
            6 -> {
                city = splitAddress[3]
            }
            5 -> {
                city = splitAddress[2]
            }
            4 -> {
                city = splitAddress[1]
            }
            3 -> {
                city = splitAddress[0]
            }
        }
        return city
    }


    private fun setUser(data: GetLoginResponse) {
        user = data
    }

    private fun setToken(it: String) {
        this.token = it
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_simpan -> {
                binding.apply {
                    val nama = etNamaEditProfile.text.toString()
                    val kota = this@EditProfileFragment.city
                    val alamat = this@EditProfileFragment.address
                    val handphone = etNoHandphoneEditProfile.text.toString().toLong()

                    if (nama != "" && kota != "" && alamat != "" && etNoHandphoneEditProfile.text.toString() != "" && handphone.toString().length >= 10) {
                        updateProfile(nama, kota, alamat, handphone)
                    }
                }
            }
        }
    }

    private fun updateProfile(nama: String, kota: String, alamat: String, handphone: Long) {
        val contentResolver = requireActivity().applicationContext.contentResolver

        var imageUpload: MultipartBody.Part? = null
        imageUpload = if (image != null) {
            val type = contentResolver.getType(image!!)
            val tempFile = File.createTempFile("temp-", null, null)
            val inputStream = contentResolver.openInputStream(image!!)

            tempFile.outputStream().use {
                inputStream?.copyTo(it)
            }
            val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())

            MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
        } else {
            null
        }

        val nameUpload = nama.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val kotaUpload = kota.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val alamatUpload = alamat.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val handphoneUpload =
            handphone.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val emailUpload = user.email.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        apiViewModel.updateUser(
            token,
            nameUpload,
            alamatUpload,
            emailUpload,
            handphoneUpload,
            kotaUpload,
            imageUpload
        ).observe(viewLifecycleOwner) {
            when (it.status) {
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
        }
    }
}