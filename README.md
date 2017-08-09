# YogaFlashcards
Basic prototype for Flash Cards to learn the names of yoga poses. This work-in-progress is experimental and recreational. Both functionality and presentation are far from final.

Related source code can be found in app/src/main/java/com/jeffreyfhow/yogaflashcards

If you are here to observe my code, MainActivity.kt is the starting point. It uses JsonParser.kt & PoseBuilder.kt to convert custom Json data to Poses (Pose.kt). Then creates a deck of cards (Deck.kt) with these poses.

Deck.kt is where most of the work happens. It makes use of TopCard.kt (the card the user interacts with), BottomCard.kt.

AnimationManager.kt does just that, it manages the animations of both the TopCard.kt and BottomCard.kt so they are synchronized.

IAnimationObservers.kt & ITopCardObservers.kt contain some Observer Pattern interfaces so they can be notified of important events without being too coupled with the concrete Observable.
