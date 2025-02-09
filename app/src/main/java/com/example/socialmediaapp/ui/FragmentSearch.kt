package com.example.socialmediaapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapter.FriendAdapter
import com.example.socialmediaapp.databinding.FragmentSearchBinding
import com.example.socialmediaapp.model.Friend
import com.example.socialmediaapp.model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentSearch : Fragment(R.layout.fragment_search) {
    lateinit var binding : FragmentSearchBinding
    lateinit var recyclerView: RecyclerView
    lateinit var friendAdapter: FriendAdapter
    private var friendList= arrayListOf<Friend>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentSearchBinding.bind(view)

        recyclerView=binding.rvSearch
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        friendAdapter= FriendAdapter(friendList,object :FriendAdapter.OnItemClickListener{
            override fun OnItemClick(friend: Friend) {
                var post=Post()
                var action=FragmentSearchDirections.actionNavSearchToNavFriendsProfile(post,friend)
                findNavController().navigate(action)
            }

        })
        recyclerView.adapter=friendAdapter

        binding.etSearchUser.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isEmpty()){
                    recyclerView.visibility=View.GONE
                }
                else{
                    recyclerView.visibility=View.VISIBLE
                    searchUser(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                friendAdapter.notifyDataSetChanged()
                recyclerView.visibility=View.VISIBLE
            }

        })
    }

    private fun searchUser(query: String) {
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("user")
        firebaseDatabase.orderByChild("username")
            .startAt(query)
            .endAt(query + "\uf8ff")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        friendList.clear()
                        for (i in snapshot.children){
                            var user=i.getValue(Friend::class.java)
                            if (user!=null){
                                friendList.add(user)
                            }
                        }
                        friendAdapter.notifyDataSetChanged()
                        if (friendList.isEmpty()){
                            recyclerView.visibility=View.GONE
                        }
                        else{
                            recyclerView.visibility=View.VISIBLE
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}