package com.dicoding.submissionone.ui.detail.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submissionone.data.UserResponse
import com.dicoding.submissionone.databinding.ItemRowUserBinding

class FollowAdapter(
    private var listUser: List<UserResponse>
) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val (login, avatar_url) = listUser[position]

        Glide.with(viewHolder.itemView.context)
            .load(avatar_url) // URL Gambar
            .apply(RequestOptions())
            .into(viewHolder.binding.imgItemPhoto) // imageView mana yang akan diterapkan

        viewHolder.binding.tvItemUsername.text = login
    }

    override fun getItemCount(): Int = listUser.size

    class ViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)
}