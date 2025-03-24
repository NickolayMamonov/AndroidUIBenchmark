package com.research.uibenchmark.bdui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.research.uibenchmark.bdui.ui.renders.SDUIScreen
import com.research.uibenchmark.bdui.viewmodel.BDUIViewModel
import com.research.uibenchmark.bdui.viewmodel.UIState

private const val TAG = "SuccessScreen"

@Composable
fun SuccessScreen(viewModel: BDUIViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val state = uiState) {
            is UIState.Loading -> {
                Log.d(TAG, "Showing loading state")
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
            is UIState.Success -> {
                Log.d(TAG, "Success state with screen: ${state.response.screen?.id}")

                // Проверка на null
                if (state.response.screen == null) {
                    ErrorScreen(
                        message = "Получен пустой экран от сервера",
                        onRetry = { viewModel.loadMainScreen() }
                    )
                    return@Surface
                }

                if (state.isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SDUIScreen(
                            screen = state.response.screen,
                            onNavigate = { url -> viewModel.navigateTo(url) },
                            onApiCall = { url, payload -> viewModel.makeApiCall(url, payload) }
                        )

                        // Полупрозрачный индикатор загрузки поверх экрана
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    SDUIScreen(
                        screen = state.response.screen,
                        onNavigate = { url -> viewModel.navigateTo(url) },
                        onApiCall = { url, payload -> viewModel.makeApiCall(url, payload) }
                    )
                }
            }
            is UIState.Error -> {
                Log.e(TAG, "Error state: ${state.message}")
                ErrorScreen(
                    message = state.message,
                    onRetry = { viewModel.loadMainScreen() }
                )
            }
        }
    }
}