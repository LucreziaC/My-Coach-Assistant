package com.lucreziacarena.mycoachassistant

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucreziacarena.mycoachassistant.repository.Repository
import com.lucreziacarena.mycoachassistant.repository.models.AthleteModel
import com.lucreziacarena.mycoachassistant.repository.results.AthletesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelTest @Inject constructor(
    val repository: Repository
): ViewModel() {

    private val _state = mutableStateOf(PostsState())
    val state: State<PostsState> = _state

    init {
        viewModelScope.launch {
            val result = repository.getAthelticsList()
            println(result)
            //_state.value = PostsState(posts = result, false, null)
        }}

    }

data class PostsState(
    val posts: List<AthleteModel>? = null,
    val loading: Boolean = false,
    val error: String? = null
)

