package com.research.uibenchmark.xml.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.research.uibenchmark.xml.model.Item
import com.research.uibenchmark.xml.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    private val repository = ItemRepository()
    
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadItems() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repository.getItems()
                _items.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun createItem(item: Item) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.createItem(item)
                loadItems() // перезагрузка списка
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun updateItem(id: Long, item: Item) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.updateItem(id, item)
                loadItems() // перезагрузка списка
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun deleteItem(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.deleteItem(id)
                loadItems() // перезагрузка списка
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
