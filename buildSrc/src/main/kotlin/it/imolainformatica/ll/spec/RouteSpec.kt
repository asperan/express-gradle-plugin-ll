package it.imolainformatica.ll.spec

import java.util.*

open class RouteSpec(val routePath: String) {
    var resourceName = ""

    var genericHandlers: MutableMap<String, String> = mutableMapOf()
        private set

    var specificHandlers: MutableMap<String, String> = mutableMapOf()
        private set

    fun getAll(lazyBody: () -> String) = setGenericHandler("getEvery", lazyBody)
    fun post(lazyBody: () -> String) = setGenericHandler("post", lazyBody)
    fun getOne(lazyBody: () -> String) = setSpecificHandler("get", lazyBody)
    fun put(lazyBody: () -> String) = setSpecificHandler("put", lazyBody)
    fun delete(lazyBody: () -> String) = setSpecificHandler("delete", lazyBody)

    private fun setGenericHandler(handlerBaseName: String, lazyBody: () -> String) =
        setHandler(genericHandlers, handlerBaseName, lazyBody)

    private fun setSpecificHandler(handlerBaseName: String, lazyBody: () -> String) =
        setHandler(specificHandlers, handlerBaseName, lazyBody)

    private fun setHandler(handlerSet: MutableMap<String, String>, handlerBaseName: String, lazyBody: () -> String) {
        handlerSet[computeHandlerKey(handlerBaseName)] = lazyBody().trimIndent()
    }

    private fun computeHandlerKey(handlerBaseName: String) = handlerBaseName +
            resourceName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}