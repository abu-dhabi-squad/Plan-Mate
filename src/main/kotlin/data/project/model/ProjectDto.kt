package data.project.model

import org.bson.codecs.pojo.annotations.BsonId

data class ProjectDto(
    @BsonId
    val _id: String,
    val projectName: String,
    val states: List<StateDto>
)