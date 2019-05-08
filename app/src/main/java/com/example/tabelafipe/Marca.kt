package com.example.tabelafipe

class Marca(name_:String, fipe_name_:String, id_:Int) {
    var name = name_
    var fipe_name = fipe_name_

    var id= id_

    override fun toString(): String {
        if (name == null)
            return ""
        else
            return fipe_name!!
    }
}
