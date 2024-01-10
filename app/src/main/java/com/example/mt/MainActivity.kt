package com.example.mt

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.content.Intent


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, MainViewModelFactory(applicationContext)).get(MainViewModel::class.java)

        val tvCatFact = findViewById<TextView>(R.id.tvCatFact)
        val btnNewFact = findViewById<Button>(R.id.btnNewFact)
        val btnLikedFacts = findViewById<Button>(R.id.btnLikedFacts)

        viewModel.catFact.observe(this) { fact ->
            tvCatFact.text = fact
            tvCatFact.setOnClickListener {
                viewModel.saveFactAsLiked(fact)
            }
        }

        btnNewFact.setOnClickListener {
            viewModel.getNewFact()
        }

        btnLikedFacts.setOnClickListener {
            startActivity(Intent(this, LikedFactsActivity::class.java))
        }
    }
}
