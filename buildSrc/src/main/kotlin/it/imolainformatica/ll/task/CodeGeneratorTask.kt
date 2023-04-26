package it.imolainformatica.ll.task

import it.imolainformatica.ll.extension.ExpressCodeGeneratorExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal

open class CodeGeneratorTask : DefaultTask() {
    @Internal
    var configuration: ExpressCodeGeneratorExtension = ExpressCodeGeneratorExtension()

    fun generateCode() {
        println("Hello from plugin 'it.imolainformatica.ll.ecg-plugin'")
    }
}