package com.example.sampleexponentialbackoff.main

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.Executors
import java.util.Random


class MessageClient {
    private val messageQueue = MessageQueue()
    private val maxRetries = 10 // Maximum number of retry attempts
    private val retryDelayMillis = 100L // Initial retry delay in milliseconds
    private val executor = Executors.newSingleThreadExecutor()

    fun start() {
        executor.execute {
            println("MessageQueue started")
            while (true) {
                println("Checking for messages")
                val message = messageQueue.dequeue()
                if (message != null) {
                    println("MessageQueue got a message ${message.content}")
                    var retries = 0
                    var delayMillis = retryDelayMillis
                    while (retries < maxRetries) {
                        try {
                            // Create a variable for a random chance of failure
                            val failed = Random().nextBoolean()
                            if (failed) {
                                println("Failed")
                                throw java.lang.Exception("Failed to make network call")
                            }
                            // Attempt to send the message to the server
                            val response = sendToServerOK(message)
                            if (response.isSuccessful) {
                                // Message sent successfully, break the retry loop
                                println("Respond got from ${message.content}: "+response.body?.string())
                                break
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        // Exponential backoff for retries
                        Thread.sleep(delayMillis)
                        delayMillis *= 2
                        retries++
                    }
                }
            }
        }
    }

    private fun sendToServerOK(message: Message?): okhttp3.Response {
        // Send the request
        // Message is unused in this example, but can be included
        val client = OkHttpClient()
        val request = Request.Builder().url("https://random-data-api.com/api/v2/beers").build()
        return client.newCall(request).execute()
    }

    fun postMessage(message: Message) {
        messageQueue.enqueue(message)
    }
}