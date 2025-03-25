package com.research.uibenchmark.bdui.viewmodel

import com.research.uibenchmark.bdui.model.BDUIResponse

/**
 * Состояния UI для отображения экранов BDUI
 */
sealed class UIState {
    object Loading : UIState()
    data class Success(val data: BDUIResponse, val isLoading: Boolean = false) : UIState()
    data class Error(val message: String) : UIState()
}
