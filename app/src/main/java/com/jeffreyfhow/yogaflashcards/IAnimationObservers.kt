package com.jeffreyfhow.yogaflashcards

/**
 * Created by jeffreyhow on 8/8/17.
 */
interface ILeftAnimationObserver {
    fun onLeftAnimationComplete()
}

interface IRightAnimationObserver {
    fun onRightAnimationComplete()
}

interface ICenterAnimationObserver {
    fun onCenterAnimationComplete()
}