package dev.soncresityindustries.schat.manager.utils

import androidx.annotation.StringRes
import dev.soncresityindustries.schat.manager.R
import java.io.Serializable

data class DiscordVersion(
    val major: Int,
    val minor: Int,
    val type: Type
) : Serializable, Comparable<DiscordVersion> {

    // https://tracker.vendetta.rocks/tracker/index
    enum class Type(val label: String, @StringRes val labelRes: Int, val maxVersionCode: Int) {
        STABLE("Stable", R.string.channel_stable, 341013),
        BETA("Beta", R.string.channel_beta, 342114),
        ALPHA("Alpha", R.string.channel_alpha, 343206)
    }

    override fun compareTo(other: DiscordVersion): Int =
        toVersionCode().toInt() - other.toVersionCode().toInt()

    override fun toString() = "$major.$minor - ${type.label}"

    fun toVersionCode() = "$major${type.ordinal}${if (minor < 10) 0 else ""}${minor}"

    fun isSupported() = toVersionCode().toInt() <= type.maxVersionCode

    companion object {
        fun fromVersionCode(string: String): DiscordVersion? = with(string) {
            if (length < 4) return@with null
            val versionCodeInt = toIntOrNull() ?: return@with null
            if (versionCodeInt <= 126021) return@with null
            val codeReversed = toCharArray().reversed().joinToString("")
            val typeInt = codeReversed[2].toString().toInt()
            val type = Type.entries.getOrNull(typeInt) ?: return@with null

            DiscordVersion(
                codeReversed.slice(3..codeReversed.lastIndex).reversed().toInt(),
                codeReversed.substring(0, 2).reversed().toInt(),
                type
            )
        }
    }

}
