package data.utils.databaseprovider

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import java.lang.System.getProperty

class MongoProvider {
    companion object {
        private var instance: MongoDatabase? = null

        fun getMongoDatabase(): MongoDatabase {
            if (instance == null) {
                if (getProperty("MONGO_URI") == null || getProperty("MONGO_DB_NAMEe") == null)
                    throw Exception("Mongo url or mongo database name are not found")
                val mongoUri: String = getProperty("MONGO_URI")
                val mongoDbName: String = getProperty("MONGO_DB_NAME")
                val client = MongoClient.create(mongoUri)
                instance = client.getDatabase(mongoDbName)
            }
            return instance ?: throw Exception("Data base not found")
        }
    }
}