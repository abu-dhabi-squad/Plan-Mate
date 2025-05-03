package presentation.ui_io

class ConsoleReader: InputReader {
    override fun readString(): String? {
        return readlnOrNull()
    }

    override fun readFloat(): Float? {
        return readlnOrNull()?.toFloatOrNull()
    }

    override fun readInt(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }
}