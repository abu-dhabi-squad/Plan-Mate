package squad.abudhabi.data.utils.mapper

interface Mapper<T> {
    fun  map(data: List<String>): T
}