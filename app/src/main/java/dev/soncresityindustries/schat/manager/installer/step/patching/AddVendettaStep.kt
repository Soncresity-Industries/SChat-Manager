package dev.soncresityindustries.schat.manager.installer.step.patching

import dev.soncresityindustries.schat.manager.R
import dev.soncresityindustries.schat.manager.installer.step.Step
import dev.soncresityindustries.schat.manager.installer.step.StepGroup
import dev.soncresityindustries.schat.manager.installer.step.StepRunner
import dev.soncresityindustries.schat.manager.installer.step.download.DownloadSChatStep
import dev.soncresityindustries.schat.manager.installer.util.Patcher
import java.io.File

/**
 * Uses LSPatch to inject the Vendetta XPosed module into Discord
 *
 * @param signedDir The signed apks to patch
 * @param lspatchedDir Output directory for LSPatch
 */
class AddVendettaStep(
    private val signedDir: File,
    private val lspatchedDir: File
) : Step() {

    override val group = StepGroup.PATCHING
    override val nameRes = R.string.step_add_vd

    override suspend fun run(runner: StepRunner) {
        val vendetta = runner.getCompletedStep<DownloadSChatStep>().workingCopy

        runner.logger.i("Adding SChatXposed module with LSPatch")
        val files = signedDir.listFiles()
            ?.takeIf { it.isNotEmpty() }
            ?: throw Error("Missing APKs from signing step")

        Patcher.patch(
            runner.logger,
            outputDir = lspatchedDir,
            apkPaths = files.map { it.absolutePath },
            embeddedModules = listOf(vendetta.absolutePath)
        )
    }

}