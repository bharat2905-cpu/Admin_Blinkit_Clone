package com.abc.adminmyshop.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abc.adminmyshop.activity.AdminMainActivity
import com.abc.adminmyshop.R
import com.abc.adminmyshop.models.Admins
import com.abc.adminmyshop.Utils
import com.abc.adminmyshop.databinding.FragmentOTPBinding
import com.abc.adminmyshop.viewModels.AuthViewModel
import kotlinx.coroutines.launch

class OTPFragment : Fragment() {

    private val viewModel : AuthViewModel by viewModels()
    private lateinit var binding : FragmentOTPBinding
    private lateinit var userNumber : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(layoutInflater)

        getUserNumber()
        customizingEnteringOTP()
        sendOTP()
        onLoginButtonClicked()
        onBackButtonClicked()
        return binding.root

//        return inflater.inflate(R.layout.fragment_o_t_p, container, false)
    }

    private fun onLoginButtonClicked() {
        binding.btnLogin.setOnClickListener {
            Utils.showDialog(requireContext(), "Signing you...")
            val editTexts = arrayOf(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6)
            val otp = editTexts.joinToString("") { it.text.toString() }

            if (otp.length < editTexts.size){
                Utils.showToast(requireContext(), "Please enter right otp")
            }
            else{
                editTexts.forEach { it.text?.clear(); it.clearFocus() }
                verifyOtp(otp)
            }
        }
    }

    private fun verifyOtp(otp: String) {
        val admins = Admins(uid = null, adminPhoneNumber = userNumber )

        viewModel.signInWithPhoneAuthCredential(otp, userNumber, admins)

        lifecycleScope.launch {
            viewModel.isSignedInSuccessfully.collect{
                if (it){
                    Utils.hideDialog()
                    Utils.showToast(requireContext(), "Logged In...")
                    startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP...")
        viewModel.apply {
            sendOTP(userNumber, requireActivity())
            lifecycleScope.launch {
                otpSent.collect{otpSent ->
                    if (otpSent){
                        Utils.hideDialog()
                        Utils.showToast(requireContext(), "Otp sent to the number...")
                    }
                }
            }
        }
    }

    private fun onBackButtonClicked() {
        binding.tbOtpFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment2)
        }
    }

    private fun customizingEnteringOTP() {
        val editTexts = arrayOf(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6)
        for (i in editTexts.indices){
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1){
                        if (i < editTexts.size - 1){
                            editTexts[i + 1].requestFocus()
                        }
                    } else if (s?.length == 0){
                        if (i > 0){
                            editTexts[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()

        binding.tvUserNumber.text = userNumber
    }


}