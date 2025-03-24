package com.research.uibenchmark.compose

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.research.uibenchmark.compose.benchmark.BenchmarkUtils
import com.research.uibenchmark.compose.network.RetrofitClient
import com.research.uibenchmark.compose.ui.ItemList
import com.research.uibenchmark.compose.viewmodel.ItemViewModel
import com.research.uibenchmark.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComposeActivity : ComponentActivity() {
    private val viewModel: ItemViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Измеряем время инициализации UI
        BenchmarkUtils.measureInitTime("Compose") {
            setContent {
                MaterialTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        BenchmarkApp(
                            viewModel = viewModel,
                            onViewItems = { loadItemsScreen() },
                            onCreateItem = { loadCreateItemScreen() },
                            onRunBenchmark = { loadBenchmarkScreen() },
                            onItemClick = { item -> loadItemDetailScreen(item.id) }
                        )
                    }
                }
            }
        }
        
        // Загружаем главный экран
        loadMainScreen()
        
        // Измеряем использование памяти
        BenchmarkUtils.measureMemoryUsage("Compose")
    }
    
    private fun loadMainScreen() {
        Log.d("Compose", "Loading main screen...")
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getMainScreenUI()
                }
                
                Log.d("Compose", "Server response: ${response.code()}")
                if (response.isSuccessful && response.body() != null) {
                    Log.d("Compose", "Response body: ${response.body()!!}")
                    // В Compose подходе мы уже имеем UI, не нужно ничего рендерить
                    // Загружаем данные для ItemList
                    viewModel.loadItems()
                } else {
                    Log.e("Compose", "Error loading UI: ${response.code()}, ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@ComposeActivity,
                        "Ошибка загрузки UI: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("Compose", "Exception while loading main screen", e)
                Toast.makeText(
                    this@ComposeActivity,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun loadItemsScreen() {
        Log.d("Compose", "Loading items screen...")
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getItemsScreenUI()
                }
                
                if (response.isSuccessful && response.body() != null) {
                    Log.d("Compose", "Items screen response successful")
                    // Загружаем данные для RecyclerView
                    viewModel.loadItems()
                } else {
                    Log.e("Compose", "Error loading items UI: ${response.code()}, ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@ComposeActivity,
                        "Ошибка загрузки UI: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ComposeActivity,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun loadItemDetailScreen(id: Long) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getItemDetailScreenUI(id)
                }
                
                if (response.isSuccessful && response.body() != null) {
                    // В Compose подходе мы бы открыли новый экран
                    // Но здесь просто покажем сообщение для демонстрации
                    Toast.makeText(
                        this@ComposeActivity,
                        "Загружены детали для элемента $id",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@ComposeActivity,
                        "Ошибка загрузки UI: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ComposeActivity,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun loadCreateItemScreen() {
        Log.d("Compose", "Loading create item screen...")
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getCreateItemScreenUI()
                }
                
                if (response.isSuccessful && response.body() != null) {
                    // В Compose подходе мы бы открыли новый экран
                    // Но здесь создадим новый элемент для демонстрации
                    val newItem = Item(
                        id = System.currentTimeMillis(),
                        title = "New Item ${System.currentTimeMillis()}",
                        description = "Description for new test item",
                        imageUrl = null,
                        timestamp = System.currentTimeMillis()
                    )
                    viewModel.createItem(newItem)
                } else {
                    Toast.makeText(
                        this@ComposeActivity,
                        "Ошибка загрузки UI: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ComposeActivity,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun loadBenchmarkScreen() {
        Log.d("Compose", "Loading benchmark screen...")
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getBenchmarkUI()
                }
                
                if (response.isSuccessful && response.body() != null) {
                    // Измеряем время обновления UI
                    BenchmarkUtils.measureUpdateTime("Compose") {
                        // Здесь бы обновили UI в соответствии с полученными данными
                        // Но для простоты просто перезагрузим элементы
                        viewModel.loadItems()
                    }
                } else {
                    Toast.makeText(
                        this@ComposeActivity,
                        "Ошибка загрузки UI: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ComposeActivity,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    // Измерение производительности прокрутки
    fun measureScrollPerformance() {
        BenchmarkUtils.measureScrollPerformance("Compose", 60) {
            // Compose имеет свой механизм прокрутки, 
            // здесь нужно будет использовать специальные инструменты для тестирования Compose
            Thread.sleep(16) // 60 FPS target
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BenchmarkApp(
    viewModel: ItemViewModel,
    onViewItems: () -> Unit,
    onCreateItem: () -> Unit,
    onRunBenchmark: () -> Unit,
    onItemClick: (Item) -> Unit
) {
    val items by viewModel.items.collectAsState(initial = emptyList())
    val isLoading by viewModel.loading.collectAsState(initial = false)
    val scope = rememberCoroutineScope()
    
    // Состояние для обновления интерфейса по свайпу вниз
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = { onViewItems() }
    )
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateItem() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Верхние кнопки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Button(
                    onClick = { onViewItems() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("ПРОСМОТР ЭЛЕМЕНТОВ")
                }
                
                Button(
                    onClick = { onCreateItem() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                ) {
                    Text("СОЗДАТЬ ЭЛЕМЕНТ")
                }
                
                Button(
                    onClick = { onRunBenchmark() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFFF5722)
                    )
                ) {
                    Text("ЗАПУСТИТЬ БЕНЧМАРК")
                }
            }
            
            // Список с поддержкой свайпа для обновления
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                // Измеряем время обновления UI
                LaunchedEffect(items) {
                    BenchmarkUtils.measureUpdateTime("Compose") {
                        // Данные обновлены
                    }
                }
                
                ItemList(
                    items = items,
                    onItemClick = { item -> onItemClick(item.id) }
                )
                
                PullRefreshIndicator(
                    refreshing = isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}
