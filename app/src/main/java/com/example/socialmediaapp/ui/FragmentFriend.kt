package com.example.socialmediaapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapter.PostAdapter
import com.example.socialmediaapp.databinding.FragmentFriendsBinding
import com.example.socialmediaapp.model.Friend
import com.example.socialmediaapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.reflect.Field


class FragmentFriend : Fragment(R.layout.fragment_friends) {
    lateinit var binding: FragmentFriendsBinding
    lateinit var recyclerView: RecyclerView
    lateinit var postAdapter: PostAdapter
    private var postList= arrayListOf<Post>()
    private var friendsList= arrayListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentFriendsBinding.bind(view)

        recyclerView=binding.recyclerViewPosts
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        postAdapter= PostAdapter(postList,object :PostAdapter.OnItemClickListener{
            override fun OnItemClick(post: Post) {
                var friend=Friend()
                var action=FragmentFriendDirections.actionNavFriendsToNavFriendsProfile(post,friend)
                findNavController().navigate(action)
            }
        })
        recyclerView.adapter=postAdapter

        getListFriends()
        getPostFriends()
    }

    private fun getListFriends() {
        val firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        val firebaseDatabase=FirebaseDatabase.getInstance().getReference("Follow")
            .child(firebaseAuth!!).child("Following")
        firebaseDatabase.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot?.key?.let {
                        friendsList.add(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun getPostFriends() {
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("post")
        firebaseDatabase.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        var post=i.getValue(Post::class.java)
                        if (post!=null && friendsList.contains(post.publisher)){
                            postList.add(post)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}