package com.example.socialmediaapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.FragmentProfileBinding
import com.example.socialmediaapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentProfile : Fragment(R.layout.fragment_profile) {
    lateinit var binding : FragmentProfileBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentProfileBinding.bind(view)

        getInfo()
        getFollowers()
        getFollowings()

        binding.btnEditProfile.setOnClickListener(){
            var action=FragmentProfileDirections.actionNavProfileToNavAccount()
            findNavController().navigate(action)
        }
    }

    private fun getFollowings() {
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var followingRef=FirebaseDatabase.getInstance().getReference("Follow")
            .child(firebaseAuth!!).child("Following")
        followingRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.totalFollowing.text=snapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun getFollowers() {
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var followersRef=FirebaseDatabase.getInstance().getReference("Follow")
            .child(firebaseAuth!!).child("Followers")
        followersRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.totalFollowers.text=snapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun getInfo() {
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var userRef=FirebaseDatabase.getInstance().getReference("user").child(firebaseAuth!!)
        userRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var user=snapshot.getValue(User::class.java)
                    if (user!=null){
                        Glide.with(requireContext())
                            .load(user.image)
                            .error(R.drawable.ic_error)
                            .into(binding.profileImage)
                        binding.profileUsername.text=user.username
                        binding.fullUsername.text=user.fullname
                        binding.bioProfile.text=user.bio
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}