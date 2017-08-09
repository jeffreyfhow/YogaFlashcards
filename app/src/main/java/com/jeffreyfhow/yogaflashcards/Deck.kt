package com.jeffreyfhow.yogaflashcards
import Pose
import android.app.Activity
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.View

const val MAX_X_DIST = 300f
const val MAX_ROT = 10f
const val MAX_ROT_ROOTED = 50f
const val NEXT_CARD_DIST = 200f

/**
 * Created by jeffreyhow on 8/7/17.
 */
class Deck (val context: Context, val poses: MutableList<Pose>) :
    ITopCardReleaseObserver, ILeftAnimationObserver, IRightAnimationObserver
{
    private var currPoses: MutableList<Pose> = mutableListOf()
    var topCard: TopCard
    var bottomCard: BottomCard
    private var nextPose: Pose? = null
    private var snackBar: Snackbar? = null
    var isInteractable: Boolean = true

    init{
        val activity = context as Activity
        topCard = activity.findViewById(R.id.top_card) as TopCard
        bottomCard = activity.findViewById(R.id.back_card) as BottomCard

        topCard.registerMoveObserver(bottomCard)
        topCard.registerReleaseObserver(this)

        // Hook Up Animation Manager
        AnimationManager.topCard = topCard
        AnimationManager.bottomCard = bottomCard
        AnimationManager.mainActivity = activity as MainActivity
        AnimationManager.registerLeftObserver(this)
        AnimationManager.registerRightObserver(this)

        resetPoses()
    }

    // Deck Manipulation
    fun trash(isRooted: Boolean){
        val pose: Pose? = getRandomPose()
        if(pose != null){
            nextPose = pose
        } else if (topCard.pose == bottomCard.pose) {
            nextPose = bottomCard.pose
            showResetSnackBar()
        } else {
            nextPose = bottomCard.pose
        }
        makeInteractable(false)
        AnimationManager.playRightAnimations(isRooted)
    }

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

    // Helper Functions
    private fun resetPoses(){
        currPoses = poses.map{it}.toMutableList()
        topCard.updateDisplay(getRandomPose())
        bottomCard.updateDisplay(getRandomPose())
    }

    private fun getRandomPose() : Pose? {
        return if (currPoses.size < 1) null
        else currPoses.removeAt((Math.random() * currPoses.size).toInt())
    }

    private fun setNextCard(){
        topCard.updateDisplay(bottomCard.pose)
        topCard.reset()
        bottomCard.updateDisplay(nextPose)
        bottomCard.reset()
    }

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

    private fun makeInteractable(on: Boolean = true){
        isInteractable = on
        topCard.isInteractable = on
    }

    // Event Handlers
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