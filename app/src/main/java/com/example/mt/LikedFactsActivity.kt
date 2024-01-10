package com.example.mt

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LikedFactsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_facts)

        val tvLikedFacts = findViewById<TextView>(R.id.tvLikedFacts)
        val repository = CatFactRepository(this)
        val likedFacts = repository.getLikedFacts().joinToString("\n\n")

        tvLikedFacts.text = likedFacts
    }
}
