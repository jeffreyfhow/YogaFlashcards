package com.jeffreyfhow.yogaflashcards.JsonToPose

import com.beust.klaxon.JsonObject

/**
 * Created by jeffreyfhow on 8/19/17.
 * Custom Exception for when a Pose.Pose is missing necessary information.
 */
class IncompletePoseException(jsonObj: JsonObject, poseDetailType: String):
        RuntimeException("IncompletePoseException: pose does not contain required data - $poseDetailType\n$jsonObj")