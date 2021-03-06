package com.epam.drill.jvmapi

import com.epam.drill.logger.DLogger
import jvmapi.*
import kotlinx.cinterop.useContents
import kotlin.native.concurrent.SharedImmutable

val logger
    get() = DLogger("capabilities")

val jvmtiWarnings
    get() = DLogger("jvmtiWarnings")

fun printAllowedCapabilities() {
    val potentialCapabilities = GetPotentialCapabilities()
    potentialCapabilities.useContents {
        logger.info { "--------------------------Allowed Capabilities--------------------------" }
        logger.info { this::can_access_local_variables.name + " = " + can_access_local_variables }
        logger.info { this::can_force_early_return.name + " = " + can_force_early_return }
        logger.info { this::can_generate_all_class_hook_events.name + " = " + can_generate_all_class_hook_events }
        logger.info { this::can_generate_breakpoint_events.name + " = " + can_generate_breakpoint_events }
        logger.info { this::can_generate_compiled_method_load_events.name + " = " + can_generate_compiled_method_load_events }
        logger.info { this::can_generate_exception_events.name + " = " + can_generate_exception_events }
        logger.info { this::can_generate_field_access_events.name + " = " + can_generate_field_access_events }
        logger.info { this::can_generate_field_modification_events.name + " = " + can_generate_field_modification_events }
        logger.info { this::can_generate_frame_pop_events.name + " = " + can_generate_frame_pop_events }
        logger.info { this::can_generate_garbage_collection_events.name + " = " + can_generate_garbage_collection_events }
        logger.info { this::can_generate_method_entry_events.name + " = " + can_generate_method_entry_events }
        logger.info { this::can_generate_method_exit_events.name + " = " + can_generate_method_exit_events }
        logger.info { this::can_generate_monitor_events.name + " = " + can_generate_monitor_events }
        logger.info { this::can_generate_native_method_bind_events.name + " = " + can_generate_native_method_bind_events }
        logger.info { this::can_generate_object_free_events.name + " = " + can_generate_object_free_events }
        logger.info { this::can_generate_resource_exhaustion_heap_events.name + " = " + can_generate_resource_exhaustion_heap_events }
        logger.info { this::can_generate_resource_exhaustion_threads_events.name + " = " + can_generate_resource_exhaustion_threads_events }
        logger.info { this::can_generate_single_step_events.name + " = " + can_generate_single_step_events }
        logger.info { this::can_generate_vm_object_alloc_events.name + " = " + can_generate_vm_object_alloc_events }
        logger.info { this::can_get_bytecodes.name + " = " + can_get_bytecodes }
        logger.info { this::can_get_constant_pool.name + " = " + can_get_constant_pool }
        logger.info { this::can_get_current_contended_monitor.name + " = " + can_get_current_contended_monitor }
        logger.info { this::can_get_current_thread_cpu_time.name + " = " + can_get_current_thread_cpu_time }
        logger.info { this::can_get_line_numbers.name + " = " + can_get_line_numbers }
        logger.info { this::can_get_monitor_info.name + " = " + can_get_monitor_info }
        logger.info { this::can_get_owned_monitor_info.name + " = " + can_get_owned_monitor_info }
        logger.info { this::can_get_owned_monitor_stack_depth_info.name + " = " + can_get_owned_monitor_stack_depth_info }
        logger.info { this::can_get_source_debug_extension.name + " = " + can_get_source_debug_extension }
        logger.info { this::can_get_source_file_name.name + " = " + can_get_source_file_name }
        logger.info { this::can_get_synthetic_attribute.name + " = " + can_get_synthetic_attribute }
        logger.info { this::can_get_thread_cpu_time.name + " = " + can_get_thread_cpu_time }
        logger.info { this::can_maintain_original_method_order.name + " = " + can_maintain_original_method_order }
        logger.info { this::can_pop_frame.name + " = " + can_pop_frame }
        logger.info { this::can_redefine_any_class.name + " = " + can_redefine_any_class }
        logger.info { this::can_redefine_classes.name + " = " + can_redefine_classes }
        logger.info { this::can_retransform_any_class.name + " = " + can_retransform_any_class }
        logger.info { this::can_retransform_classes.name + " = " + can_retransform_classes }
        logger.info { this::can_set_native_method_prefix.name + " = " + can_set_native_method_prefix }
        logger.info { this::can_signal_thread.name + " = " + can_signal_thread }
        logger.info { this::can_suspend.name + " = " + can_suspend }
        logger.info { this::can_tag_objects.name + " = " + can_tag_objects }

    }
}

@ExperimentalUnsignedTypes
fun checkEx(errCode: jvmtiError, funName: String): jvmtiError {
    if (errCode == 0.toUInt()) {
        return 0.toUInt()
    }
    val message = errorMapping[errCode]
    if (message != null)
        jvmtiWarnings.warn { "$funName: $message" }
    return errCode
}

@SharedImmutable
val errorMapping = mapOf(
    JVMTI_ERROR_NULL_POINTER to "Pointer is unexpectedly NULL.",
    JVMTI_ERROR_OUT_OF_MEMORY to "The function attempted to allocate memory and no more memory was available for allocation.",
    JVMTI_ERROR_ACCESS_DENIED to "The desired functionality has not been enabled in this virtual machine.",
    JVMTI_ERROR_UNATTACHED_THREAD to "The thread being used to call this function is not attached to the virtual machine. Calls must be made from attached threads. See AttachCurrentThread in the JNI invocation API.",
    JVMTI_ERROR_INVALID_ENVIRONMENT to "The JVM TI environment provided is no longer connected or is not an environment.",
    JVMTI_ERROR_WRONG_PHASE to "The desired functionality is not available in the current phase. Always returned if the virtual machine has completed running.",
    JVMTI_ERROR_INTERNAL to "An unexpected internal error has occurred.",
    JVMTI_ERROR_INVALID_PRIORITY to "Invalid priority.",
    JVMTI_ERROR_THREAD_NOT_SUSPENDED to "Thread was not suspended.",
    JVMTI_ERROR_THREAD_SUSPENDED to "Thread already suspended.",
    JVMTI_ERROR_THREAD_NOT_ALIVE to "This operation requires the thread to be alive--that is, it must be started and not yet have died.",
    JVMTI_ERROR_CLASS_NOT_PREPARED to "The class has been loaded but not yet prepared.",
    JVMTI_ERROR_NO_MORE_FRAMES to "There are no Java programming language or JNI stack frames at the specified depth.",
    JVMTI_ERROR_OPAQUE_FRAME to "Information about the frame is not available (e.g. for native frames).",
    JVMTI_ERROR_DUPLICATE to "Item already set.",
    JVMTI_ERROR_NOT_FOUND to "Desired element (e.g. field or breakpoint) not found",
    JVMTI_ERROR_NOT_MONITOR_OWNER to "This thread doesn't own the raw monitor.",
    JVMTI_ERROR_INTERRUPT to "The call has been interrupted before completion.",
    JVMTI_ERROR_UNMODIFIABLE_CLASS to "The class cannot be modified.",
    JVMTI_ERROR_NOT_AVAILABLE to "The functionality is not available in this virtual machine.",
    JVMTI_ERROR_ABSENT_INFORMATION to "The requested information is not available.",
    JVMTI_ERROR_INVALID_EVENT_TYPE to "The specified event type ID is not recognized.",
    JVMTI_ERROR_NATIVE_METHOD to "The requested information is not available for native method.",
    JVMTI_ERROR_CLASS_LOADER_UNSUPPORTED to "The class loader does not support this operation.",
    JVMTI_ERROR_INVALID_THREAD to "The passed thread is not a valid thread.",
    JVMTI_ERROR_INVALID_FIELDID to "Invalid field.",
    JVMTI_ERROR_INVALID_METHODID to "Invalid method.",
    JVMTI_ERROR_INVALID_LOCATION to "Invalid location.",
    JVMTI_ERROR_INVALID_OBJECT to "Invalid object.",
    JVMTI_ERROR_INVALID_CLASS to "Invalid class.",
    JVMTI_ERROR_TYPE_MISMATCH to "The variable is not an appropriate type for the function used.",
    JVMTI_ERROR_INVALID_SLOT to "Invalid slot.",
    JVMTI_ERROR_MUST_POSSESS_CAPABILITY to "The capability being used is false in this environment.",
    JVMTI_ERROR_INVALID_THREAD_GROUP to "Thread group invalid.",
    JVMTI_ERROR_INVALID_MONITOR to "Invalid raw monitor.",
    JVMTI_ERROR_ILLEGAL_ARGUMENT to "Illegal argument.",
    JVMTI_ERROR_INVALID_TYPESTATE to "The state of the thread has been modified, and is now inconsistent.",
    JVMTI_ERROR_UNSUPPORTED_VERSION to "A new class file has a version number not supported by this VM.",
    JVMTI_ERROR_INVALID_CLASS_FORMAT to "A new class file is malformed (the VM would return a ClassFormatError).",
    JVMTI_ERROR_CIRCULAR_CLASS_DEFINITION to "The new class file definitions would lead to a circular definition (the VM would return a ClassCircularityError).",
    JVMTI_ERROR_UNSUPPORTED_REDEFINITION_METHOD_ADDED to "A new class file would require adding a method.",
    JVMTI_ERROR_UNSUPPORTED_REDEFINITION_SCHEMA_CHANGED to "A new class version changes a field.",
    JVMTI_ERROR_FAILS_VERIFICATION to "The class bytes fail verification.",
    JVMTI_ERROR_UNSUPPORTED_REDEFINITION_HIERARCHY_CHANGED to "A direct superclass is different for the new class version, or the set of directly implemented interfaces is different.",
    JVMTI_ERROR_UNSUPPORTED_REDEFINITION_METHOD_DELETED to "A new class version does not declare a method declared in the old class version.",
    JVMTI_ERROR_NAMES_DONT_MATCH to "The class name defined in the new class file is different from the name in the old class object.",
    JVMTI_ERROR_UNSUPPORTED_REDEFINITION_CLASS_MODIFIERS_CHANGED to "A new class version has different modifiers.",
    JVMTI_ERROR_UNSUPPORTED_REDEFINITION_METHOD_MODIFIERS_CHANGED to "A method in the new class version has different modifiers than its counterpart in the old class version."
)


