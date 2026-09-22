package com.napier.devops;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://mongo-dbserver:27017")) {
            MongoDatabase database = mongoClient.getDatabase("mydb");
            MongoCollection<Document> collection = database.getCollection("test");
            Document document = new Document("name", "Lab 02")
                    .append("class", "DevOps");
            collection.insertOne(document);

            Document saved = collection.find(new Document("_id", document.getObjectId("_id"))).first();
            if (saved == null) {
                throw new IllegalStateException("MongoDB did not return the inserted document");
            }
            System.out.println(saved.toJson());
        }
    }
}
