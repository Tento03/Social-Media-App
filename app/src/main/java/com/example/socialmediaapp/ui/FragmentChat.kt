package com.example.socialmediaapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapter.ChatAdapter
import com.example.socialmediaapp.api.UserApi
import com.example.socialmediaapp.databinding.FragmentChatBinding
import com.example.socialmediaapp.model.Chat
import com.example.socialmediaapp.model.MessageRequest
import com.example.socialmediaapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class FragmentChat : Fragment(R.layout.fragment_chat) {
    lateinit var binding : FragmentChatBinding
    lateinit var chatAdapter: ChatAdapter
    lateinit var recyclerView: RecyclerView
    private val chatList= arrayListOf<Chat>()
    private val args by navArgs<FragmentChatArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentChatBinding.bind(view)

       var intent= Intent(requireContext(),ChatActivity::class.java)
        startActivity(intent)
    }
}