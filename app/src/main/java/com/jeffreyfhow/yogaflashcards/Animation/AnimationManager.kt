package com.jeffreyfhow.yogaflashcards.Animation
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import com.jeffreyfhow.yogaflashcards.Cards.BottomCard
import com.jeffreyfhow.yogaflashcards.Cards.TopCard
import com.jeffreyfhow.yogaflashcards.Cards.TopCardLocation

/**
 * Created by jeffreyhow on 8/7/17.
 * Singleton used to coordinate all the various animations
 */
object AnimationManager {
    /**
     * The items that are animated
     */
    lateinit var topCard: TopCard
    lateinit var bottomCard: BottomCard

    /*****************************************************************************************
     *                               Observer Functionality
     *****************************************************************************************/
    private var leftObservers: MutableList<ILeftAnimationObserver> = mutableListOf()
    private var rightObservers: MutableList<IRightAnimationObserver> = mutableListOf()
    private var centerObservers: MutableList<ICenterAnimationObserver> = mutableListOf()

    private fun notifyLeftObservers(){
        leftObservers.forEach{ it.onLeftAnimationComplete() }
    }

    private fun notifyRightObservers(){
        rightObservers.forEach { it.onRightAnimationComplete() }
    }

    private fun notifyCenterObservers(){
        centerObservers.forEach { it.onCenterAnimationComplete() }
    }

    fun registerLeftObserver(observer: ILeftAnimationObserver){
        leftObservers.add(observer)
    }

    fun registerRightObserver(observer: IRightAnimationObserver){
        rightObservers.add(observer)
    }

//    fun registerCenterObserver(observer: ICenterAnimationObserver){
//        centerObservers.add(observer)
//    }

    /*****************************************************************************************
     *                               Animations
     *****************************************************************************************/
    /**
     * Plays the animations for when the top card is released on the left
     */
    fun playLeftAnimations(isRooted: Boolean = false){
        val animators: MutableList<Animator> = mutableListOf()
        animators.addAll(topCard.getLeftAnimators(isRooted))
        animators.addAll(bottomCard.getScaleUpAnimators())
        playAnimators(animators, TopCardLocation.LEFT)
    }

    /**
     * Plays the animations for when the top card is released on the right
     */
    fun playRightAnimations(isRooted: Boolean = false){
        val animators: MutableList<Animator> = mutableListOf()
        animators.addAll(topCard.getRightAnimators(isRooted))
        animators.addAll(bottomCard.getScaleUpAnimators())
        playAnimators(animators, TopCardLocation.RIGHT)
    }

    /**
     * Plays the animations for when the top card is released in the center
     */
    fun playCenterAnimations(){
        val animators: MutableList<Animator> = mutableListOf()
        animators.addAll(topCard.getCenterAnimators())
        animators.addAll(bottomCard.getScaleDownAnimators())
        playAnimators(animators, TopCardLocation.CENTER)
    }

    /**
     * Takes a list of animators and starts playing them together
     * Sets them up to notify appropriate observers when the animation is complete.
     */
    private fun playAnimators(animators: MutableList<Animator>, topCardLocation: TopCardLocation){
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animators)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                when(topCardLocation){
                    TopCardLocation.LEFT -> notifyLeftObservers()
                    TopCardLocation.CENTER -> notifyCenterObservers()
                    TopCardLocation.RIGHT -> notifyRightObservers()
                }
            }
        })

        animatorSet.start()
    }
}