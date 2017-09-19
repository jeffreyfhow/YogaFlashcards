package com.jeffreyfhow.yogaflashcards
import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import com.jeffreyfhow.yogaflashcards.Animation.AnimationManager
import com.jeffreyfhow.yogaflashcards.Animation.ILeftAnimationObserver
import com.jeffreyfhow.yogaflashcards.Animation.IRightAnimationObserver
import com.jeffreyfhow.yogaflashcards.Cards.BottomCard
import com.jeffreyfhow.yogaflashcards.Cards.ITopCardReleaseObserver
import com.jeffreyfhow.yogaflashcards.Cards.TopCard
import com.jeffreyfhow.yogaflashcards.Cards.TopCardLocation
import com.jeffreyfhow.yogaflashcards.Pose.Pose

/**
 * Created by jeffreyhow on 8/7/17.
 * Manages the interactions between the Cards
 */
class Deck (private val context: Context, private val poses: MutableList<Pose>) :
    ITopCardReleaseObserver, ILeftAnimationObserver, IRightAnimationObserver
{
    /*****************************************************************************************
     *                               Initialization
     *****************************************************************************************/
    private var currPoses: MutableList<Pose> = mutableListOf()
    private var topCard: TopCard
    private var bottomCard: BottomCard
    private var nextPose: Pose? = null
    private var snackBar: Snackbar? = null
    var isInteractable: Boolean = true

    init{
        val activity = context as Activity
        topCard = activity.findViewById(R.id.top_card) as TopCard
        bottomCard = activity.findViewById(R.id.bottom_card) as BottomCard

        topCard.registerMoveObserver(bottomCard)
        topCard.registerReleaseObserver(this)

        // Hook Up Animation Manager
        AnimationManager.topCard = topCard
        AnimationManager.bottomCard = bottomCard
        AnimationManager.registerLeftObserver(this)
        AnimationManager.registerRightObserver(this)

        resetPoses()
    }

    /*****************************************************************************************
     *                               Deck Manipulation
     *****************************************************************************************/
    /**
     * Trashes card -> tweens rightward off screen and removes pose from deck
     */
    fun trash(isRooted: Boolean){
        val pose: Pose? = getRandomPose()
        when {
            pose != null -> nextPose = pose
            topCard.pose == bottomCard.pose -> {
                nextPose = bottomCard.pose
                showResetSnackBar()
            }
            else -> nextPose = bottomCard.pose
        }
        makeInteractable(false)
        AnimationManager.playRightAnimations(isRooted)
    }

    /**
     * Keeps card -> tweens leftward off screen and returns pose to deck
     */
    fun keep(isRooted: Boolean){

        val pose: Pose? = getRandomPose()
        if(pose != null){
            nextPose = pose
            currPoses.add(topCard.pose)
        } else{
            nextPose = topCard.pose
        }
        makeInteractable(false)
        AnimationManager.playLeftAnimations(isRooted)
    }

    /**
     * Reveals text of top card
     */
    fun reveal(){
        topCard.showText()
    }

    /*****************************************************************************************
     *                               Event Handlers
     *****************************************************************************************/
    /**
     * Reshuffles all poses into deck
     */
    private fun resetPoses(){
        currPoses = poses.map{it}.toMutableList()
        topCard.updateDisplay(getRandomPose())
        bottomCard.updateDisplay(getRandomPose())
    }

    private fun getRandomPose() : Pose? {
        return if (currPoses.size < 1) null
        else currPoses.removeAt((Math.random() * currPoses.size).toInt())
    }

    /**
     * Sets both cards to their next poses
     */
    private fun setNextCard(){
        topCard.updateDisplay(bottomCard.pose)
        topCard.reset()
        bottomCard.updateDisplay(nextPose)
        bottomCard.reset()
    }

    /**
     * Shows snackbar when exhausted through all cards
     */
    private fun showResetSnackBar(){
        if(snackBar == null) {
            snackBar = Snackbar.make(
                    (context as Activity).findViewById(R.id.coordinator),
                    "You have exhausted through all your cards",
                    Snackbar.LENGTH_INDEFINITE
            ).setAction("Shuffle"){
                resetPoses()
            }
        }
        snackBar?.show()
    }

    /**
     * toggles isInteractable (which is used to denote when a card can be interacted with)
     */
    private fun makeInteractable(on: Boolean = true){
        isInteractable = on
        topCard.isInteractable = on
    }

    /*****************************************************************************************
     *                               Event Handlers
     *****************************************************************************************/
    override fun onLeftAnimationComplete() {
        setNextCard()
        makeInteractable()
    }

    override fun onRightAnimationComplete() {
        setNextCard()
        makeInteractable()
    }

    override fun onTopCardRelease(topCardLocation: TopCardLocation) {
        when(topCardLocation){
            TopCardLocation.LEFT -> keep(false)
            TopCardLocation.RIGHT -> trash(false)
            TopCardLocation.CENTER -> AnimationManager.playCenterAnimations()
        }
    }

}