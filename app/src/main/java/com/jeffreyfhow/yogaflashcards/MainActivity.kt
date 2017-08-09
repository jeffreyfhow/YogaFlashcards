package com.jeffreyfhow.yogaflashcards

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import JsonParser
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var deck: Deck

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create Deck with Json Data
        val jsonObject = JsonParser().jsonFileToJsonArr("/res/raw/yoga_data_test.json")
        deck = Deck(this, PoseBuilder().createPoses(jsonObject))
    }

    fun onTrash(v:View){
        if(deck.isInteractable) deck.trash(true)
    }

    fun onKeep(v:View){
        if(deck.isInteractable) deck.keep(true)
    }
}
