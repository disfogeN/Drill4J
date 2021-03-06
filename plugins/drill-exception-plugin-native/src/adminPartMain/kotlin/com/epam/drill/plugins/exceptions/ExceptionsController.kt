package com.epam.drill.plugins.exceptions


import com.epam.drill.plugin.api.end.AdminPluginPart
import com.epam.drill.plugin.api.end.WsService
import com.epam.drill.plugin.api.message.DrillMessage

@Suppress("unused")

class ExceptionsController(private val ws: WsService, val name: String) : AdminPluginPart(ws, name) {

    override suspend fun processData(dm: DrillMessage): Any {
        val sessionId = dm.sessionId
        ws.convertAndSend(id + (sessionId ?: ""), dm.content!!)
        return ""
    }

}
