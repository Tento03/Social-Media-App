package com.example.socialmediaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.ItemUserBinding
import com.example.socialmediaapp.model.Friend

class FriendAdapter(private val dataSet: ArrayList<Friend>,private val listener: OnItemClickListener) :
    RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    inner class ViewHolder(var binding:ItemUserBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(friend: Friend){
            Glide.with(itemView)
                .load(friend.image)
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.userProfileImage)
            binding.tvUsernameUser.text=friend.username

            binding.root.setOnClickListener(){
                var position=adapterPosition
                if (position!=RecyclerView.NO_POSITION){
                    var user=dataSet[position]
                    if (user!=null){
                        listener.OnItemClick(friend)
                    }
                }
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
       val binding=ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        var friend=dataSet[position]
        viewHolder.bind(friend)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    interface OnItemClickListener{
        fun OnItemClick(friend: Friend)
    }

}