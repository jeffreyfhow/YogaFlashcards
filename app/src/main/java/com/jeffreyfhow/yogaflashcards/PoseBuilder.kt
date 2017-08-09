package com.jeffreyfhow.yogaflashcards
import Pose
import Translation
import Grade
import com.beust.klaxon.*

/**
 * Created by jeffreyhow on 7/29/17.
 */

class IncompletePoseException(jsonObj: JsonObject, poseDetailType: String):
        RuntimeException("IncompletePoseException: pose does not contain required data - $poseDetailType\n$jsonObj")

class PoseBuilder {
    fun createPose(jsonObj: JsonObject): Pose? {
        val reqPropNames = listOf("id", "english", "difficulty", "position", "classification")
        var reqPropMap: MutableMap<String, String?> = mutableMapOf()

        // Required Properties
        for (name in reqPropNames) reqPropMap[name] = jsonObj.string(name)
        var grade: Grade = Grade.valueOf(jsonObj.string("grade")?:"NULL")

        //Optional Properties
        val sanskritName = jsonObj.string("sanskrit")
        val img_file = jsonObj.string("img_file")

        var translations: MutableList<Translation> = mutableListOf()
        val jsonTranslations = jsonObj.array<String>("translations")
        jsonTranslations?.forEach {
            it?.let{ // Warning is a lie
                val (san, eng) = it.split("=")
                translations.add(Translation(san.trim(), eng.trim()))
            }
        }

        val altNames: List<String> = jsonObj.array<String>("alt_names")?.toList() ?: listOf()

        try {
            reqPropNames.forEach {
                if(reqPropMap[it] == null) throw IncompletePoseException(jsonObj, it)
            }
            return Pose(
                    reqPropMap["id"]!!,
                    reqPropMap["english"]!!,
                    reqPropMap["position"]!!,
                    reqPropMap["classification"]!!,
                    reqPropMap["difficulty"]!!,
                    sanskritName, altNames, translations, img_file, grade)
        } catch (e: IncompletePoseException){
            println("****************************************")
            println(e.message)
            println("****************************************")
            return null
        }
    }

    fun createPoses(jsonArr: JsonArray<JsonObject>): MutableList<Pose> {
        var poses: MutableList<Pose> = mutableListOf()
        for(item in jsonArr){
            var pose = createPose(item)
            if(pose != null) {
                poses.add(pose)
            } else {
                println("WARNING: PoseBuilder.createPoses - pose is null")
                println(item)
            }
        }
        return poses
    }

}