package com.example.tbc_test10.presentation.screen.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tbc_test10.databinding.FragmentBottomSheetBinding
import com.example.tbc_test10.presentation.screen.home.HomeFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment(){

    private lateinit var homeFragment: HomeFragment

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    interface BottomSheetListener {
        fun onOptionSelected(option: String)
    }

    private var listener: BottomSheetListener? = null

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        homeFragment = HomeFragment()



        binding.btnTakePicture.setOnClickListener {
            listener?.onOptionSelected("CAMERA")
            dismiss()
        }

        binding.btnChooseGallery.setOnClickListener {
            listener?.onOptionSelected("GALLERY")
            dismiss()
        }

        binding.btnUpload.setOnClickListener {
            listener?.onOptionSelected("UPLOAD")
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}