package com.udacity.asteroidradar.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.FragmentItemAsteroidBinding
import com.udacity.asteroidradar.models.Asteroid

class AsteroidAdapter(val clickListener: AsteroidListener) : ListAdapter<Asteroid, AsteroidViewHolder>(AsteroidDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = getItem(position) as Asteroid
        holder.bind(clickListener, item)
    }
}

class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }

}

class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}

class AsteroidViewHolder private constructor(val binding: FragmentItemAsteroidBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: AsteroidListener, item: Asteroid) {
        binding.asteroid = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AsteroidViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentItemAsteroidBinding.inflate(layoutInflater, parent, false)

            return AsteroidViewHolder(binding)
        }
    }
}
