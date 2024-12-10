package com.cs407.sonicstudy

class DeckModel (private var course_name: String) {

    // Getter and Setter
    fun get_deck_name(): String {
        return course_name
    }

    fun set_deck_name(course_name: String) {
        this.course_name = course_name
    }

}
