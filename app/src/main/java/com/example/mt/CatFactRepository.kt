package com.example.mt

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException


class CatFactRepository(private val context: Context) {
    private val sharedPref = context.getSharedPreferences("CatFacts", Context.MODE_PRIVATE)

    fun getRandomCatFact(): CatFact {
        return try {
            val jsonString = context.assets.open("cat_facts.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<CatFacts>() {}.type
            val catFacts = Gson().fromJson<CatFacts>(jsonString, type)
            catFacts.facts.random()
        } catch (e: IOException) {
            e.printStackTrace()
            CatFact("Failed to load fact") // Now matches the constructor of CatFact
        }
    }

    fun saveFactAsLiked(fact: String) {
        val likedFacts = sharedPref.getStringSet("LikedFacts", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        likedFacts.add(fact)
        with(sharedPref.edit()) {
            putStringSet("LikedFacts", likedFacts)
            apply()
        }
    }

    fun getLikedFacts(): Set<String> {
        return sharedPref.getStringSet("LikedFacts", setOf()) ?: setOf()
    }
}
