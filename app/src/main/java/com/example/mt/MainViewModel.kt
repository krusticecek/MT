package com.example.mt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: CatFactRepository) : ViewModel() {
    val catFact = MutableLiveData<String>()

    fun getNewFact() {
        viewModelScope.launch(Dispatchers.IO) {
            val newFact = repository.getRandomCatFact()
            catFact.postValue(newFact.fact)
        }
    }

    fun saveFactAsLiked(fact: String) {
        repository.saveFactAsLiked(fact)
    }
}
