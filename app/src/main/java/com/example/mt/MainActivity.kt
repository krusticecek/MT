package com.example.mt

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)

        val tvCatFact = findViewById<TextView>(R.id.tvCatFact)
        val btnNewFact = findViewById<Button>(R.id.btnNewFact)

        viewModel.catFact.observe(this) { fact ->
            // Zde se aktualizuje text ve TextView novým faktem
            tvCatFact.text = fact
        }

        btnNewFact.setOnClickListener {
            // Po kliknutí na tlačítko se zavolá tato metoda ve ViewModel
            viewModel.getNewFact()
        }
    }
}

class MainViewModel : ViewModel() {
    val catFact = MutableLiveData<String>()

    fun getNewFact() {
        // Použití viewModelScope pro spuštění kódu v coroutine
        viewModelScope.launch(Dispatchers.IO) {
            // Asynchronní volání Retrofitu
            CatFactRepository.getRandomCatFact().enqueue(object : Callback<CatFact> {
                override fun onResponse(call: Call<CatFact>, response: Response<CatFact>) {
                    if (response.isSuccessful) {
                        // Příspěvek do LiveData, který bude pozorován v MainActivity
                        catFact.postValue(response.body()?.fact)
                    } else {
                        catFact.postValue("Failed to load fact: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<CatFact>, t: Throwable) {
                    catFact.postValue("Error: ${t.message}")
                }
            })
        }
    }
}

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

interface CatFactService {
    @GET("fact")
    fun getRandomCatFact(): Call<CatFact>
}

data class CatFact(
    val fact: String,
    val length: Int
)

object CatFactRepository {
    private val catFactService: CatFactService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        catFactService = retrofit.create(CatFactService::class.java)
    }

    fun getRandomCatFact(): Call<CatFact> = catFactService.getRandomCatFact()
}
