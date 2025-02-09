package com.example.socialmediaapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.MainActivity
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapter.FriendAdapter
import com.example.socialmediaapp.adapter.UserAdapter
import com.example.socialmediaapp.databinding.FragmentUserBinding
import com.example.socialmediaapp.model.Chat
import com.example.socialmediaapp.model.Friend
import com.example.socialmediaapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentUser : Fragment (R.layout.fragment_user) {
    lateinit var binding : FragmentUserBinding
    lateinit var recyclerView: RecyclerView
    lateinit var userAdapter: UserAdapter
    private val userList= arrayListOf<User>()
    private var followingList= arrayListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserBinding.bind(view)

        var intent=Intent(requireContext(),UserActivity::class.java)
        startActivity(intent)
    }
}