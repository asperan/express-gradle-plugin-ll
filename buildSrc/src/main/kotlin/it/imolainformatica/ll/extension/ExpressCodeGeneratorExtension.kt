package it.imolainformatica.ll.extension

import it.imolainformatica.ll.spec.RouteSpec

open class ExpressCodeGeneratorExtension {
    var baseAppSourceFolder = "./src"
    var mainRouteFileName = "main.ts"
    var routesFolder = "routes"
    var controllerFolder = "controllers"

    var routeSpecs: MutableList<RouteSpec> = mutableListOf()
        private set

    fun route(path: String, action: RouteSpec.() -> Unit) =
        routeSpecs.add(
            RouteSpec(path)
                .apply(action)
                .also {
                    if (it.resourceName.isEmpty()) {
                        throw IllegalStateException("The resource name must be defined for path ${it.routePath}")
                    }
                }
        )
}