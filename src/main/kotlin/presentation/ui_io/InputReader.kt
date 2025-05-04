package presentation.ui_io

interface InputReader {
    fun readString(): String?
    fun readFloat(): Float?
    fun readInt(): Int?
}