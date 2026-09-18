package dev.soncresityindustries.schat.manager.installer.step.download

import androidx.compose.runtime.Stable
import dev.soncresityindustries.schat.manager.R
import dev.soncresityindustries.schat.manager.installer.step.StepRunner
import dev.soncresityindustries.schat.manager.installer.step.download.base.DownloadStep
import java.io.File

/**
 * Downloads the SChat Xposed module
 *
 * https://github.com/Soncresity-Industries/SChat-Xposed
 */
@Stable
class DownloadSChatStep(
    workingDir: File
) : DownloadStep() {

    override val nameRes = R.string.step_dl_vd

    override val url: String =
        "https://github.com/Soncresity-Industries/SChat-Xposed/releases/latest/download/app-release.apk"
    override val destination = File("/data/local/tmp/xposed.apk")
    override val workingCopy = File("/data/local/tmp/xposed_work.apk")

    override suspend fun run(runner: StepRunner) {
        runner.logger.i("Using local SChat-Xposed module")
        destination.copyTo(workingCopy, true)
        status = dev.soncresityindustries.schat.manager.installer.step.StepStatus.SUCCESSFUL
    }

}