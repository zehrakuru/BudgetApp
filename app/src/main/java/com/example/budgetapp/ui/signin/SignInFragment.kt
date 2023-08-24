package com.example.budgetapp.ui.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.budgetapp.R
import com.example.budgetapp.common.viewBinding
import com.example.budgetapp.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        auth.currentUser.let {
          findNavController().navigate(R.id.signInToSummary)
        }

        with(binding) {
            btnRegister.setOnClickListener {
                val email = editTextEmail.text.toString()
                val password = textPassword.text.toString()

                if(email.isNotEmpty() && password.isNotEmpty()) {
                    signIn(email,password)
                }
            }
            tvRegister.setOnClickListener {
                findNavController().navigate(R.id.signInToSignUp)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            findNavController().navigate(R.id.signInToSummary)
        }.addOnFailureListener {
            Snackbar.make(requireView(), it.message.orEmpty(), 1000).show()
            //show snackbar (it.message.orEmpty())
        }
    }
}