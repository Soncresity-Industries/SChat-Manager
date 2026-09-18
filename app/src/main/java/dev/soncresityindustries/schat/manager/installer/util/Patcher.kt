package dev.soncresityindustries.schat.manager.installer.util

import com.android.tools.build.apkzlib.zip.AlignmentRules
import com.android.tools.build.apkzlib.zip.ZFile
import com.android.tools.build.apkzlib.zip.ZFileOptions
import com.github.diamondminer88.zip.ZipCompression
import com.github.diamondminer88.zip.ZipReader
import com.github.diamondminer88.zip.ZipWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.lsposed.patch.LSPatch
import org.lsposed.patch.util.Logger
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object Patcher {
    private const val PAGE_ALIGNMENT = 16384

    suspend fun patch(
        logger: Logger,
        outputDir: File,
        apkPaths: List<String>,
        embeddedModules: List<String>
    ) {
        withContext(Dispatchers.IO) {
            LSPatch(
                logger,
                *apkPaths.toTypedArray(),
                "-o",
                outputDir.absolutePath,
                "-l",
                "0",
                "-v",
                "-m",
                *embeddedModules.toTypedArray(),
                "-k",
                Signer.keyStore.absolutePath,
                "password",
                "alias",
                "password"
            ).doCommandLine()

            outputDir.listFiles { file -> file.extension == "apk" }
                ?.forEach { apk ->
                    normalizeAndAlign(apk, logger)
                    resign(apk)
                }
        }
    }

    private fun normalizeAndAlign(apk: File, logger: Logger) {
        val alignedApk = File(apk.parentFile, "${apk.name}.aligned")
        alignedApk.delete()

        try {
            ZipReader(apk).use { reader ->
                ZipWriter(alignedApk, true).use { writer ->
                    reader.entryNames.forEach { name ->
                        val entry = reader.openEntry(name) ?: return@forEach
                        val bytes = entry.read()

                        if (name.endsWith(".so")) {
                            writer.writeEntry(name, bytes, ZipCompression.NONE, PAGE_ALIGNMENT)
                        } else if (name == "resources.arsc") {
                            writer.writeEntry(name, bytes, ZipCompression.NONE, 4096)
                        } else {
                            writer.writeEntry(name, bytes, ZipCompression.NONE)
                        }
                    }
                }
            }
            logger.d("Page-aligned embedded native libraries in ${apk.name} via manual rewrite")
        } catch (e: Exception) {
            logger.e("Failed to align APK: ${e.message}")
            return
        }

        Files.move(
            alignedApk.toPath(),
            apk.toPath(),
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.ATOMIC_MOVE
        )
    }

    private fun resign(apk: File) {
        val signedApk = File(apk.parentFile, "${apk.nameWithoutExtension}.signed.apk")
        signedApk.delete()
        Signer.signApk(apk, signedApk)
        Files.move(
            signedApk.toPath(),
            apk.toPath(),
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.ATOMIC_MOVE
        )
    }
}
