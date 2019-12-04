package com.mekpap.mekPap.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

public object ServiceUtil{
    private val firestoreInstance : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private  val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid}")
    fun getFCMRegistrationTokens(onComplete: (tokens:MutableList<String>) -> Unit){
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)!!
            onComplete(user.registrationTokens)
        }
    }
    fun setFCMRegistrationTokens(registrationTokens:MutableList<String>){
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }

    fun removeRegistrationToken(){
        val nullTokens = mutableListOf<String>()
        currentUserDocRef.update(mapOf("registrationTokens" to nullTokens))
    }
}