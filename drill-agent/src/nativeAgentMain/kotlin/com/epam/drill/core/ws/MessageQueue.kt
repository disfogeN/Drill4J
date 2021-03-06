package com.epam.drill.core.ws

import com.epam.drill.api.drillRequest
import com.epam.drill.common.Message
import com.epam.drill.common.MessageType
import com.epam.drill.core.request.DrillMessage
import com.epam.drill.core.request.MessageWrapper
import drillInternal.addMessage

import drillInternal.getMessage
import kotlinx.cinterop.Arena
import kotlinx.cinterop.cstr
import kotlinx.cinterop.toKString
import kotlinx.serialization.json.Json

//todo think about coroutines
object MessageQueue {

    fun retrieveMessage(): String? {
        return getMessage()?.toKString()
    }


    fun sendMessage(content: String) {
        //fixme create global Arena scope
        val message =
            Json.stringify(
                Message.serializer(), Message(
                    MessageType.PLUGIN_DATA, "",

                    Json.stringify(
                        MessageWrapper.serializer(),
                        MessageWrapper(
                            "except-ions", DrillMessage(
                                drillRequest()?.drillSessionId ?: "", content
                            )
                        )
                    )
                )
            )
        addMessage(message.cstr.getPointer(Arena()))
    }
}