package com.example.socialmediaapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapter.UserAdapter
import com.example.socialmediaapp.auth.LoginActivity
import com.example.socialmediaapp.databinding.ActivityUserBinding
import com.example.socialmediaapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserActivity : AppCompatActivity() {
    companion object{
        const val ID="ID"
    }
    lateinit var binding: ActivityUserBinding
    lateinit var recyclerView: RecyclerView
    lateinit var userAdapter: UserAdapter
    private val userList= arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView=binding.userRecyclerView
        recyclerView.layoutManager= LinearLayoutManager(this)
        userAdapter= UserAdapter(userList,object :UserAdapter.OnItemClickListener{
            override fun OnItemClick(user: User) {
                var intent= Intent(this@UserActivity,ChatActivity::class.java)
                intent.putExtra(ID,user.uid)
                startActivity(intent)
            }
        })
        recyclerView.adapter=userAdapter

        binding.logout.setOnClickListener(){
            var firebaseAuth= FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            var intent=Intent(this@UserActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        var userRef= FirebaseDatabase.getInstance().getReference("user").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        var user=i.getValue(User::class.java)
                        if (user!=null && user.uid!=firebaseAuth){
                            userList.add(user)
                        }
                        userAdapter.notifyDataSetChanged()
                    }

                    for (i in snapshot.children){
                        var user=i.getValue(User::class.java)
                        if (user!=null && user.uid==firebaseAuth){
                            Glide.with(this@UserActivity)
                                .load(user.image)
                                .error(R.drawable.ic_error)
                                .into(binding.imgProfile)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}