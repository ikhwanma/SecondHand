package binar.lima.satu.secondhand.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import binar.lima.satu.secondhand.R
import binar.lima.satu.secondhand.data.utils.Status.*
import binar.lima.satu.secondhand.databinding.FragmentEditProfileBinding
import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.view.dialogfragment.Dialog
import binar.lima.satu.secondhand.viewmodel.ApiViewModel
import binar.lima.satu.secondhand.viewmodel.UserViewModel
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
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

    private lateinit var dialog: Dialog

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
        dialog = Dialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etNamaEditProfile.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0!!.length < 3){
                        binding.tvErrName.visibility = View.VISIBLE
                    }else{
                        binding.tvErrName.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }

        binding.apply {
            etNoHandphoneEditProfile.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0!!.length < 10){
                        binding.tvErrPhone.visibility = View.VISIBLE
                    }else{
                        binding.tvErrPhone.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }

        binding.btnSimpan.setOnClickListener(this)

        userViewModel.getToken().observe(viewLifecycleOwner) {
            setToken(it)
            apiViewModel.getLoginUser(it).observe(viewLifecycleOwner) { user ->
                when (user.status) {
                    SUCCESS -> {
                        binding.apply {
                            dialog.dismissDialog()
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
                                    this@EditProfileFragment.city = data.city
                                }else{
                                    tvAddress.text = address
                                }
                                if (data.imageUrl != null){
                                    Glide.with(requireView()).load(data.imageUrl).into(imgUser)
                                }
                            }

                            setUser(data)
                        }
                    }
                    ERROR -> {
                        dialog.dismissDialog()
                    }
                    LOADING -> {
                        dialog.startDialog()
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_editProfileFragment_to_profileFragment)
        }

        binding.imgUser.setOnClickListener {
            checkingPermissions()
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

    private fun checkingPermissions() {
        if (isGranted(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,
            )
        ) {
            galleryResult.launch("image/*")
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", "packageName", null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    @Deprecated("Deprecated in Java")
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
                binding.tvErrAddress.visibility = View.GONE
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
                    val handphone = etNoHandphoneEditProfile.text.toString()

                    if (nama != "" && nama.length >= 3 && kota != "" && alamat != "" && etNoHandphoneEditProfile.text.toString() != "" && handphone.length >= 10) {
                        updateProfile(nama, kota, alamat, handphone.toLong())
                    }
                    if (nama.length < 3){
                        binding.tvErrName.visibility = View.VISIBLE
                    }
                    if(handphone.length < 10){
                        binding.tvErrPhone.visibility = View.VISIBLE
                    }
                    if (city == ""){
                        binding.tvErrAddress.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun updateProfile(nama: String, kota: String, alamat: String, handphone: Long) {
        val contentResolver = requireActivity().applicationContext.contentResolver

        val imageUpload: MultipartBody.Part? = if (image != null) {
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
                    dialog.dismissDialog()
                    val snackBar = Snackbar.make(requireView(), "Profile diupdate", Snackbar.LENGTH_LONG)
                    snackBar.setAction("X"){
                        snackBar.dismiss()
                    }
                    snackBar.show()
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

    companion object {
        const val REQUEST_CODE_PERMISSION = 100
    }
}