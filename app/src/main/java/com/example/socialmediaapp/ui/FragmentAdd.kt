package com.example.socialmediaapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.ActivityLoginBinding
import com.example.socialmediaapp.databinding.FragmentAddBinding
import com.example.socialmediaapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class FragmentAdd : Fragment(R.layout.fragment_add) {
    lateinit var binding : FragmentAddBinding
    private var imageUri:Uri?=null
    private val pickImageLauncher=registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri=it
        binding.imagePost.setImageURI(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentAddBinding.bind(view)

        binding.btnCloseAddPost.setOnClickListener(){
            var action=FragmentAddDirections.actionNavAddToNavHome()
            findNavController().navigate(action)
        }
        binding.imagePost.setOnClickListener(){
            pickImageLauncher.launch("image/*")
        }
        binding.btnSaveNewPost.setOnClickListener(){
            uploadPost()
        }
    }

    private fun uploadPost() {
        var uid=FirebaseAuth.getInstance().currentUser?.uid
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("post")

        val postId=firebaseDatabase.push().key
        var image=imageUri.toString()
        var publisher=uid
        var description=binding.etDescriptionPost.text.toString()
        var post=Post(postId!!,image, publisher!!,description)
        if (uid != null) {
            firebaseDatabase.child(uid).child(postId).setValue(post).addOnCompleteListener(){
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"Your Post has been added",Toast.LENGTH_SHORT).show()
                    var action=FragmentAddDirections.actionNavAddToNavHome()
                    findNavController().navigate(action)
                }
                else{
                    Toast.makeText(requireContext(),"Your Post failed to added",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadStorage(){
        var uid=FirebaseAuth.getInstance().currentUser?.uid
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("post")
        var firebaseStorage=FirebaseStorage.getInstance().getReference("post")

        if (imageUri.toString().isEmpty()){
            Toast.makeText(requireContext(),"Choose the image first",Toast.LENGTH_SHORT).show()
        }
        imageUri?.let {
            if (uid != null) {
                firebaseStorage.child(uid).child(firebaseDatabase.push().key!!).putFile(it)
                    .addOnSuccessListener { task->
                        task.metadata?.reference?.downloadUrl?.addOnSuccessListener{ uri ->
                            val postId=firebaseDatabase.push().key
                            var image=imageUri.toString()
                            var publisher=uid
                            var description=binding.etDescriptionPost.text.toString()
                            var post=Post(postId!!,image, publisher!!,description)
                            firebaseDatabase.child(uid).child(firebaseDatabase.push().key!!).setValue(post).addOnSuccessListener{
                                Toast.makeText(requireContext(),"Your Post has been added",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        }
    }
}