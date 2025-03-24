package com.research.uibenchmark.xml

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.research.uibenchmark.xml.benchmark.BenchmarkUtils
import com.research.uibenchmark.xml.databinding.ActivityXmlBinding
import com.research.uibenchmark.model.Item
import com.research.uibenchmark.xml.network.RetrofitClient
import com.research.uibenchmark.xml.viewmodel.ItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class XmlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityXmlBinding
    private lateinit var adapter: XmlAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val viewModel: ItemViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Измеряем время инициализации UI
        BenchmarkUtils.measureInitTime("XML") {
            binding = ActivityXmlBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            swipeRefreshLayout = binding.swipeRefresh
            
            setupRecyclerView()
            setupObservers()
            setupListeners()
        }
        
        // Загружаем главный экран
        loadMainScreen()
        
        // Измеряем использование памяти
        BenchmarkUtils.measureMemoryUsage("XML")
    }
    
    private fun setupRecyclerView() {
        adapter = XmlAdapter { item ->
            // Обработка клика по элементу
            loadItemDetailScreen(item.id)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
    
    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collectLatest { items ->
                    BenchmarkUtils.measureUpdateTime("XML") {
                        adapter.submitList(items)
                    }
                }
            }
        }
        
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loading.collectLatest { isLoading ->
                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    binding.swipeRefresh.isRefreshing = isLoading
                }
            }
        }
    }
    
    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            loadItemsScreen()
        }
        
        binding.fab.setOnClickListener {
            loadCreateItemScreen()
        }
        
        binding.btnBenchmark.setOnClickListener {
            loadBenchmarkScreen()
        }
    }
    
    private fun loadMainScreen() {
        lifecycleScope.launch {
            try {
                Log.d("XML", "Loading main screen...")
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getMainScreenUI()
                }
                
                Log.d("XML", "Server response: ${response.code()}")
                if (response.isSuccessful && response.body() != null) {
                    Log.d("XML", "Response body: ${response.body()!!}")
                    // В XML подходе мы уже имеем UI, не нужно ничего рендерить
                    // Загружаем данные для RecyclerView
                    viewModel.loadItems()
                } else {
                    Log.e("XML", "Error loading UI: ${response.code()}, ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@XmlActivity,
                        "Ошибка загрузки UI: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("XML", "Exception while loading main screen", e)
                Toast.makeText(
                    this@XmlActivity,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun loadItemsScreen() {
        Log.d("XML", "Loading items screen...")
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getItemsScreenUI()
                }
                
                if (response.isSuccessful && response.body() != null) {
                    Log.d("XML", "Items screen response successful")
                    // Загружаем данные для RecyclerView
                    viewModel.loadItems()
                } else {
                    Log.e("XML", "Error loading items UI: ${response.code()}, ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@XmlActivity,
                        "Ошибка загрузки UI: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@XmlActivity,
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
                    // В XML подходе мы бы открыли новую Activity или Fragment
                    // Но здесь просто покажем сообщение для демонстрации
                    Toast.makeText(
                        this@XmlActivity,
                        "Загружены детали для элемента $id",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@XmlActivity,
                        "Ошибка загрузки UI: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@XmlActivity,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun loadCreateItemScreen() {
        Log.d("XML", "Loading create item screen...")
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getCreateItemScreenUI()
                }
                
                if (response.isSuccessful && response.body() != null) {
                    // В XML подходе мы бы открыли новую Activity или Fragment
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
                        this@XmlActivity,
                        "Ошибка загрузки UI: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@XmlActivity,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun loadBenchmarkScreen() {
        Log.d("XML", "Loading benchmark screen...")
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getBenchmarkUI()
                }
                
                if (response.isSuccessful && response.body() != null) {
                    // Измеряем время обновления UI
                    BenchmarkUtils.measureUpdateTime("XML") {
                        // Здесь бы обновили UI в соответствии с полученными данными
                        // Но для простоты просто перезагрузим элементы
                        viewModel.loadItems()
                    }
                } else {
                    Toast.makeText(
                        this@XmlActivity,
                        "Ошибка загрузки UI: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@XmlActivity,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    // Измерение производительности прокрутки
    fun measureScrollPerformance() {
        BenchmarkUtils.measureScrollPerformance("XML", 60) {
            // Симуляция прокрутки
            runOnUiThread {
                binding.recyclerView.smoothScrollBy(0, 10)
            }
            Thread.sleep(16) // 60 FPS target
        }
    }
}
