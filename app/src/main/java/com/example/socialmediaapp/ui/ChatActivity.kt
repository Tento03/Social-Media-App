package com.example.socialmediaapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapter.ChatAdapter
import com.example.socialmediaapp.api.UserApi
import com.example.socialmediaapp.databinding.ActivityChatBinding
import com.example.socialmediaapp.model.Chat
import com.example.socialmediaapp.model.MessageRequest
import com.example.socialmediaapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import java.lang.Exception

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var recyclerView: RecyclerView
    lateinit var chatAdapter: ChatAdapter
    private val listChat= arrayListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView=binding.chatRecyclerView
        recyclerView.layoutManager=LinearLayoutManager(this)
        chatAdapter= ChatAdapter(listChat)
        recyclerView.adapter=chatAdapter

        binding.imgBack.setOnClickListener(){
            onBackPressed()
        }

        var receiverId=intent.getStringExtra(UserActivity.ID)
        var firebaseDatabase= receiverId?.let {
            FirebaseDatabase.getInstance().getReference("user")
                .child(it)
        }
        firebaseDatabase?.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var user=snapshot.getValue(User::class.java)
                    if (user!=null && user.uid==receiverId){
                        Glide.with(this@ChatActivity)
                            .load(user.image)
                            .error(R.drawable.ic_error)
                            .into(binding.imgProfile)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        readMessage()
        binding.btnSendMessage.setOnClickListener(){
            lifecycleScope.launch {
                try {
                    var message=binding.etMessage.text.toString()
                    var senderId=FirebaseAuth.getInstance().currentUser?.uid
                    if (message.isNotEmpty() && senderId!=null && receiverId!=null){
                        sendMessage(message,senderId,receiverId)
                        binding.etMessage.text.clear()
                        readMessage()

                        var messageRequest= MessageRequest("user2","user1",message)
                        val response= UserApi.apiService.send_message(messageRequest)
                        if (response.isSuccessful){
                            Log.d("FCM","Pesan terkirim")
                        }
                        else{
                            Log.w("FCM","Pesan tidak terkirim")
                        }
                    }
                }
                catch (e:Exception){
                    Log.w("FCM","${e.message.toString()}")
                }
            }
        }
    }

    private fun sendMessage(message: String, senderId: String, receiverId: String) {
        val chatRef=FirebaseDatabase.getInstance().getReference("chat")
        val timestamp = System.currentTimeMillis()
        val chat=Chat(senderId,receiverId, message, timestamp)
        chatRef.push().setValue(chat)
    }

    private fun readMessage() {
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var receiverId=intent.getStringExtra(UserActivity.ID)
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("chat")
        firebaseDatabase.orderByChild("timestamp").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    listChat.clear()
                    for (i in snapshot.children){
                        val chat=i.getValue(Chat::class.java)
                        if (chat != null &&
                            ((chat.senderId == firebaseAuth && chat.receiverId == receiverId) ||
                                    (chat.senderId == receiverId && chat.receiverId == firebaseAuth))) {
                            listChat.add(chat)
                        }
                    }
                    chatAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}