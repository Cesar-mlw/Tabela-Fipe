package com.example.tabelafipe

//"fipe_marca": "Fiat", "fipe_codigo": "2014-1", "name": "2014 Gasolina", "marca": "FIAT", "key": "2014-1", "veiculo": "Palio 1.0 ECONOMY Fire Flex 8V 4p", "id": "2014-1"

class Modelo(name_:String, id_: String){
    var name = name_
    var id = id_
    override fun toString(): String {
        if(name == null){
            return ""
        }
        else{
            return name
        }
    }
}