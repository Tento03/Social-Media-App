package com.example.socialmediaapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.FragmentAccountBinding
import com.example.socialmediaapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentAccount : Fragment(R.layout.fragment_account) {
    lateinit var binding : FragmentAccountBinding
    private var imageUri: Uri?=null
    private val pickImageLauncher=registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri=it
        binding.settingsProfileImage.setImageURI(it)
    }
    private var isClicked=""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentAccountBinding.bind(view)

        binding.closeSettingsBtn.setOnClickListener(){
            var action=FragmentAccountDirections.actionNavAccountToNavProfile()
            findNavController().navigate(action)
        }
        binding.updateAccountSettingsBtn.setOnClickListener(){
            updateUsername()
        }
        binding.profileImageChangeBtn.setOnClickListener(){
            isClicked="checked"
            pickImageLauncher.launch("image/*")
        }

        getInfo()

    }

    private fun updatePfp(){
        val image=imageUri.toString()
        val userImage= hashMapOf<String,Any>(
            "image" to image
        )
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("user")
            .child(firebaseAuth!!)
        firebaseDatabase.updateChildren(userImage).addOnCompleteListener(){
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"Pfp Updaated",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(),"Pfp failed to Updated",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUsername() {
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("user")
            .child(firebaseAuth!!)
        var image=imageUri.toString()
        var fullname=binding.settingsFullName.text.toString()
        var bio=binding.settingsAddress.text.toString()
        var user= hashMapOf<String,Any>(
            "image" to image,
            "fullname" to fullname,
            "bio" to bio
        )
        firebaseDatabase.updateChildren(user).addOnCompleteListener(){
            if (it.isSuccessful){
                Toast.makeText(requireContext(),"Pfp Updaated",Toast.LENGTH_SHORT).show()
                var action=FragmentAccountDirections.actionNavAccountToNavProfile()
                findNavController().navigate(action)
            }
            else{
                Toast.makeText(requireContext(),"Pfp failed to Updated",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getInfo(){
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("user")
            .child(firebaseAuth!!)
        firebaseDatabase.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var user=snapshot.getValue(User::class.java)
                    if (user != null) {
                        Glide.with(requireContext())
                            .load(user.image)
                            .error(R.drawable.ic_error)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(binding.settingsProfileImage)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}