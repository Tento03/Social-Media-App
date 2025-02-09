package com.example.socialmediaapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.FragmentFriendsProfileBinding
import com.example.socialmediaapp.model.Friend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentFriendsProfile : Fragment(R.layout.fragment_friends_profile) {
    lateinit var binding : FragmentFriendsProfileBinding
    private val args by navArgs<FragmentFriendsProfileArgs>()
    private var isFollowing=false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentFriendsProfileBinding.bind(view)

        var id= args.friend.uid
        if (id != null) {
            checkIsFollowing(id)
        }
        getInfo()
        getFollowers()
        getFollowings()

        binding.btnEditProfile.setOnClickListener {
            if (isFollowing) {
                if (id != null) {
                    unfollowUser(id)
                }
                binding.btnEditProfile.text = "Follow"
            } else {
                if (id != null) {
                    followUser(id)
                }
                binding.btnEditProfile.text = "Following"
            }
            isFollowing = !isFollowing // Toggle status following
        }

        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        if (firebaseAuth==args.friend.uid){
            binding.btnEditProfile.visibility=View.GONE
        }
    }

    private fun checkIsFollowing(id: String) {
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("Follow")
            .child(firebaseAuth!!).child("Following").child(id)
        firebaseDatabase.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.btnEditProfile.text= if (isFollowing) "Follow" else "Following"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun followUser(publisher: String) {
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var followingRef=FirebaseDatabase.getInstance().getReference("Follow")
            .child(firebaseAuth!!).child("Following")
        var followersRef=FirebaseDatabase.getInstance().getReference("Follow")
            .child(publisher).child("Followers")
        followingRef.child(publisher).setValue(true).addOnCompleteListener{
            if (it.isSuccessful){
                followersRef.child(firebaseAuth).setValue(true).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(requireContext(),"Followed",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(),"Failed to Followed",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun unfollowUser(publisher: String) {
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var followingRef=FirebaseDatabase.getInstance().getReference("Follow")
            .child(firebaseAuth!!).child("Following")
        var followersRef=FirebaseDatabase.getInstance().getReference("Follow")
            .child(publisher).child("Followers")
        followingRef.child(publisher).removeValue().addOnCompleteListener{
            if (it.isSuccessful){
                followersRef.child(firebaseAuth).removeValue().addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(requireContext(),"Followed",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(),"Failed to Followed",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getFollowings() {
        val followingRef=FirebaseDatabase.getInstance().getReference("Follow")
            .child(args.friend!!.uid).child("Following")
        followingRef.addListenerForSingleValueEvent(object :ValueEventListener{
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
        val followersRef= args.friend?.let {
            FirebaseDatabase.getInstance().getReference("Follow")
                .child(it.uid).child("Followers")
        }
        if (followersRef != null) {
            followersRef.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        binding.totalFollowers.text=snapshot.childrenCount.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    private fun getInfo() {
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("user")
            .child(args.friend!!.uid)
        firebaseDatabase.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var user=snapshot.getValue(Friend::class.java)
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