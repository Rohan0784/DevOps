package com.napier.devops;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        // Connect to MongoDB
        MongoClient mongoClient = new MongoClient("mongo-dbserver");

        // Get database and collection
        MongoDatabase database = mongoClient.getDatabase("mydb");
        MongoCollection<Document> collection = database.getCollection("test");

        // Create a document
        Document doc = new Document("name", "Kevin Sim")
                .append("class", "DevOps")
                .append("year", "2024")
                .append("result", new Document("CW", 95).append("EX", 85));

        // Insert and query
        collection.insertOne(doc);
        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());

        // Close connection
        mongoClient.close();
    }
}