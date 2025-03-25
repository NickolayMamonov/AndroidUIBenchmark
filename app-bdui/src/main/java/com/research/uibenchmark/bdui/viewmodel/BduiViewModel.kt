package com.research.uibenchmark.bdui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.research.uibenchmark.bdui.model.BDUIResponse
import com.research.uibenchmark.bdui.repository.BDUIRepository
import com.research.uibenchmark.bdui.utils.ComponentDebugHelper
import com.research.uibenchmark.bdui.utils.DebugUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "BDUIViewModel"

class BDUIViewModel(
    private val repository: BDUIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    // Текущий загруженный экран
    private var currentScreen: BDUIResponse? = null

    // История навигации для реализации "назад"
    private val navigationHistory = mutableListOf<String>()

    // Загрузка главного экрана при старте
    init {
        loadMainScreen()
    }

    // Функция для загрузки главного экрана
    fun loadMainScreen() {
        Log.d(TAG, "Loading main screen...")
        _uiState.value = UIState.Loading
        viewModelScope.launch {
            repository.getMainScreen().fold(
                onSuccess = { response ->
                    Log.d(TAG, "Main screen loaded successfully")

                    // Добавляем детальное логирование структуры ответа
                    DebugUtils.logSduiResponse(response)

                    // Проверки на null
                    if (response.screen == null) {
                        Log.e(TAG, "Server returned null screen")
                        _uiState.value = UIState.Error("Сервер вернул пустой экран")
                        return@fold
                    }

                    // Проверяем структуру компонентов
                    val isValid = ComponentDebugHelper.validateScreen(response.screen)
                    if (!isValid) {
                        Log.w(TAG, "Component validation failed, but proceeding with rendering")
                        // Мы продолжаем рендеринг даже при ошибках валидации, так как частичный UI лучше, чем ничего
                    }

                    currentScreen = response
                    _uiState.value = UIState.Success(response)
                    navigationHistory.clear()
                    navigationHistory.add("/api/ui/main")
                },
                onFailure = { error ->
                    Log.e(TAG, "Failed to load main screen: ${error.message}", error)
                    _uiState.value =
                        UIState.Error("Не удалось загрузить главный экран: ${error.localizedMessage ?: error.message}")
                }
            )
        }
    }

    // Функция для навигации на другой экран
    fun navigateTo(url: String) {
        Log.d(TAG, "Navigating to: $url")
        if (url == "/api/ui/main") {
            loadMainScreen()
            return
        }

        _uiState.value = UIState.Loading
        viewModelScope.launch {
            val result = when {
                url.startsWith("/api/ui/details/") -> {
                    val id = url.substringAfterLast("/")
                    repository.getDetailScreen(id)
                }

                url == "/api/ui/settings" -> {
                    repository.getSettingsScreen()
                }

                url == "/api/ui/profile" -> {
                    repository.getProfileScreen()
                }

                else -> {
                    Result.failure(Exception("Неизвестный URL: $url"))
                }
            }

            result.fold(
                onSuccess = { response ->
                    Log.d(TAG, "Navigation successful to: $url, screen: ${response.screen?.id}")

                    // Проверки на null
                    if (response.screen == null) {
                        Log.e(TAG, "Server returned null screen for $url")
                        _uiState.value = UIState.Error("Сервер вернул пустой экран для $url")
                        return@fold
                    }

                    // Проверяем структуру компонентов
                    val isValid = ComponentDebugHelper.validateScreen(response.screen)
                    if (!isValid) {
                        Log.w(
                            TAG,
                            "Component validation failed for screen from $url, but proceeding with rendering"
                        )
                    }

                    currentScreen = response
                    _uiState.value = UIState.Success(response)
                    navigationHistory.add(url)
                },
                onFailure = { error ->
                    Log.e(TAG, "Navigation failed to $url: ${error.message}", error)
                    _uiState.value =
                        UIState.Error("Не удалось выполнить навигацию: ${error.localizedMessage ?: error.message}")
                }
            )
        }
    }

    // Функция для API-вызовов
    fun makeApiCall(url: String, payload: Map<String, String>?) {
        if (url.isEmpty()) return
        Log.d(TAG, "Making API call to: $url with payload: $payload")

        viewModelScope.launch {
            _uiState.update { state ->
                if (state is UIState.Success) {
                    state.copy(isLoading = true)
                } else {
                    UIState.Loading
                }
            }

            // Обработка различных API-вызовов
            val result = when {
                url == "toggle" -> {
                    currentScreen?.let { Result.success(it) }
                        ?: Result.failure(Exception("Нет текущего экрана"))
                }

                url.startsWith("/api/action/") -> {
                    currentScreen?.let { Result.success(it) }
                        ?: Result.failure(Exception("Нет текущего экрана"))
                }

                url == "/api/settings/save" ||
                        url == "/api/profile/save" -> {
                    repository.submitData(payload ?: emptyMap())
                }

                else -> {
                    Result.failure(Exception("Неизвестный API-вызов: $url"))
                }
            }

            result.fold(
                onSuccess = { response ->
                    Log.d(TAG, "API call successful to: $url")

                    // Проверки на null
                    if (response.screen == null) {
                        Log.e(TAG, "Server returned null screen for API call to $url")
                        _uiState.value = UIState.Error("Сервер вернул пустой экран")
                        return@fold
                    }

                    // Проверяем структуру компонентов
                    val isValid = ComponentDebugHelper.validateScreen(response.screen)
                    if (!isValid) {
                        Log.w(
                            TAG,
                            "Component validation failed for screen from API call to $url, but proceeding with rendering"
                        )
                    }

                    currentScreen = response
                    _uiState.value = UIState.Success(response)
                },
                onFailure = { error ->
                    Log.e(TAG, "API call failed to $url: ${error.message}", error)
                    _uiState.value =
                        UIState.Error("Не удалось выполнить API-вызов: ${error.localizedMessage ?: error.message}")
                }
            )
        }
    }

    // Функция для обработки нажатия кнопки "назад"
    fun navigateBack(): Boolean {
        if (navigationHistory.size <= 1) {
            return false // Нельзя вернуться назад, уже на главном экране
        }

        // Удаляем текущий URL и переходим к предыдущему
        navigationHistory.removeAt(navigationHistory.lastIndex)
        val previousUrl = navigationHistory.last()

        // Удаляем предыдущий URL из истории, чтобы избежать дублирования
        navigationHistory.removeAt(navigationHistory.lastIndex)

        // Выполняем навигацию на предыдущий URL
        navigateTo(previousUrl)
        return true
    }
}
