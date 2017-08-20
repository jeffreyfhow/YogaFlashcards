package com.jeffreyfhow.yogaflashcards.Cards
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jeffreyfhow.yogaflashcards.Pose.Pose
import com.jeffreyfhow.yogaflashcards.R

/**
 * Created by jeffreyhow on 8/6/17.
 * Superclass for Top & Bottom Cards
 */
open class Card : LinearLayout
{
    /**
     * Initialization
     */
    lateinit var pose : Pose
    private var imgView: ImageView? = null
    private var engView: TextView? = null
    private var sanView: TextView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * Updates the card display based on a Pose
     */
    fun updateDisplay(p : Pose? = null) : Boolean {
        if(p != null) pose = p

        if(pose == null) return false

        imgView = imgView ?: this.findViewById(R.id.pose_image) as ImageView
        engView = engView ?: this.findViewById(R.id.english_name) as TextView
        sanView = sanView ?: this.findViewById(R.id.sanskrit_name) as TextView
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
        engView?.alpha = 0f
        sanView?.alpha = 0f
    }

    /**
     * Makes translation text opaque
     */
    fun showText(){
        engView?.alpha = 1f
        sanView?.alpha = 1f
    }
}