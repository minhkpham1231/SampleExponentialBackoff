package com.example.sampleexponentialbackoff.main

class MessageQueue {
    private val queue = mutableListOf<Message>()

    fun enqueue(message: Message) {
        queue.add(message)
    }

    fun dequeue(): Message? {
        if (queue.isEmpty()) return null
        return queue.removeAt(0)
    }
}