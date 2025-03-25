//package com.research.uibenchmark.compose
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.viewModels
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.ExperimentalMaterialApi
//import androidx.compose.material.FloatingActionButton
//import androidx.compose.material.Icon
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Scaffold
//import androidx.compose.material.Surface
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.pullrefresh.PullRefreshIndicator
//import androidx.compose.material.pullrefresh.pullRefresh
//import androidx.compose.material.pullrefresh.rememberPullRefreshState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import com.research.uibenchmark.benchmark.BenchmarkUtils
//import com.research.uibenchmark.model.Item
//import com.research.uibenchmark.viewmodel.ItemViewModel
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//
//class ComposeActivity : ComponentActivity() {
//    private val viewModel: ItemViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Измеряем время инициализации UI
//        BenchmarkUtils.measureInitTime("Compose") {
//            setContent {
//                MaterialTheme {
//                    Surface(color = MaterialTheme.colors.background) {
//                        ItemListScreen(viewModel)
//                    }
//                }
//            }
//        }
//
//        // Измеряем использование памяти
//        BenchmarkUtils.measureMemoryUsage("Compose")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        viewModel.loadItems()
//    }
//
//    // Измерение производительности прокрутки
//    fun measureScrollPerformance() {
//        BenchmarkUtils.measureScrollPerformance("Compose", 60) {
//            // Compose имеет свой механизм прокрутки,
//            // здесь нужно будет использовать специальные инструменты для тестирования Compose
//            Thread.sleep(16) // 60 FPS target
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun ItemListScreen(viewModel: ItemViewModel) {
//    val items by viewModel.items.collectAsState(initial = emptyList())
//    val isLoading by viewModel.loading.collectAsState(initial = false)
//    val scope = rememberCoroutineScope()
//
//    val pullRefreshState = rememberPullRefreshState(
//        refreshing = isLoading,
//        onRefresh = { viewModel.loadItems() }
//    )
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    // Добавление нового элемента
//                    val newItem = Item(
//                        id = System.currentTimeMillis(),
//                        title = "New Item ${System.currentTimeMillis()}",
//                        description = "Description for new test item",
//                        imageUrl = null,
//                        timestamp = System.currentTimeMillis()
//                    )
//                    viewModel.createItem(newItem)
//                }
//            ) {
//                Icon(Icons.Filled.Add, contentDescription = "Add")
//            }
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .pullRefresh(pullRefreshState)
//        ) {
//            // Измеряем время обновления UI
//            LaunchedEffect(items) {
//                BenchmarkUtils.measureUpdateTime("Compose") {
//                    // Данные обновлены
//                }
//            }
//
//            ItemList(
//                items = items,
//                onItemClick = { /* Обработка клика */ }
//            )
//
//            PullRefreshIndicator(
//                refreshing = isLoading,
//                state = pullRefreshState,
//                modifier = Modifier.align(Alignment.TopCenter)
//            )
//        }
//    }
//}
