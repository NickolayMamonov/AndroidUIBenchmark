//package com.research.uibenchmark.bdui
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import com.research.uibenchmark.bdui.renderer.BDUIScreen
//
///**
// * Основной экран приложения BDUI
// */
//@Composable
//fun BduiScreen(viewModel: BduiStateManager) {
//    val state by viewModel.state.collectAsState()
//    val context = LocalContext.current
//
//    when (val currentState = state) {
//        is BduiState.Loading -> {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//
//        is BduiState.Success -> {
//            BDUIScreen(
//                screen = currentState.screen,
//                onNavigate = { url -> viewModel.navigate(url) },
//                onApiCall = { url, params -> viewModel.handleApiCall(url, params) }
//            )
//        }
//
//        is BduiState.Error -> {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "Ошибка: ${currentState.message}",
//                    color = Color.Red,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
//}
