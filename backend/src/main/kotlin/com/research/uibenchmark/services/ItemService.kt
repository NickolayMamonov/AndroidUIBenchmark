package com.research.uibenchmark.services

import com.research.uibenchmark.models.Item
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

object ItemService {
    // Используем ConcurrentHashMap для хранения элементов в памяти
    private val items = ConcurrentHashMap<Long, Item>()
    
    // Генератор ID
    private val idCounter = AtomicLong(1)
    
    // Инициализируем некоторые тестовые данные
    init {
        val currentTime = System.currentTimeMillis()
        
        for (i in 1..20) {
            val id = idCounter.getAndIncrement()
            items[id] = Item(
                id = id,
                title = "Item $id",
                description = "This is a description for item $id. It contains some sample text for testing purposes.",
                imageUrl = null,
                timestamp = currentTime - (i * 86400000) // Дни назад
            )
        }
    }
    
    fun getAllItems(): List<Item> {
        return items.values.sortedByDescending { it.timestamp }
    }
    
    fun getItemById(id: Long): Item? {
        return items[id]
    }
    
    fun createItem(item: Item): Item {
        val id = idCounter.getAndIncrement()
        val newItem = item.copy(id = id)
        items[id] = newItem
        return newItem
    }
    
    fun updateItem(id: Long, item: Item): Item? {
        if (!items.containsKey(id)) {
            return null
        }
        
        val updatedItem = item.copy(id = id)
        items[id] = updatedItem
        return updatedItem
    }
    
    fun deleteItem(id: Long): Boolean {
        return items.remove(id) != null
    }
}
