package com.example.budgetapp.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budgetapp.R
import com.example.budgetapp.common.viewBinding
import com.example.budgetapp.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        with(binding) {
            btnRegister.setOnClickListener {
                val email = editTextEmail.text.toString()
                val password = textPassword.text.toString()

                if(email.isNotEmpty() && password.isNotEmpty()) {
                    signUp(email,password)
                }
            }
        }
    }

    private fun signUp(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            findNavController().navigate(R.id.signUpToSummary)
        }.addOnFailureListener {
            Snackbar.make(requireView(), it.message.orEmpty(), 1000).show()
            //show snackbar (it.message.orEmpty())
        }
    }
}