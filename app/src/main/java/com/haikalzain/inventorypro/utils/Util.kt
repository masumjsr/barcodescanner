package com.haikalzain.inventorypro.utils

sealed class ApiState {
    class Success(val data: Any) : ApiState()
    class Failure(val msg: String) : ApiState()
    data object Loading:ApiState()
    data object Empty: ApiState()
}