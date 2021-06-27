package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemViewBinding

class AsteroidAdapter(private val onClickListner: onClickListner) :
    ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val itemView = AsteroidItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AsteroidViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroidItem = getItem(position)
      holder.itemView.setOnClickListener {
          onClickListner.onClick(asteroidItem)
      }
        holder.bind(asteroidItem)
    }


    class AsteroidViewHolder(private var binding: AsteroidItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()

        }

    }
} class onClickListner(val clickListner: (asteroid:Asteroid)->Unit){
    fun onClick(asteroid:Asteroid)=clickListner(asteroid)
}


object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }



}