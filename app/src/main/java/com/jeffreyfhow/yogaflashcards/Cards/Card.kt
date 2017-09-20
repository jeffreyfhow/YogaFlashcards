package com.jeffreyfhow.yogaflashcards.Cards
import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jeffreyfhow.yogaflashcards.Pose.Pose
import com.jeffreyfhow.yogaflashcards.R
import org.w3c.dom.Text

/**
 * Created by jeffreyhow on 8/6/17.
 * Superclass for Top & Bottom Cards
 */
open class Card : CardView
{
    /**
     * Initialization
     */
    lateinit var pose : Pose
    private var imgView: ImageView? = null
    private var engView: TextView? = null
    private var sanView: TextView? = null
    private var txtViews: LinearLayout? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * Updates the card display based on a Pose
     */
    fun updateDisplay(p : Pose? = null) : Boolean {
        if(p != null) pose = p

        if(pose == null) return false

        imgView = imgView ?: findViewById<ImageView>(R.id.pose_image)
        engView = engView ?: findViewById<TextView>(R.id.english_name)
        sanView = sanView ?: findViewById<TextView>(R.id.sanskrit_name)
        txtViews = txtViews ?: findViewById<LinearLayout>(R.id.card_text_views)
        val drawId: Int = resources.getIdentifier("${pose.img_file?.removeSuffix(".png")}", "drawable", context.packageName)
        imgView?.setImageResource(drawId)
        engView?.text = pose.engName
        sanView?.text = pose.sanName
        return true
    }

    /**
     * Makes translation text transparent
     */
    fun hideText(){
        txtViews?.alpha = 0f
    }

    /**
     * Makes translation text opaque
     */
    fun showText(){
        txtViews?.alpha = 1f
    }
}