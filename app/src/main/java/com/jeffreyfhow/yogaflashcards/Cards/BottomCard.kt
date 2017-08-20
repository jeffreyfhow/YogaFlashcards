package com.jeffreyfhow.yogaflashcards.Cards

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.animation.Animator
import com.jeffreyfhow.yogaflashcards.Pose.Pose

/**
 * Created by jeffreyhow on 8/6/17.
 * The bottom card is the card behind the card the user is interacting with.
 * It's main use is to animate into the next card.
 */
class BottomCard : Card, ITopCardMoveObserver {
    /*****************************************************************************************
     *                               Initialization
     *****************************************************************************************/
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    init { setScale(.8f) }

    /*****************************************************************************************
     *                               State Functions
     *****************************************************************************************/
    /**
     * Sets bottom card to starting size/position/state
     */
    fun reset(pose: Pose? = null){
        if(pose != null) updateDisplay(pose)
        stopAnimations()
        setScale(.8f)
        hideText()
    }

    private fun setScale(scale: Float) {
        this.scaleX = scale
        this.scaleY = scale
    }

    /*****************************************************************************************
     *                                   Animation
     *****************************************************************************************/
    /**
     * The following functions get the animators (used by the Animation Manager)
     */
    fun getScaleUpAnimators() : MutableList<Animator> {
        val animators: MutableList<Animator> = mutableListOf()
        animators.add(ObjectAnimator.ofFloat(this, "scaleX", scaleX, 1f))
        animators.add(ObjectAnimator.ofFloat(this, "scaleY", scaleY, 1f))
        return animators
    }

    fun getScaleDownAnimators() : MutableList<Animator> {
        val animators: MutableList<Animator> = mutableListOf()
        animators.add(ObjectAnimator.ofFloat(this, "scaleX", scaleX, .8f))
        animators.add(ObjectAnimator.ofFloat(this, "scaleY", scaleY, .8f))
        return animators
    }

    private fun stopAnimations(){
        this.clearAnimation()
    }

    /*****************************************************************************************
     *                                   Event Handlers
     *****************************************************************************************/
    /**
     * Handles top card moving by scaling the bottom card accordingly
     */
    override fun onTopCardMove(normalizedX: Float) {
        stopAnimations()
        setScale(.1f*Math.abs(normalizedX) + .8f)
    }

}