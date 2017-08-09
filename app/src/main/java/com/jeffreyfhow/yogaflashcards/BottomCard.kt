package com.jeffreyfhow.yogaflashcards

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import Pose
import android.animation.Animator

/**
 * Created by jeffreyhow on 8/6/17.
 */
class BottomCard : Card, ITopCardMoveObserver {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    init {
        setScale(.8f)
    }

    fun reset(pose:Pose? = null){
        if(pose != null) updateDisplay(pose)
        stopAnimations()
        setScale(.8f)
        hideText()
    }

    private fun setScale(scale: Float) {
        this.scaleX = scale
        this.scaleY = scale
    }

    fun stopAnimations(){
        this.clearAnimation()
    }

    // Animator Accessors
    fun getScaleUpAnimators() : MutableList<Animator> {
        var animators: MutableList<Animator> = mutableListOf()
        animators.add(ObjectAnimator.ofFloat(this, "scaleX", scaleX, 1f))
        animators.add(ObjectAnimator.ofFloat(this, "scaleY", scaleY, 1f))
        return animators
    }

    fun getScaleDownAnimators() : MutableList<Animator> {
        var animators: MutableList<Animator> = mutableListOf()
        animators.add(ObjectAnimator.ofFloat(this, "scaleX", scaleX, .8f))
        animators.add(ObjectAnimator.ofFloat(this, "scaleY", scaleY, .8f))
        return animators
    }

    // Event Handling
    override fun onTopCardMove(normalizedX: Float) {
        stopAnimations()
        setScale(.1f*Math.abs(normalizedX) + .8f)
    }

}