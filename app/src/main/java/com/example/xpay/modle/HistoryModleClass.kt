package com.example.xpay.modle

data class HistoryModleClass(
    var isReceived:Boolean = false,
    var userName:String = "",
    var dateAndTime:String = "",
    var amount:String = ""
)
/*class HistoryModleClass {
    var isReceived:Boolean = false
    var userName:String = ""
    var dateAndTime:String = ""
    var amount:String = ""

    constructor()
    constructor(isReceived: Boolean, userName: String, dateAndTime: String, amount: String) {
        this.isReceived = isReceived
        this.userName = userName
        this.dateAndTime = dateAndTime
        this.amount = amount
    }

}*/