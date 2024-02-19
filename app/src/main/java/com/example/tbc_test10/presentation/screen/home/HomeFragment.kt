package com.example.tbc_test10.presentation.screen.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.tbc_test10.databinding.FragmentHomeBinding
import com.example.tbc_test10.presentation.screen.base.BaseFragment
import com.example.tbc_test10.presentation.screen.bottomSheet.BottomSheetFragment
import java.io.ByteArrayOutputStream


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeViewModel: HomeFragmentViewModel by viewModels()

    private lateinit var galleryResultLauncher: ActivityResultLauncher<String>

    override fun setUp() {
        galleryResultLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    val imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                    val compressedBitmap = compressBitmap(imageBitmap)
                    binding.ivChosenImage.setImageBitmap(compressedBitmap)
                }
            }
        listeners()
    }

    private fun listeners() {
        binding.btnOpenSheet.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment().apply {
                setListener(object : BottomSheetFragment.BottomSheetListener {
                    override fun onOptionSelected(option: String) {
                        when (option) {
                            "CAMERA" -> openCamera()
                            "GALLERY" -> openGallery()
                            "UPLOAD" -> {}
                        }
                    }
                })
            }
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun openCamera(){
        if (checkCameraPermission()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureLauncher.launch(takePictureIntent)
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                val compressedBitmap = compressBitmap(it)
                binding.ivChosenImage.setImageBitmap(compressedBitmap)
            }
        } else {
            Toast.makeText(context, "Failed to take a picture", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        galleryResultLauncher.launch("image/*")
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}