package com.techmobile.artbooktesting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.techmobile.artbooktesting.R
import com.techmobile.artbooktesting.roomdb.Art
import java.util.Objects
import javax.inject.Inject

class ArtRecyclerAdapter @Inject constructor(
    val glide : RequestManager
) : RecyclerView.Adapter<ArtRecyclerAdapter.ArtViewHolder>() {

    private val diffUtil = object : ItemCallback<Art>(){
        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
           return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
           return oldItem == newItem
        }

    }
    private val recyclerViewListDiffer = AsyncListDiffer(this, diffUtil)
    var arts  : List<Art>
    get() = recyclerViewListDiffer.currentList
    set(value) = recyclerViewListDiffer.submitList(value)

    class ArtViewHolder( itemView : View) : RecyclerView.ViewHolder( itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.art_row, parent,false)
        return ArtViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.artRowImageView)
        val nameText = holder.itemView.findViewById<TextView>(R.id.artRowArtNameText)
        val artistNameText = holder.itemView.findViewById<TextView>(R.id.artRowArtistNameText)
        val yearText = holder.itemView.findViewById<TextView>(R.id.artRowYearText)
        val art = arts[position]
        holder.itemView.apply {
            glide.load(art.imageUrl).into(imageView)
            nameText.text = "Name: ${art.name}"
            artistNameText.text = "Artist Name: ${art.artistName}"
            yearText.text = "Year: ${art.year}"
        }
    }

    override fun getItemCount(): Int {
      return arts.size
    }
}