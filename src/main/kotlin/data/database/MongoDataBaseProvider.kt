package data.database

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document

class MongoDataBaseProvider(
    private val databaseName: String,
    private val connectionString: String
) {
    private var client: MongoClient? = null
    private var database: MongoDatabase? = null

    init {
        connect()
    }

    private fun connect() {
        client = MongoClients.create(connectionString)
        database = client?.getDatabase(databaseName)
    }

    fun getCollection(collectionName: String): MongoCollection<Document> {
        if (database == null) {
            connect()
        }
        return database?.getCollection(collectionName)
            ?: throw IllegalStateException("Database connection is not initialized.")
    }

    fun disconnect() {
        client?.close()
        client = null
        database = null
    }
}
