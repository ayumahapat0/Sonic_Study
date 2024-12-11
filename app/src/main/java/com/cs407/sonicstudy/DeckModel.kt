package com.cs407.sonicstudy

class DeckModel (private var deck_name: String) {

    // Getter and Setter
    fun get_deck_name(): String {
        return deck_name
    }

    fun set_deck_name(course_name: String) {
        this.deck_name = course_name
    }

}
