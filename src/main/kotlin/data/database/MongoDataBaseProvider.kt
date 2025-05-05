package data.database

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document

class MongoDataBaseProvider(private val databaseName: String, private val connectionString: String) {
    private var client: MongoClient? = MongoClients.create(connectionString)
    private var database: MongoDatabase? = client?.getDatabase(databaseName)

    fun getCollection(collectionName: String): MongoCollection<Document> {
        return database!!.getCollection(collectionName)
    }

    fun disconnect() {
        client?.close()
        client = null
        database = null
    }
}