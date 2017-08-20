package com.jeffreyfhow.yogaflashcards.Cards

/**
 * Created by jeffreyfhow on 8/19/17.
 * Interface to be notified of when the top card moves around.
 */
interface ITopCardMoveObserver {
    fun onTopCardMove(normalizedX: Float)
}