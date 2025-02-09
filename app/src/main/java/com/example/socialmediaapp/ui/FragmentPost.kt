package com.example.socialmediaapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapter.PostAdapter
import com.example.socialmediaapp.databinding.FragmentPostsBinding
import com.example.socialmediaapp.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentPost : Fragment(R.layout.fragment_posts) {
    lateinit var binding : FragmentPostsBinding
    lateinit var recyclerView: RecyclerView
    lateinit var postAdapter: PostAdapter
    private val postList= arrayListOf<Post>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentPostsBinding.bind(view)

        recyclerView=binding.recyclerViewPosts
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        postAdapter= PostAdapter(postList,object :PostAdapter.OnItemClickListener{
            override fun OnItemClick(post: Post) {
                var action=FragmentPostDirections.actionNavPostToNavProfile()
                findNavController().navigate(action)
            }
        })
        recyclerView.adapter=postAdapter

        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var postRef=FirebaseDatabase.getInstance().getReference("post").child(firebaseAuth!!)
        postRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        var post=i.getValue(Post::class.java)
                        if (post!=null){
                            postList.add(post)
                        }
                    }
                    postAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}