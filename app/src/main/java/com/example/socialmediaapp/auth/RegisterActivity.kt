package com.example.socialmediaapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.socialmediaapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(){
            var intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener(){
            var email=binding.etEmail.text.toString()
            var pass=binding.etPassword.text.toString()
            var repass=binding.etConfirmPassword.text.toString()
            var fullname=binding.etFullName.text.toString()
            var username=binding.etUserName.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmail.error="Harus sesuai format"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()){
                binding.etEmail.error="Jangan kosong"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            if (pass.isEmpty()){
                binding.etPassword.error="Jangan kosong"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            if (pass.length < 8){
                binding.etPassword.error="Pass tidak boleh kurang dari 8"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            if (pass!=repass){
                binding.etPassword.error="Harus sesuai dengan password"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            if (username.isEmpty()){
                binding.etUserName.error="Pass tidak boleh kurang dari 8"
                binding.etUserName.requestFocus()
                return@setOnClickListener
            }
            if (fullname.isEmpty()){
                binding.etFullName.error="Pass tidak boleh kurang dari 8"
                binding.etFullName.requestFocus()
                return@setOnClickListener
            }
            registerUser(email,pass,username,fullname)
        }
    }

    private fun registerUser(email: String, pass: String, username: String, fullname: String) {
        var firebaseAuth=FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(){
            var uid=firebaseAuth.currentUser?.uid
            var firebaseDatabase= FirebaseDatabase.getInstance().getReference("user").child(uid!!)
            var user= hashMapOf<String,Any>(
                "uid" to uid.toString(),
                "email" to email,
                "password" to pass,
                "username" to username,
                "fullname" to fullname
            )
            firebaseDatabase?.setValue(user)?.addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(this,"Sukses Registrasi",Toast.LENGTH_SHORT).show()
                    var intent= Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Gagal Registrasi",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}