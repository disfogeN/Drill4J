package com.epam.kjni.core.util

import com.epam.kjni.core.GlobState
import jvmapi.jboolean
import jvmapi.jchar
import jvmapi.jclass
import jvmapi.jdouble
import jvmapi.jfloat
import jvmapi.jint
import jvmapi.jobject
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.free
import kotlinx.cinterop.invoke
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.pointed
import kotlinx.cinterop.value

class CallJavaFloatMethod(
    jO: jobject?,
    javaClass: jclass?,
    methodName: CPointer<ByteVar>,
    methodSignature: CPointer<ByteVar>
) : JavaMethod(jO, javaClass, methodName, methodSignature) {
    override fun invoke(vararg raw: X): jfloat {
        val arguments = toJObjectArray(raw)
        val jniEnv = GlobState.env?.pointed?.value?.pointed
        val value = jniEnv!!.CallFloatMethodA!!(GlobState.env, jO, getMethod(), arguments)
        nativeHeap.free(arguments)
        return value
    }


}