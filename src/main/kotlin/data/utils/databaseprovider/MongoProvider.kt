package data.utils.databaseprovider

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import logic.exceptions.DatabaseNotFoundException

class MongoProvider {
    companion object {
        private var instance: MongoDatabase? = null

        fun getMongoDatabase(): MongoDatabase {
            if (instance == null) {
                if (System.getenv("MONGO_URI") == null || System.getenv("MONGO_DB_NAME") == null)
                    throw IllegalArgumentException("Mongo url or mongo database name are not found")
                val mongoUri: String = System.getenv("MONGO_URI")
                val mongoDbName: String = System.getenv("MONGO_DB_NAME")
                val client = MongoClient.create(mongoUri)
                instance = client.getDatabase(mongoDbName)
            }
            return instance ?: throw DatabaseNotFoundException()
        }
    }
}