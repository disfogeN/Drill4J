package com.epam.drill.plugin

import com.epam.drill.plugin.api.processing.AgentPluginPart
import drillInternal.config
import kotlinx.cinterop.asStableRef

actual object PluginStorage {

    actual val storage: MutableMap<String, AgentPluginPart>
        get() = config.pstorage?.asStableRef<MutableMap<String, AgentPluginPart>>()?.get()!!


}