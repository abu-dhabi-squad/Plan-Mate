package presentation.presentation.utils

import presentation.io.InputReader
import presentation.io.Printer
import presentation.logic.utils.DateParser
import java.time.LocalDate

class PromptService(
    private val printer: Printer,
    private val reader: InputReader,
    private val dateParser: DateParser
) {

    fun promptNonEmptyInt(prompt: String): Int {
        while (true) {
            printer.display(prompt)
            val input = reader.readInt()
            if (input != null) return input
            printer.displayLn("\nInput cannot be empty.")
        }
    }

    fun promptNonEmptyString(prompt: String): String {
        while (true) {
            printer.display(prompt)
            val input = reader.readString()
            if (!input.isNullOrBlank()) return input
            printer.displayLn("\nInput cannot be empty.")
        }
    }

    fun promptDate(prompt: String): LocalDate {
        while (true) {
            printer.display(prompt)
            val input = reader.readString()
            if (input.isNullOrBlank()) {
                printer.displayLn("\nDate cannot be empty.")
                continue
            }
            try {
                return dateParser.parseDateFromString(input)
            } catch (exception: Exception) {
                printer.displayLn("\nInvalid date format. Please use YYYY-MM-DD.")
            }
        }
    }

    fun promptSelectionIndex(prompt: String, size: Int): Int {
        while (true) {
            printer.display("$prompt (1–$size): ")
            val input = reader.readInt()
            if (input != null && input in 1..size) {
                return input - 1
            }
            printer.displayLn("\nPlease enter a number between 1 and $size.")
        }
    }

    fun promptString(message: String, currentValue: String): String {
        printer.display(message)
        val input = reader.readString()
        return if (input.isNullOrBlank()) currentValue else input
    }

    fun promptDate(message: String, currentValue: LocalDate): LocalDate {
        printer.display(message)
        val input = reader.readString()
        return if (input.isNullOrBlank()) {
            currentValue
        } else {
            try {
                dateParser.parseDateFromString(input)
            } catch (exception: Exception) {
                printer.displayLn("\nInvalid date format. Keeping current value.")
                currentValue
            }
        }
    }

    fun promptSelectionIndex(prompt: String, size: Int, currentValue: Int): Int {
        while (true) {
            printer.display("$prompt (1–$size): ")
            val input = reader.readInt() ?: return currentValue
            if (input in 1..size) {
                return input - 1
            }
            printer.displayLn("\nPlease enter a number between 1 and $size.")
        }
    }
}
