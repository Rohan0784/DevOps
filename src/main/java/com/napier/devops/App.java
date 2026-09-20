package com.napier.devops;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        // Read host from environment variable or fall back to "mongo-dbserver"
        String dbHost = System.getenv("MONGO_HOST") != null ? System.getenv("MONGO_HOST") : "mongo-dbserver";

        // Try-with-resources handles closing the connection automatically
        try (MongoClient mongoClient = new MongoClient(dbHost, 27017)) {

            MongoDatabase database = mongoClient.getDatabase("mydb");
            MongoCollection<Document> collection = database.getCollection("test");

            Document doc = new Document("name", "Kevin Sim")
                    .append("class", "DevOps")
                    .append("year", "2024")
                    .append("result", new Document("CW", 95).append("EX", 85));

            collection.insertOne(doc);

            // Fetch document with null check
            Document myDoc = collection.find().first();
            if (myDoc != null) {
                System.out.println(myDoc.toJson());
            } else {
                System.out.println("No document found in collection.");
            }
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }
}