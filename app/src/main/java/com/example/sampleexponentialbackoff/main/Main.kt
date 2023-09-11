package com.example.sampleexponentialbackoff.main

fun main() {
    val client = MessageClient()

    // Start the retry client
    client.start()

    // Add messages to the queue for posting
    val messages = listOf(
        Message("Message 1"),
        Message("Message 2"),
        Message("Message 3"),
        Message("Message 4"),
        Message("Message 5"),
        Message("Message 6"),
        Message("Message 7"),
        Message("Message 8"),
        // Add more messages as needed
    )

    // Post messages
    for (message in messages) {
        client.postMessage(message)
    }
}