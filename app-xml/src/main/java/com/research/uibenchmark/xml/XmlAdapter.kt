package com.research.uibenchmark.xml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.research.uibenchmark.xml.model.Item
import com.research.uibenchmark.xml.databinding.ItemXmlBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class XmlAdapter(
    private val onItemClick: (Item) -> Unit
) : ListAdapter<Item, XmlAdapter.ItemViewHolder>(ItemDiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemXmlBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemXmlBinding) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }
        
        fun bind(item: Item) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
            binding.tvTimestamp.text = dateFormat.format(Date(item.timestamp))
            
            // Здесь должна быть загрузка изображения с помощью библиотеки типа Glide/Coil
            // Для простоты этот код опущен
        }
    }
    
    private class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}
