package com.max.guess.data

class EventResult : ArrayList<Event>()

data class Event(
    val version: String,
    val UID: String,
    val title: String,
    val category: String,
    val showInfo: List<ShowInfo>,
    val showUnit: String,
    val discountInfo: String,
    val descriptionFilterHtml: String,
    val imageUrl: String,
    val masterUnit: List<String>,
    val subUnit: List<Any>,
    val supportUnit: List<Any>,
    val otherUnit: List<Any>,
    val webSales: String,
    val sourceWebPromote: String,
    val comment: String,
    val editModifyDate: String,
    val sourceWebName: String,
    val startDate: String,
    val endDate: String,
    val hitRate: Int
)

data class ShowInfo(
    val time: String,
    val location: String,
    val locationName: String,
    val onSales: String,
    val price: String,
    val latitude: String,
    val longitude: String,
    val endTime: String
)