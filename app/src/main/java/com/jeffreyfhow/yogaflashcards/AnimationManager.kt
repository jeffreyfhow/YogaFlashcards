package com.jeffreyfhow.yogaflashcards

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet

/**
 * Created by jeffreyhow on 8/7/17.
 */
object AnimationManager {
    lateinit var topCard: TopCard
    lateinit var bottomCard: BottomCard
    lateinit var mainActivity: MainActivity

    private var leftObservers: MutableList<ILeftAnimationObserver> = mutableListOf()
    private var rightObservers: MutableList<IRightAnimationObserver> = mutableListOf()
    private var centerObservers: MutableList<ICenterAnimationObserver> = mutableListOf()

    // Observer Functionality
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

    fun registerCenterObserver(observer: ICenterAnimationObserver){
        centerObservers.add(observer)
    }

    // Animations
    fun playLeftAnimations(isRooted: Boolean = false){
        var animators: MutableList<Animator> = mutableListOf()
        animators.addAll(topCard.getLeftAnimators(isRooted))
        animators.addAll(bottomCard.getScaleUpAnimators())
        playAnimators(animators, TopCardLocation.LEFT)
    }

    fun playRightAnimations(isRooted: Boolean = false){
        var animators: MutableList<Animator> = mutableListOf()
        animators.addAll(topCard.getRightAnimators(isRooted))
        animators.addAll(bottomCard.getScaleUpAnimators())
        playAnimators(animators, TopCardLocation.RIGHT)
    }

    fun playCenterAnimations(){
        var animators: MutableList<Animator> = mutableListOf()
        animators.addAll(topCard.getCenterAnimators())
        animators.addAll(bottomCard.getScaleDownAnimators())
        playAnimators(animators, TopCardLocation.CENTER)
    }

    private fun playAnimators(animators: MutableList<Animator>, topCardLocation: TopCardLocation){
        var animatorSet = AnimatorSet()
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