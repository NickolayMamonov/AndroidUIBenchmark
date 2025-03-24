package com.research.uibenchmark.xml

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.research.uibenchmark.benchmark.BenchmarkUtils
import com.research.uibenchmark.databinding.ActivityXmlBinding
import com.research.uibenchmark.model.Item
import com.research.uibenchmark.viewmodel.ItemViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        
        // Измеряем использование памяти
        BenchmarkUtils.measureMemoryUsage("XML")
    }
    
    private fun setupRecyclerView() {
        adapter = XmlAdapter(emptyList()) { item ->
            // Обработка клика по элементу
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
    
    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collectLatest { items ->
                    BenchmarkUtils.measureUpdateTime("XML") {
                        adapter.updateItems(items)
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
            viewModel.loadItems()
        }
        
        binding.fab.setOnClickListener {
            // Добавление нового элемента для тестирования обновления UI
            val newItem = Item(
                id = System.currentTimeMillis(),
                title = "New Item ${System.currentTimeMillis()}",
                description = "Description for new test item",
                imageUrl = null,
                timestamp = System.currentTimeMillis()
            )
            viewModel.createItem(newItem)
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.loadItems()
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
