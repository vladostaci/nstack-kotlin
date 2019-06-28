package dk.nodes.nstack.kotlin.models

import org.json.JSONObject

data class Update(
    val state: String,
    val lastId: Int,
    val version: String,
    val link: String,
    val translate: UpdateTranslate
) {

    constructor(jsonObject: JSONObject) : this(
        state = jsonObject.optString("state", "none"),
        lastId = jsonObject.getInt("last_id"),
        version = jsonObject.getString("version"),
        link = jsonObject.optString("link", "empty"),
        translate = UpdateTranslate(jsonObject.getJSONObject("translate"))
    )
}