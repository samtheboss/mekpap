package com.mekpap.mekPap.service

data class User(val User_name:String,val email:String,val phone:String,val registrationTokens:MutableList<String>){
    constructor():this("","","", mutableListOf<String>())
}