package com.example.tabelafipe

class Veiculo(name_:String, id_:String){
    var name = name_
    var id = id_

    override fun toString():String{
        if(name == null){
            return ""
        }
        else{
            return name!!
        }
    }
}