package cn.wj.plugin.gitcommit

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.regex.Pattern
import java.util.stream.Collectors

class GitLogQuery(private val workingDirectory: File) {

    fun execute(): Result {
        return try {
            val processBuilder: ProcessBuilder
            val osName = System.getProperty("os.name")
            processBuilder = if (osName.contains("Windows")) {
                ProcessBuilder("cmd", "/C", GIT_LOG_COMMAND)
            } else {
                ProcessBuilder("sh", "-c", GIT_LOG_COMMAND)
            }
            val process = processBuilder
                .directory(workingDirectory)
                .start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.lines().collect(Collectors.toList())
            process.waitFor(2, TimeUnit.SECONDS)
            process.destroy()
            process.waitFor()
            Result(process.exitValue(), output)
        } catch (e: InterruptedException) {
            Result.ERROR
        }
    }

    companion object {
        private const val GIT_LOG_COMMAND = "git log --all --format=%s"
        private val COMMIT_FIRST_LINE_FORMAT = Pattern.compile("^[a-z]+\\((.+)\\):.*")
    }

    data class Result(val exitValue: Int, val logs: MutableList<String> = arrayListOf()) {

        fun isSuccess(): Boolean {
            return exitValue == 0
        }

        fun getScopes(): Set<String> {
            val scopes = hashSetOf<String>()
            logs.forEach(
                Consumer { s: String? ->
                    val matcher = COMMIT_FIRST_LINE_FORMAT.matcher(s.orEmpty())
                    if (matcher.find()) {
                        scopes.add(matcher.group(1))
                    }
                }
            )
            return scopes
        }

        companion object {
            val ERROR = Result(-1)
        }
    }
}
