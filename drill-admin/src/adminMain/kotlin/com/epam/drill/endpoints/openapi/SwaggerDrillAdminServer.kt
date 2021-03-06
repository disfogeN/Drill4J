package com.epam.drill.endpoints.openapi

import com.epam.drill.agentmanager.AgentStorage
import com.epam.drill.agentmanager.DrillAgent
import com.epam.drill.common.AgentEvent
import com.epam.drill.common.DrillEvent
import com.epam.drill.common.Message
import com.epam.drill.common.MessageType
import com.epam.drill.plugins.Plugins
import com.epam.drill.plugins.agentPluginPart
import com.epam.drill.plugins.serverInstance
import com.epam.drill.router.Routes
import com.google.gson.Gson
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.send
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.serialization.toUtf8Bytes
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

/**
 * Swagger DrillAdmin
 *
 * This is a drill-ktor-admin-server
 */
@KtorExperimentalLocationsAPI
class SwaggerDrillAdminServer(override val kodein: Kodein) : KodeinAware {
    private val app: Application by instance()
    private val agentStorage: AgentStorage by instance()
    private val plugins: Plugins by kodein.instance()

    init {
        app.routing {
            registerAgent()
            registerDrillAdmin()
        }
    }

    private fun Routing.registerAgent() {
        authenticate {
            get<Routes.UnloadPlugin> { up ->
                val drillAgent: DrillAgent? = agentStorage.agents[up.agentName]
                if (drillAgent == null) {
                    call.respond("can't find the agent '${up.agentName}'")
                    return@get
                }
                val agentPluginPartFile = plugins.plugins[up.pluginName]?.agentPluginPart
                if (agentPluginPartFile == null) {
                    call.respond("can't find the plugin '${up.pluginName}' in the agent '${up.agentName}'")
                    return@get
                }

                drillAgent.agentWsSession.send(
                    Gson().toJson(
                        Message(
                            MessageType.MESSAGE,
                            "/",
                            Gson().toJson(AgentEvent(DrillEvent.UNLOAD_PLUGIN, data = up.pluginName))
                        )
                    )
                )
//            drillAgent.agentInfo.rawPluginNames.removeIf { x -> x.id == up.pluginName }
                call.respond("event 'unload' was sent to AGENT")
            }
        }

        authenticate {
            get<Routes.LoadPlugin> { lp ->
                val drillAgent: DrillAgent? = agentStorage.agents[lp.agentName]
                if (drillAgent == null) {
                    call.respond("can't find the agent '${lp.agentName}'")
                    return@get
                }
                val agentPluginPartFile = plugins.plugins[lp.pluginName]?.agentPluginPart
                if (agentPluginPartFile == null) {
                    call.respond("can't find the plugin '${lp.pluginName}' in the agent '${lp.agentName}'")
                    return@get
                }
                val inChannel: FileChannel = agentPluginPartFile.inputStream().channel
                val fileSize: Long = inChannel.size()
                val buffer: ByteBuffer = ByteBuffer.allocate(fileSize.toInt())


                val title: ByteBuffer = ByteBuffer.allocate(20)
                title.put(lp.pluginName.toUtf8Bytes())
                while (title.hasRemaining())
                    title.put("!".toUtf8Bytes())
                title.flip()
                inChannel.read(buffer)
                buffer.flip()
                val put = ByteBuffer.allocate(20 + fileSize.toInt()).put(title).put(buffer)
                put.flip()
                drillAgent.agentWsSession.send(Frame.Binary(true, put))
                call.respond("event 'load' and plugin's file(${lp.agentName}) was sent to AGENT")
            }
        }

        authenticate {
            get<Routes.Agents> {
                call.respond(agentStorage.agents.values.map { da -> da.agentInfo })
            }
        }
        authenticate {
            get<Routes.Agent> { up ->
                call.respond(agentStorage.agents[up.agentName]?.agentInfo!!)
            }
        }
    }

    /**
     * drill-admin
     */
    private fun Routing.registerDrillAdmin() {
        authenticate {
            get<Routes.AllPlugins> {
                call.respond(plugins.plugins.values.map { dp -> dp.serverInstance.id })
            }
        }
    }

}
