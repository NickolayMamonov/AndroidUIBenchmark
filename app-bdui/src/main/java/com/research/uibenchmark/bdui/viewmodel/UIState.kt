package com.research.uibenchmark.bdui.viewmodel

import com.research.uibenchmark.bdui.model.BDUIResponse

sealed class UIState {
    object Loading : UIState()
    data class Success(
        val response: BDUIResponse,
        val isLoading: Boolean = false
    ) : UIState()
    data class Error(val message: String) : UIState()
}