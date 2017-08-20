package com.jeffreyfhow.yogaflashcards
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import JsonParser
import com.jeffreyfhow.yogaflashcards.JsonToPose.PoseBuilder

/**
 * Swiping through cards via dragging or buttons
 */
class MainActivity : AppCompatActivity() {

    private lateinit var deck: Deck

    /**
     * Create the deck of Pose Cards from json data
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create Deck with Json Data
        val jsonObject = JsonParser.jsonFileToJsonArr("/res/raw/yoga_data_test.json")
        deck = Deck(this, PoseBuilder.createPoses(jsonObject))
    }

    /**
     * Click handler for 'Trash' button
     */
    fun onTrash(v:View){
        if(deck.isInteractable) deck.trash(true)
    }

    /**
     * Click handler for 'Keep' button
     */
    fun onKeep(v:View){
        if(deck.isInteractable) deck.keep(true)
    }
}
