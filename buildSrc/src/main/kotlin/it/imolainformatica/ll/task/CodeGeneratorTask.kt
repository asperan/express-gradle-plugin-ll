package it.imolainformatica.ll.task

import it.imolainformatica.ll.extension.ExpressCodeGeneratorExtension
import it.imolainformatica.ll.spec.RouteSpec
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.configurationcache.extensions.capitalized
import java.io.File

open class CodeGeneratorTask : DefaultTask() {
    @Internal
    var configuration: ExpressCodeGeneratorExtension = ExpressCodeGeneratorExtension()

    fun generateCode() {
        configuration.routeSpecs.forEach { generateRouteCode(it) }
        val mainRouteSetterCode = generateMainRouteSetterCode(configuration.routeSpecs)
        prepareRelativeFile("${configuration.baseAppSourceFolder}/${configuration.routesFolder}", "/${configuration.mainRouteFileName}")
            .writeText(mainRouteSetterCode, Charsets.UTF_8)
    }

    private fun generateRouteCode(routeSpec: RouteSpec) {
        val controllerCode =
            """
                import express from "express";
                
                ${routeSpec.sideEffectImports.joinToString("\n") { "import \"${it}\";" }}

                ${routeSpec.additionalImports.joinToString("\n") { "import ${it};"}}
                
            """.trimIndent() +
            (routeSpec.genericHandlers + routeSpec.specificHandlers).map { (key, value) ->
                """
                    export function ${key}(request: express.Request, response: express.Response) {
                    ${value.lines().joinToString("\n") { "\t${it}" }}
                    }
                    
                """.trimIndent()
        }.joinToString("\n")
        val routeSetterCode =
            """
                import express from "express";
                import * as Controller from '../${configuration.controllerFolder}/${routeSpec.resourceName}';
                
                export default function ${generateRouteSetterName(routeSpec)}(app: express.Application): void {
                    app.route("${routeSpec.routePath}")
                    ${routeSpec.genericHandlers.keys.joinToString("\n") {
                        "\t\t.${extractCrudVerb(it).toString().lowercase()}(Controller.${it})"
                    }};
                    app.route("${routeSpec.routePath}/:${routeSpec.resourceName}Id")
                    ${routeSpec.specificHandlers.keys.joinToString("\n") {
                        "\t\t.${extractCrudVerb(it).toString().lowercase()}(Controller.${it})"
                    }};
                }
            """.trimIndent()
        prepareRelativeFile("/${configuration.baseAppSourceFolder}/${configuration.controllerFolder}", "/${routeSpec.resourceName}.ts")
            .writeText(controllerCode, Charsets.UTF_8)
        prepareRelativeFile("/${configuration.baseAppSourceFolder}/${configuration.routesFolder}", "/${routeSpec.resourceName}.ts")
            .writeText(routeSetterCode, Charsets.UTF_8)
    }

    private fun extractCrudVerb(handlerName: String): CrudVerb {
        return if (handlerName.startsWith("get")) {
            CrudVerb.GET
        } else if (handlerName.startsWith("post")) {
            CrudVerb.POST
        } else if (handlerName.startsWith("put")) {
            CrudVerb.PUT
        } else {
            CrudVerb.DELETE
        }
    }

    private enum class CrudVerb {
        GET, POST, PUT, DELETE
    }

    private fun generateRouteSetterName(routeSpec: RouteSpec) =
        "set${routeSpec.resourceName.capitalized()}Routes"

    private fun generateMainRouteSetterCode(routes: List<RouteSpec>): String =
        """
            import express from "express";
            ${routes.joinToString("\n") {
                "import ${generateRouteSetterName(it)} from './${it.resourceName}';"
            }}
            
            export default function setRoutes(app: express.Application) {
                ${routes.joinToString("\n") { generateRouteSetterName(it) }}(app);
            }
        """.trimIndent()

    private fun prepareRelativeFile(folderPath: String, filePath: String): File =
        File("${project.rootDir}/${folderPath}")
            .normalize()
            .also { it.mkdirs() }
            .let { File(it, filePath) }
            .also { it.delete() }
            .also { it.createNewFile() }
}