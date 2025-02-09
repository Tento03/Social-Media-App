package com.example.socialmediaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.ItemPostBinding
import com.example.socialmediaapp.model.Friend
import com.example.socialmediaapp.model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostAdapter(private val dataSet: ArrayList<Post>,private val listener: OnItemClickListener) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    inner class ViewHolder(var binding:ItemPostBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            Glide.with(itemView)
                .load(post.image)
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivPostImageHome)
            binding.tvDescription.text=post.description

            binding.tvUserNameSearch.setOnClickListener(){
                var position=adapterPosition
                if (position!=RecyclerView.NO_POSITION){
                    var user=dataSet[position]
                    user?.let {
                        listener.OnItemClick(user)
                    }
                }
            }

            var firebaseDatabase=FirebaseDatabase.getInstance().getReference("user")
            firebaseDatabase.child(post.publisher).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var user=snapshot.getValue(Friend::class.java)
                        if (user != null) {
                            Glide.with(itemView)
                                .load(user.username)
                                .error(R.drawable.ic_error)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(binding.userProfileImageSearch)
                            binding.tvUserNameSearch.text=user.username
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding=ItemPostBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        var post=dataSet[position]
        viewHolder.bind(post)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    interface OnItemClickListener{
        fun OnItemClick(post: Post)
    }

}