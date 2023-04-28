package it.imolainformatica.ll.spec

import java.util.*

open class RouteSpec(val routePath: String) {
    var resourceName = ""

    var genericHandlers: MutableMap<String, String> = mutableMapOf()
        private set

    var specificHandlers: MutableMap<String, String> = mutableMapOf()
        private set

    var sideEffectImports: MutableList<String> = mutableListOf()
        private set

    val additionalImports: MutableList<String> = mutableListOf()

    fun getAll(lazyBody: () -> String) = setGenericHandler("getEvery", lazyBody)
    fun post(lazyBody: () -> String) = setGenericHandler("post", lazyBody)
    fun getOne(lazyBody: () -> String) = setSpecificHandler("get", lazyBody)
    fun put(lazyBody: () -> String) = setSpecificHandler("put", lazyBody)
    fun delete(lazyBody: () -> String) = setSpecificHandler("delete", lazyBody)

    fun importSideEffects(from: String) = sideEffectImports.add(from)

    private fun setGenericHandler(handlerBaseName: String, lazyBody: () -> String) =
        setHandler(genericHandlers, handlerBaseName, lazyBody)

    private fun setSpecificHandler(handlerBaseName: String, lazyBody: () -> String) =
        setHandler(specificHandlers, handlerBaseName, lazyBody)

    private fun setHandler(handlerSet: MutableMap<String, String>, handlerBaseName: String, lazyBody: () -> String) {
        handlerSet[computeHandlerKey(handlerBaseName)] = lazyBody().trimIndent()
    }

    private fun computeHandlerKey(handlerBaseName: String) = handlerBaseName +
            resourceName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    fun import(vararg symbols: String): ImportStatement = when(symbols.size) {
        0 -> throw IllegalStateException("Import statements cannot have 0 arguments")
        1 -> ImportSymbol(symbols[0])
        else -> ImportList(listOf(*symbols))
    }

    infix fun ImportStatement.renamedAs(newName: String): ImportAllRenamed = run {
        if (this !is ImportSymbol || this.symbol != "*") {
            throw IllegalStateException("Cannot rename symbols other than '*'")
        }
        ImportAllRenamed(newName)
    }

    infix fun ImportStatement.from(reference: String) = additionalImports.add(when(this) {
        is ImportAllRenamed -> "* as ${this.newName}"
        is ImportList -> this.symbols.joinToString(", ", "{ ", " }")
        is ImportSymbol -> this.symbol
    } + " from \"${reference}\"")

    sealed class ImportStatement
    class ImportSymbol(val symbol: String) : ImportStatement()
    class ImportList(val symbols: List<String>) : ImportStatement()
    class ImportAllRenamed(val newName: String) : ImportStatement()
}