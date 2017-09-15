package com.jeffreyfhow.yogaflashcards
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import JsonParser
import android.support.v7.widget.Toolbar
import com.jeffreyfhow.yogaflashcards.JsonToPose.PoseBuilder
import android.view.MenuItem
import android.view.Menu

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

        val myToolbar: Toolbar = findViewById(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
    }

    /**
     * Click handler for 'Okay' button
     */
    fun onTrash(v:View){
        if(deck.isInteractable) deck.trash(true)
    }

    /**
     * Click handler for 'Cancel' button
     */
    fun onKeep(v:View){
        if(deck.isInteractable) deck.keep(true)
    }

    /**
     * Click handler for 'Show' button
     */
    fun onShow(v:View){
        if(deck.isInteractable) deck.reveal()
    }

    /**
     * Attach menu to toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    /**
     * Toolbar Handler
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_help -> {
                // User chose the "Settings" item, show the app settings UI...
                println("Help")
                return true
            }
            R.id.menu_main_info -> {
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                println("Info")
                return true
            }
            else -> {
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
