package com.example.madetoliveapp

import android.content.Context
import io.appwrite.Client
import io.appwrite.services.Account


class BackEnd(context: Context) {
    private val client = Client(context)
        .setEndpoint("https://cloud.appwrite.io/v1") // Replace with your Appwrite endpoint
        .setProject("66f2fc83001087da40a2") // Replace with your project ID
        .setSelfSigned(true) // Optional for self-signed certificates

    private val account = Account(client)

    // Your other code here...
}