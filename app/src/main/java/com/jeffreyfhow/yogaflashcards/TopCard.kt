package com.jeffreyfhow.yogaflashcards

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent

/**
 * Created by jeffreyhow on 8/6/17.
 */
enum class TopCardLocation { LEFT, CENTER, RIGHT }

class TopCard : Card {
    private var dX: Float = 0f
    private var rootX: Float = -1f
    private var rootY: Float = -1f
    private var offScreenDistance: Float = -1f
    private var isInitialized = false
    private var moveObservers: MutableList<ITopCardMoveObserver> = mutableListOf()
    private var releaseObservers: MutableList<ITopCardReleaseObserver> = mutableListOf()
    var isInteractable: Boolean = true

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        post{
            rootX = this.x
            rootY = this.y

            var displayMetrics: DisplayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            offScreenDistance = 0.5f*displayMetrics.widthPixels + this.measuredHeight

            isInitialized = true
        }
    }

    // Observer Functionality
    fun registerReleaseObserver(observer: ITopCardReleaseObserver){
        releaseObservers.add(observer)
    }
    fun registerMoveObserver(observer: ITopCardMoveObserver){
        moveObservers.add(observer)
    }
    fun notifyCardMove(){
        val normalizedX = (this.x - rootX)/ MAX_X_DIST
        moveObservers.forEach { it.onTopCardMove(normalizedX) }
    }
    fun notifyCardRelease(topCardLocation: TopCardLocation){
        releaseObservers.forEach { it.onTopCardRelease(topCardLocation) }
    }

    // Update State
    fun reset(){
        if(pose != null){
            updateDisplay(pose)
        }
        stopAnimations()
        this.x = rootX
        this.y = rootY
        this.rotation = 0f
        this.hideText()
    }

    private fun setXPosition(eventRawX: Float){
        this.x = Math.min(Math.max(eventRawX + dX, rootX - MAX_X_DIST), rootX + MAX_X_DIST)
    }

    private fun updateRotation(){
        this.rotation = MAX_ROT * (this.x - rootX)/ MAX_X_DIST
    }

    private fun stopAnimations(){
        this.clearAnimation()
    }

    // Animator Accessors
    fun getLeftAnimators(isRooted: Boolean = false) : MutableList<Animator> {
        var animators: MutableList<Animator> = mutableListOf()
        animators.add(ObjectAnimator.ofFloat(this, "x", this.x, rootX - offScreenDistance))
        animators.add(ObjectAnimator.ofFloat(this, "y", this.y, this.y + 0.25f*this.measuredHeight))
        animators.add(ObjectAnimator.ofFloat(this, "rotation", this.rotation,
            if (isRooted) -MAX_ROT_ROOTED else -MAX_ROT))
        return animators
    }

    fun getRightAnimators(isRooted: Boolean = false) : MutableList<Animator> {
        var animators: MutableList<Animator> = mutableListOf()
        animators.add(ObjectAnimator.ofFloat(this, "x", this.x, rootX + offScreenDistance))
        animators.add(ObjectAnimator.ofFloat(this, "y", this.y, this.y + 0.25f*this.measuredHeight))
        animators.add(ObjectAnimator.ofFloat(this, "rotation", this.rotation,
                if (isRooted) MAX_ROT_ROOTED else MAX_ROT))
        return animators
    }

    fun getCenterAnimators() : MutableList<Animator> {
        var animators: MutableList<Animator> = mutableListOf()
        animators.add(ObjectAnimator.ofFloat(this, "x", this.x, rootX))
        animators.add(ObjectAnimator.ofFloat(this, "y", this.y, rootY))
        animators.add(ObjectAnimator.ofFloat(this, "rotation", this.rotation, 0f))
        return animators
    }

    // Accessors
    fun GetTopCardLocation() : TopCardLocation? {
        val xDist = this.x - rootX
        when{
            xDist < -NEXT_CARD_DIST -> return TopCardLocation.LEFT
            xDist > NEXT_CARD_DIST -> return TopCardLocation.RIGHT
            else -> return TopCardLocation.CENTER
        }
    }

    // Touch Handling
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> handleTouchDown(event.rawX)
            MotionEvent.ACTION_MOVE -> handleTouchMove(event.rawX)
            MotionEvent.ACTION_UP -> handleTouchUp()
            else -> return false
        }
        return true
    }

    private fun handleTouchDown(eventRawX: Float){
        dX = this.x - eventRawX
        showText()
    }

    private fun handleTouchMove(eventRawX: Float){
        setXPosition(eventRawX)
        updateRotation()
        notifyCardMove()
    }

    private fun handleTouchUp(){
        val topCardLocation = GetTopCardLocation()
        if(topCardLocation != null){
            notifyCardRelease(topCardLocation)
        }
    }

}