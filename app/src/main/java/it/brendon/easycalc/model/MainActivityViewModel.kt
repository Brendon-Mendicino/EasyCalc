package it.brendon.easycalc.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    private val expressionRawString: MutableState<String> = mutableStateOf("")

}