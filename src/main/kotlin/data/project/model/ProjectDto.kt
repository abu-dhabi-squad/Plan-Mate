package data.project.model

import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.*

data class ProjectDto(
    val id: String = UUID.randomUUID().toString(),
    @BsonProperty("projectName")
    val projectName: String,
    @BsonProperty("states")
    val states: List<StateDto>
)

