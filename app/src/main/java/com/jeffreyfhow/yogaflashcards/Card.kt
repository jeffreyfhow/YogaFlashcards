package com.jeffreyfhow.yogaflashcards

import Pose
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.w3c.dom.Text

/**
 * Created by jeffreyhow on 8/6/17.
 */

open class Card : LinearLayout
{
    lateinit var pose : Pose
    var imgView: ImageView? = null
    var engView: TextView? = null
    var sanView: TextView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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

    fun hideText(){
        engView?.alpha = 0f
        sanView?.alpha = 0f
    }

    fun showText(){
        engView?.alpha = 1f
        sanView?.alpha = 1f
    }

    fun getImageId(context: Context, imageName: String): Int {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName())
    }



}