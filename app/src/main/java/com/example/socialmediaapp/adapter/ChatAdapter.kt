package com.example.socialmediaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.databinding.ItemLeftBinding
import com.example.socialmediaapp.databinding.ItemRightBinding
import com.example.socialmediaapp.model.Chat
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(private val dataSet: ArrayList<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    private val MESSAGE_TYPE_RIGHT=1
    private val MESSAGE_TYPE_LEFT=0

    inner class LeftViewHolder(var binding:ItemLeftBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(chat: Chat){
            binding.tvMessage.text=chat.message
        }
    }
    inner class RightViewHolder(var binding:ItemRightBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(chat: Chat){
            binding.tvMessage.text=chat.message
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Create a new view, which defines the UI of the list item
        return when (viewType){
            MESSAGE_TYPE_RIGHT ->{
                var binding=ItemRightBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
                return RightViewHolder(binding)
            }
            else ->{
                var binding=ItemLeftBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
                return LeftViewHolder(binding)
            }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        var chat=dataSet[position]
        when (viewHolder){
            is RightViewHolder ->{
                viewHolder.bind(chat)
            }
             is LeftViewHolder ->{
                viewHolder.bind(chat)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    override fun getItemViewType(position: Int): Int {
        var firebaseAuth=FirebaseAuth.getInstance().currentUser?.uid
        if (firebaseAuth==dataSet[position].senderId){
            return MESSAGE_TYPE_RIGHT
        }
        else{
            return MESSAGE_TYPE_LEFT
        }
    }

}