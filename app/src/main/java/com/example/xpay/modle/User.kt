package com.example.xpay.modle

class User {
    private var uid=""
    private var name=""
    private var phoneNumber=""
    private var profileImage=""

    constructor()
    constructor(uid: String, name: String, phoneNumber: String, profileImage: String) {
        this.uid = uid
        this.name = name
        this.phoneNumber = phoneNumber
        this.profileImage = profileImage
    }


}