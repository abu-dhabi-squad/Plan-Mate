package squad.abudhabi.logic.model

data class Project(
    val id: String,
    val projectName: String,
    val states: List<State>,

)
