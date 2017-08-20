package com.jeffreyfhow.yogaflashcards.Cards

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent

/**
 * Created by jeffreyhow on 8/6/17.
 * The Top Card is the main card that the user interacts with (ie. drags and releases)
 */
class TopCard : Card {
    /*****************************************************************************************
     *                               Initialization
     *****************************************************************************************/
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
        /**
         * delay these initializations, as they are not available until layout is complete.
         */
        post{
            rootX = this.x
            rootY = this.y

            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            offScreenDistance = 0.5f*displayMetrics.widthPixels + this.measuredHeight

            isInitialized = true
        }
    }

    /*****************************************************************************************
     *                               Observer Functionality
     *****************************************************************************************/
    fun registerReleaseObserver(observer: ITopCardReleaseObserver){
        releaseObservers.add(observer)
    }
    fun registerMoveObserver(observer: ITopCardMoveObserver){
        moveObservers.add(observer)
    }
    private fun notifyCardMove(){
        val normalizedX = (this.x - rootX)/ MAX_X_DIST
        moveObservers.forEach { it.onTopCardMove(normalizedX) }
    }
    private fun notifyCardRelease(topCardLocation: TopCardLocation){
        releaseObservers.forEach { it.onTopCardRelease(topCardLocation) }
    }

    /*****************************************************************************************
     *                               State Functions
     *****************************************************************************************/
    /**
     * Sets start card to initial position/rotation/state
     */
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

    private fun setXFromEventRawX(eventRawX: Float){
        this.x = Math.min(Math.max(eventRawX + dX, rootX - MAX_X_DIST), rootX + MAX_X_DIST)
    }

    /**
     * Updates rotation depending on x position of card
     */
    private fun updateRotation(){
        this.rotation = MAX_ROT * (this.x - rootX)/ MAX_X_DIST
    }

    /*****************************************************************************************
     *                               Animation
     *****************************************************************************************/
    /**
     * The following functions get the animators (used by the Animation Manager)
     */
    fun getLeftAnimators(isRooted: Boolean = false) : MutableList<Animator> {
        val animators: MutableList<Animator> = mutableListOf()
        animators.add(ObjectAnimator.ofFloat(this, "x", this.x, rootX - offScreenDistance))
        animators.add(ObjectAnimator.ofFloat(this, "y", this.y, this.y + 0.25f*this.measuredHeight))
        animators.add(ObjectAnimator.ofFloat(this, "rotation", this.rotation,
            if (isRooted) -MAX_ROT_ROOTED else -MAX_ROT))
        return animators
    }

    fun getRightAnimators(isRooted: Boolean = false) : MutableList<Animator> {
        val animators: MutableList<Animator> = mutableListOf()
        animators.add(ObjectAnimator.ofFloat(this, "x", this.x, rootX + offScreenDistance))
        animators.add(ObjectAnimator.ofFloat(this, "y", this.y, this.y + 0.25f*this.measuredHeight))
        animators.add(ObjectAnimator.ofFloat(this, "rotation", this.rotation,
                if (isRooted) MAX_ROT_ROOTED else MAX_ROT))
        return animators
    }

    fun getCenterAnimators() : MutableList<Animator> {
        val animators: MutableList<Animator> = mutableListOf()
        animators.add(ObjectAnimator.ofFloat(this, "x", this.x, rootX))
        animators.add(ObjectAnimator.ofFloat(this, "y", this.y, rootY))
        animators.add(ObjectAnimator.ofFloat(this, "rotation", this.rotation, 0f))
        return animators
    }

    private fun stopAnimations(){
        this.clearAnimation()
    }

    /*****************************************************************************************
     *                               Accessor Functions
     *****************************************************************************************/
    private fun GetTopCardLocation() : TopCardLocation? {
        val xDist = this.x - rootX
        return when{
            xDist < -NEXT_CARD_DIST -> TopCardLocation.LEFT
            xDist > NEXT_CARD_DIST -> TopCardLocation.RIGHT
            else -> TopCardLocation.CENTER
        }
    }

    /*****************************************************************************************
     *                                   Touch Handling
     *****************************************************************************************/
    /**
     * Routes touch event handling to appropriate handler depending on action
     */
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

    /**
     * Handles touch down by revealing text and updating dX state
     */
    private fun handleTouchDown(eventRawX: Float){
        dX = this.x - eventRawX
        showText()
    }

    /**
     * Handles touch move by dragging/rotating card and notifying any move-observers
     */
    private fun handleTouchMove(eventRawX: Float){
        setXFromEventRawX(eventRawX)
        updateRotation()
        notifyCardMove()
    }

    /**
     * Handles touch up by notifying any release-observers
     */
    private fun handleTouchUp(){
        val topCardLocation = GetTopCardLocation()
        if(topCardLocation != null){
            notifyCardRelease(topCardLocation)
        }
    }

}