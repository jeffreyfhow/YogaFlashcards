package com.jeffreyfhow.yogaflashcards

/**
 * Created by jeffreyhow on 8/8/17.
 */

interface ITopCardMoveObserver {
    fun onTopCardMove(normalizedX: Float)
}

interface ITopCardReleaseObserver {
    fun onTopCardRelease(topCardLocation: TopCardLocation)
}