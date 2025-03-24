package com.research.uibenchmark.bdui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.research.uibenchmark.bdui.model.Screen
import com.research.uibenchmark.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Состояние экрана BDUI
 */
sealed class BduiState {
    object Loading : BduiState()
    data class Success(val screen: Screen) : BduiState()
    data class Error(val message: String) : BduiState()
}

/**
 * Менеджер состояния для BDUI приложения
 */
class BduiStateManager(
    private val itemRepository: (suspend () -> List<Item>),
    private val addItemCallback: (Item) -> Unit,
    private val toastCallback: (String) -> Unit
) : ViewModel() {
    private val TAG = "BduiStateManager"
    
    // Текущее состояние UI
    private val _state = MutableStateFlow<BduiState>(BduiState.Loading)
    val state: StateFlow<BduiState> = _state
    
    // История навигации для возможности возврата назад
    private val navigationHistory = mutableListOf<String>()
    
    // Данные
    private var currentScreenId: String = "main"
    private var currentItems: List<Item> = emptyList()
    
    init {
        loadMainScreen()
    }
    
    /**
     * Загрузка главного экрана
     */
    fun loadMainScreen() {
        Log.d(TAG, "Loading main screen")
        _state.update { BduiState.Loading }
        
        viewModelScope.launch {
            try {
                // Загружаем элементы
                currentItems = itemRepository()
                
                // Генерируем экран через BDUI сервис (имитация сервера)
                val screen = BduiService.getMainScreen(currentItems)
                
                // Обновляем состояние
                _state.update { BduiState.Success(screen) }
                
                // Сбрасываем историю навигации
                navigationHistory.clear()
                navigationHistory.add("main")
                currentScreenId = "main"
                
                Log.d(TAG, "Main screen loaded with ${currentItems.size} items")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading main screen", e)
                _state.update { BduiState.Error("Ошибка загрузки экрана: ${e.message}") }
            }
        }
    }
    
    /**
     * Обработка API-вызовов от UI-компонентов
     */
    fun handleApiCall(url: String, params: Map<String, String>?) {
        Log.d(TAG, "API call: $url, params: $params")
        
        when (url) {
            "create_item" -> createNewItem()
            "item_click" -> showItemDetails(params)
            "refresh" -> loadMainScreen()
            else -> {
                Log.w(TAG, "Unknown API endpoint: $url")
                toastCallback("Неизвестный API endpoint: $url")
            }
        }
    }
    
    /**
     * Обработка навигации
     */
    fun navigate(url: String) {
        Log.d(TAG, "Navigate to: $url")
        
        when {
            url == "/main" -> {
                loadMainScreen()
            }
            url.startsWith("/detail/") -> {
                val itemId = url.substringAfterLast("/").toLongOrNull()
                if (itemId != null) {
                    navigateToItemDetail(itemId)
                } else {
                    Log.e(TAG, "Invalid item ID in URL: $url")
                    toastCallback("Неверный ID элемента")
                }
            }
            else -> {
                Log.w(TAG, "Unknown navigation URL: $url")
                toastCallback("Неизвестный URL: $url")
            }
        }
    }
    
    /**
     * Навигация к детальному экрану элемента
     */
    private fun navigateToItemDetail(itemId: Long) {
        _state.update { BduiState.Loading }
        
        viewModelScope.launch {
            try {
                // Генерируем детальный экран через BDUI сервис
                val screen = BduiService.getDetailScreen(itemId)
                
                // Обновляем состояние
                _state.update { BduiState.Success(screen) }
                
                // Добавляем в историю навигации
                navigationHistory.add("detail/$itemId")
                currentScreenId = "detail/$itemId"
                
                Log.d(TAG, "Detail screen loaded for item $itemId")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading detail screen for item $itemId", e)
                _state.update { BduiState.Error("Ошибка загрузки детального экрана: ${e.message}") }
            }
        }
    }
    
    /**
     * Создание нового элемента
     */
    private fun createNewItem() {
        Log.d(TAG, "Creating new item")
        
        val newItem = Item(
            id = System.currentTimeMillis(),
            title = "New Item ${System.currentTimeMillis()}",
            description = "Description for new test item",
            imageUrl = null,
            timestamp = System.currentTimeMillis()
        )
        
        // Добавляем элемент через колбэк
        addItemCallback(newItem)
        
        // Показываем сообщение
        toastCallback("Новый элемент добавлен")
        
        // Обновляем список с уже добавленным элементом (через колбэк он уже добавлен в репозиторий)
        loadMainScreen()
    }
    
    /**
     * Показ деталей элемента
     */
    private fun showItemDetails(params: Map<String, String>?) {
        val itemId = params?.get("item_id")?.toLongOrNull()
        if (itemId != null) {
            Log.d(TAG, "Showing details for item $itemId")
            navigateToItemDetail(itemId)
        } else {
            Log.e(TAG, "Item ID not provided or invalid")
            toastCallback("ID элемента не указан или некорректен")
        }
    }
    
    /**
     * Возврат назад
     * @return true если обработано, false если нет истории
     */
    fun navigateBack(): Boolean {
        if (navigationHistory.size <= 1) {
            // Нет предыдущих экранов
            return false
        }
        
        // Удаляем текущий экран
        navigationHistory.removeAt(navigationHistory.lastIndex)
        
        // Загружаем предыдущий
        val previous = navigationHistory.last()
        
        Log.d(TAG, "Navigating back to $previous")
        
        when {
            previous == "main" -> loadMainScreen()
            previous.startsWith("detail/") -> {
                val itemId = previous.substringAfter("detail/").toLongOrNull()
                if (itemId != null) {
                    navigateToItemDetail(itemId)
                } else {
                    loadMainScreen()
                }
            }
            else -> loadMainScreen()
        }
        
        return true
    }
    
    /**
     * Обновление состояния при изменении данных
     */
    fun updateItems(items: List<Item>) {
        Log.d(TAG, "Items updated, refreshing current screen")
        currentItems = items
        
        // Обновляем текущий экран с новыми данными
        when (currentScreenId) {
            "main" -> loadMainScreen()
            else -> {
                // Если мы на детальном экране, оставляем как есть
                // В реальном приложении можно было бы обновить данные
            }
        }
    }
}

/**
 * Фабрика для создания BduiStateManager
 */
class BduiStateManagerFactory(
    private val itemRepository: suspend () -> List<Item>,
    private val addItemCallback: (Item) -> Unit,
    private val toastCallback: (String) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BduiStateManager::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BduiStateManager(itemRepository, addItemCallback, toastCallback) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
