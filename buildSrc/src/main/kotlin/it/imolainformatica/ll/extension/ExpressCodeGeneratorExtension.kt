package it.imolainformatica.ll.extension

import it.imolainformatica.ll.spec.RouteSpec

open class ExpressCodeGeneratorExtension {
    var baseAppSourceFolder = "./src"
    var mainRouteFileName = "main.ts"
    var routesFolder = "routes"
        set(value) {
            if (value == this.controllerFolder) {
                throw IllegalStateException("The routes folder and the controller folder cannot have the same path.")
            } else {
                field = value
            }
        }
    var controllerFolder = "controllers"
        set(value) {
            if (value == this.routesFolder) {
                throw IllegalStateException("The routes folder and the controller folder cannot have the same path.")
            } else {
                field = value
            }
        }

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