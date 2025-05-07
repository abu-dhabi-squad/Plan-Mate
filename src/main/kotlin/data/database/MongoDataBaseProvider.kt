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
    private val client: MongoClient by lazy { MongoClients.create(connectionString) }
    private val database: MongoDatabase by lazy { client.getDatabase(databaseName) }

    fun getCollection(collectionName: String): MongoCollection<Document> {
        return database.getCollection(collectionName)
            ?: throw IllegalStateException("Database connection is not initialized.")
    }

}
