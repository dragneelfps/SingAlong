package com.nooblabs.singalong.Models

data class Song(var title: String, var artist: String, var duration: Duration, var path: String) {
    //    override fun toString() = "$title by $artist : ${duration.minutes}:${duration.seconds}"
    override fun toString() = path
}

class Duration(val millis: Long) {
    val minutes = Math.floor(millis.toDouble() / 60000).toLong()
    val seconds = Math.ceil((millis.toDouble() / 1000) % 60).toInt()
    override fun toString() = "$minutes:$seconds"
}
