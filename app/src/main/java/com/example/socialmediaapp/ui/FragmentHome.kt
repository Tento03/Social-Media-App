package com.example.socialmediaapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapter.FriendAdapter
import com.example.socialmediaapp.adapter.PostAdapter
import com.example.socialmediaapp.databinding.FragmentHomeBinding
import com.example.socialmediaapp.model.Friend
import com.example.socialmediaapp.model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentHome : Fragment(R.layout.fragment_home) {
    lateinit var binding : FragmentHomeBinding
    lateinit var postAdapter: PostAdapter
    lateinit var recyclerView: RecyclerView
    private val  postList= arrayListOf<Post>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentHomeBinding.bind(view)
        setHasOptionsMenu(true)

        recyclerView=binding.recyclerViewPosts
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        postAdapter= PostAdapter(postList,object :PostAdapter.OnItemClickListener{
            override fun OnItemClick(post: Post) {
                var friend=Friend()
                var action=FragmentHomeDirections.actionNavHomeToFragmentFriendsProfile(post,friend)
                findNavController().navigate(action)
            }

        })
        recyclerView.adapter=postAdapter

        var postRef=FirebaseDatabase.getInstance().getReference("post")
        postRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        var post=i.getValue(Post::class.java)
                        if (post!=null){
                           postList.add(post)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_add_post ->{
                var action=FragmentHomeDirections.actionNavHomeToNavAdd()
                findNavController().navigate(action)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}