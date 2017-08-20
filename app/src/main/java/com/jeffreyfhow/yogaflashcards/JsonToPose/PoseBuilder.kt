package com.jeffreyfhow.yogaflashcards.JsonToPose
import com.beust.klaxon.*
import com.jeffreyfhow.yogaflashcards.Pose.Grade
import com.jeffreyfhow.yogaflashcards.Pose.Pose
import com.jeffreyfhow.yogaflashcards.Pose.Translation

/**
 * Created by jeffreyhow on 7/29/17.
 * Singleton used to build Pose objects from custom JsonObjects
 */
object PoseBuilder {
    /**
     * Creates Pose object from custom formatted JsonObject
     */
    private fun createPose(jsonObj: JsonObject): Pose? {
        val reqPropNames = listOf("id", "english", "difficulty", "position", "classification")
        val reqPropMap: MutableMap<String, String?> = mutableMapOf()

        // Required Properties
        for (name in reqPropNames) reqPropMap[name] = jsonObj.string(name)
        val grade: Grade = Grade.valueOf(jsonObj.string("grade")?:"NULL")

        //Optional Properties
        val sanskritName = jsonObj.string("sanskrit")
        val img_file = jsonObj.string("img_file")

        val translations: MutableList<Translation> = mutableListOf()
        val jsonTranslations = jsonObj.array<String>("translations")
        jsonTranslations?.forEach {
            it.let{ // Warning is a lie
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

    /**
     * Creates list of poses from custom formatted JsonArray
     */
    fun createPoses(jsonArr: JsonArray<JsonObject>): MutableList<Pose> {
        val poses: MutableList<Pose> = mutableListOf()
        for(item in jsonArr){
            val pose = createPose(item)
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