package com.napier.devops;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class App {
    public static void main(String[] args) {
        // Using try-with-resources to automatically close the MongoClient connection
        try (MongoClient mongoClient = MongoClients.create("mongodb://mongo-dbserver:27017")) {
            MongoDatabase database = mongoClient.getDatabase("mydb");
            System.out.println("Connected to database: " + database.getName());
        }
    }
}