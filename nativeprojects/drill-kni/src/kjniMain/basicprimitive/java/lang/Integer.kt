package java.lang

import com.epam.kjni.core.GlobState.env
import com.epam.kjni.core.GlobState.jni
import com.epam.kjni.core.Synthetic
import com.epam.kjni.core.util.CallJavaIntMethod
import com.epam.kjni.core.util.CallJavaVoidMethod
import com.epam.kjni.core.util.JavaConstructor
import com.epam.kjni.core.util.getJString
import com.epam.kjni.core.util.getKString
import com.epam.kjni.core.util.toJObjectArray
import jvmapi.jclass
import jvmapi.jobject
import kotlin.Int
import kotlin.String
import kotlinx.cinterop.alloc
import kotlinx.cinterop.cstr
import kotlinx.cinterop.free
import kotlinx.cinterop.invoke
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value

class Integer : Synthetic {
    override lateinit var javaObject: jobject

    lateinit var javaClass: jclass

    val className: String = "java/lang/Integer"

    constructor(cp0: Int) {
        memScoped {
        javaClass = jni.FindClass!!(env, className.cstr.getPointer(this))!!
        val toJObjectArray = toJObjectArray(arrayOf(com.epam.kjni.core.util.X(cp0, true)))
        val methodName = "<init>".cstr.getPointer(this)
        val methodSignature = "(I)V".cstr.getPointer(this)
        val jconstructor = JavaConstructor(javaClass, methodName, methodSignature).getMethod()
        javaObject = jni.NewObjectA!!(env, javaClass, jconstructor, toJObjectArray)!!
        nativeHeap.free(toJObjectArray)
        }
    }

    constructor(cp0: String) {
        memScoped {
        javaClass = jni.FindClass!!(env, className.cstr.getPointer(this))!!
        val toJObjectArray = toJObjectArray(arrayOf(com.epam.kjni.core.util.X(cp0, false)))
        val methodName = "<init>".cstr.getPointer(this)
        val methodSignature = "(Ljava/lang/String;)V".cstr.getPointer(this)
        val jconstructor = JavaConstructor(javaClass, methodName, methodSignature).getMethod()
        javaObject = jni.NewObjectA!!(env, javaClass, jconstructor, toJObjectArray)!!
        nativeHeap.free(toJObjectArray)
        }
    }

    constructor(jobj: jobject) {
        memScoped {
        javaClass = jni.FindClass!!(env, className.cstr.getPointer(this))!!
        javaObject = jobj
        }
    }

    fun toPrimitive(): Int {
        memScoped{
        val methodName = "intValue".cstr.getPointer(this)
        val methodSignature = "()I".cstr.getPointer(this)
        return CallJavaIntMethod(
                            javaObject,
                            javaClass,
                            methodName,
                            methodSignature
                        )()
        }
    }
}
