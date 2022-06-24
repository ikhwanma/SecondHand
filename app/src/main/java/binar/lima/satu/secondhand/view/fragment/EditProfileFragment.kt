package binar.lima.satu.secondhand.view.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentEditProfileBinding
import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    //Define viewModel
    private val apiViewModel: ApiViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var token: String
    private lateinit var user: GetLoginResponse

    private lateinit var image: Uri

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
                            etNamaEditProfile.setText(data.fullName)
                            etAlamatEditProfile.setText(data.address)
                            etNoHandphoneEditProfile.setText(data.phoneNumber)
                            etPilihKotaEditProfile.setText(data.city)

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

        binding.btnPanah.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_editProfileFragment_to_profileFragment)
        }

        binding.imgUser.setOnClickListener {
            galleryResult.launch("image/*")
        }
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
                    val kota = etPilihKotaEditProfile.text.toString()
                    val alamat = etAlamatEditProfile.text.toString()
                    val handphone = etNoHandphoneEditProfile.text.toString().toLong()

                    updateProfile(nama, kota, alamat, handphone)

                }
            }
        }
    }

    private fun updateProfile(nama: String, kota: String, alamat: String, handphone: Long) {
        val contentResolver = requireActivity().applicationContext.contentResolver

        val type = contentResolver.getType(image)
        val tempFile = File.createTempFile("temp-", null, null)
        val inputStream = contentResolver.openInputStream(image)

        tempFile.outputStream().use {
            inputStream?.copyTo(it)
        }
        val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())

        val imageUpload =
            MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
        val nameUpload = nama.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val kotaUpload = kota.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val alamatUpload = alamat.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val handphoneUpload =
            handphone.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val passwordUpload = "123456".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val emailUpload = user.email.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        apiViewModel.updateUser(
            token,
            nameUpload,
            alamatUpload,
            emailUpload,
            passwordUpload,
            handphoneUpload,
            kotaUpload,
            imageUpload
        ).observe(viewLifecycleOwner){
            when(it.status){
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