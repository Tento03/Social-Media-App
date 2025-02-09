package com.example.socialmediaapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.socialmediaapp.MainActivity
import com.example.socialmediaapp.R
import com.example.socialmediaapp.auth.LoginActivity
import com.example.socialmediaapp.databinding.FragmentLogoutBinding
import com.google.firebase.auth.FirebaseAuth


class FragmentLogout : Fragment(R.layout.fragment_logout) {
    lateinit var binding : FragmentLogoutBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentLogoutBinding.bind(view)

        binding.btnConfirmLogout.setOnClickListener(){
            var firebaseAuth=FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            var intent= Intent(requireContext(),LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnCancelLogout.setOnClickListener(){
            var intent= Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
        }
    }
}