package com.epam.drill.jvmti.types

import jvmapi.jthread
import jvmapi.jvmtiLocalVariableEntry

abstract class JType {

    abstract fun retrieveValue(thread: jthread?, depth: Int, currentEntry: jvmtiLocalVariableEntry): Pair<String, Any>?
}