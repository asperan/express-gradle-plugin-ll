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
        getAll {
            """
                response.send("get all projects");
            """
        }

        post {
            """
                response.send("post a new project");
            """
        }

        getOne {
            """
                response.send("get project details for id: " + request.params.${resourceName}Id);
            """
        }

        put {
            """
                response.send("put project details for id: " + request.params.${resourceName}Id);
            """
        }

        delete {
            """
                response.send("delete project for id: " + request.params.${resourceName}Id);
            """
        }
    }
}
