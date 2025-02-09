package com.example.socialmediaapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.socialmediaapp.MainActivity
import com.example.socialmediaapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToRegisterTextview.setOnClickListener(){
            var intent= Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.loginButtonLogin.setOnClickListener(){
            var email=binding.emailEdittextLogin.text.toString()
            var pass=binding.passwordEdittextLogin.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailEdittextLogin.error="Harus sesuai format"
                binding.emailEdittextLogin.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()){
                binding.emailEdittextLogin.error="Jangan kosong"
                binding.emailEdittextLogin.requestFocus()
                return@setOnClickListener
            }
            if (pass.isEmpty()){
                binding.passwordEdittextLogin.error="Jangan kosong"
                binding.passwordEdittextLogin.requestFocus()
                return@setOnClickListener
            }
            if (pass.length < 8){
                binding.passwordEdittextLogin.error="Pass tidak boleh kurang dari 8"
                binding.passwordEdittextLogin.requestFocus()
                return@setOnClickListener
            }
            loginUser(email,pass)
        }
    }

    private fun loginUser(email: String, pass: String) {
        var firebaseAuth=FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(this,"Berhasil",Toast.LENGTH_SHORT).show()
                var intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Gagal",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        if (firebaseAuth!=null){
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}