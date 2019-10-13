package com.lexaloris.rickandmorty.presentation.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.lexaloris.rickandmorty.R
import com.lexaloris.rickandmorty.data.response.Character

class CharacterItemViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {
    private val avatarView = parentView.findViewById<ImageView>(R.id.character_avatar)
    private val nameView = parentView.findViewById<TextView>(R.id.character_name)
    private val idView = parentView.findViewById<TextView>(R.id.character_id)

    fun bind(character: Character) {
        loadImage(character.imageUrl)
        nameView.text = character.name
        idView.text = itemView.context.getString(R.string.character_id, character.id)
    }

    private fun loadImage(url: String) {
        Glide.with(itemView.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions.circleCropTransform())
            .into(avatarView)
    }
}
