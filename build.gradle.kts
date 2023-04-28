plugins {
    id("it.imolainformatica.ll.ecg-plugin")
}

express {
    baseAppSourceFolder = "./src"
    mainRouteFileName = "main.ts"
    routesFolder = "routes"
    controllerFolder = "controllers"
    route("/projects") {
        resourceName = "project"
        importSideEffects("test")
        import("test") from "test-file"
        import("*") renamedAs "good" from "test-file"
        getAll {
            """
                
            """
        }

        post {
            """
                
            """
        }

        getOne {
            """
                
            """
        }

        put {
            """
            
            """
        }

        delete {
            """
                
            """
        }
    }
}
