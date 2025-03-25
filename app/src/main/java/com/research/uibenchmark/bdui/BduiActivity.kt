//package com.research.uibenchmark.bdui
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.compose.setContent
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.ui.Modifier
//import com.research.uibenchmark.benchmark.BenchmarkUtils
//import com.research.uibenchmark.model.Item
//import com.research.uibenchmark.viewmodel.ItemViewModel
//
//class BduiActivity : AppCompatActivity() {
//    // ViewModel для данных
//    private val itemViewModel: ItemViewModel by viewModels()
//
//    // BDUI StateManager
//    private val bduiViewModel: BduiStateManager by viewModels {
//        BduiStateManagerFactory(
//            itemRepository = { itemViewModel.getItems() },
//            addItemCallback = { itemViewModel.createItem(it) },
//            toastCallback = { message ->
//                Toast.makeText(this@BduiActivity, message, Toast.LENGTH_SHORT).show()
//            }
//        )
//    }
//
//    @SuppressLint("BackHandler")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        supportActionBar?.hide() // Скрываем заголовок для соответствия Compose версии
//
//        // Измеряем время инициализации UI
//        BenchmarkUtils.measureInitTime("BDUI") {
//            // Инициализация BDUI с использованием Compose для рендеринга
//            setContent {
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    // Отображаем основной экран BDUI
//                    BduiScreen(viewModel = bduiViewModel)
//                }
//            }
//        }
//
//        // Измеряем использование памяти
//        BenchmarkUtils.measureMemoryUsage("BDUI")
//
//        // Загружаем элементы при старте
//        itemViewModel.loadItems()
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        // Перехватываем нажатие кнопки Назад для реализации навигации BDUI
//        if (!bduiViewModel.navigateBack()) {
//            @Suppress("DEPRECATION")
//            super.onBackPressed()
//        }
//    }
//
//    // Измерение производительности прокрутки
//    fun measureScrollPerformance() {
//        BenchmarkUtils.measureScrollPerformance("BDUI", 60) {
//            // Симуляция прокрутки
//            Thread.sleep(16) // 60 FPS target
//        }
//    }
//}
