package com.cs407.sonicstudy

class EntryModel(private var term: String, private var definition: String, private var id: String) {
    // Getter and Setter
    fun get_term(): String {
        return term
    }

    fun set_term(termText: String) {
        this.term = termText
    }

    fun get_definition(): String {
        return definition
    }

    fun set_definition(defText: String){
        this.definition = defText
    }

    fun get_id(): String {
        return id
    }

    fun set_id(termId: String) {
        this.id = termId
    }
}