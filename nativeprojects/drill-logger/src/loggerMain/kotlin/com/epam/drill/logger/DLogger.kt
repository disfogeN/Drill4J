package com.epam.drill.logger

import com.soywiz.klogger.Logger
import kotlinx.cinterop.asStableRef
import storage.loggers

object DLogger {

    operator fun invoke(name: String): Logger {
        val loggers1 = loggers.logs ?: return Logger("random").apply { level = Logger.Level.TRACE }
        val logges = loggers1.asStableRef<MutableMap<String, Logger>>().get()
        val logger = logges[name]
        val get = loggers.loggerConfig?.asStableRef<Properties>()?.get()

        val s = get?.get(name)

        return if (logger != null) {
            logger
        } else {
            logges[name] = Logger(name).apply {
                if (s != null) {
                    level = Logger.Level.valueOf(s)
                }
            }
            logges[name] ?: Logger("random").apply { level = Logger.Level.TRACE }
        }

    }

}