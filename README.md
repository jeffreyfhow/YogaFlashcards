# YogaFlashcards
Basic prototype for Flash Cards to learn the names of yoga poses. This work-in-progress is experimental and recreational. Both functionality and presentation are far from final.

Beta app can be found <a href="https://play.google.com/store/apps/details?id=com.jeffreyfhow.yogaflashcards&ah=aLUqg0pjkK1ZL__Ns36x2G2y1GQ">here</a>.

Custom formatted data file was created using my <a href="https://github.com/jeffreyfhow/yoga_data_parser">yoga_data_parser</a> project.

If you are here to observe my code, MainActivity.kt is the starting point. It uses JsonParser.kt & PoseBuilder.kt to convert custom Json data to Poses (Pose.kt). Then creates a deck of cards (Deck.kt) with these poses.

Deck.kt is where most of the work happens. It makes use of TopCard.kt (the card the user interacts with), BottomCard.kt.

AnimationManager.kt does just that, it manages the animations of both the TopCard.kt and BottomCard.kt so they are synchronized.

Aside from that, the project contains some Observer Pattern interfaces used for decoupled notifications of important events.
