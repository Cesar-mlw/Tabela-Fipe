package com.example.tabelafipe

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    var marcaArray = ArrayList<Marca>()
    var veiculoArray = ArrayList<Veiculo>()
    var modeloArray = ArrayList<Modelo>()

    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        progressDialog = ProgressDialog(this)
        progressDialog!!.isIndeterminate = true
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setMessage("Por favor, aguarde. Carregando...")

        puxarMarcas()

        spMarca.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                puxarVeiculo()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }

        spVeiculo.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                puxarModelo()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
    }

    fun consultarPrecoClick(v: View){
        consultarPreco()
    }

    fun consultarPreco(){
        progressDialog!!.show()
        val marca = spMarca.selectedItem as Marca
        val veiculo = spVeiculo.selectedItem as Veiculo
        val modelo = spModelo.selectedItem as Modelo
        val URL = "http://fipeapi.appspot.com/api/1/carros/veiculo/${marca.id}/${veiculo.id}/${modelo.id}.json"
        val req = JsonObjectRequest(URL, null, PrecoRequisicao(), ErroRequisicao())
        val queue = Volley.newRequestQueue(this)
        queue.add(req)
    }

    fun puxarMarcas(){
        progressDialog!!.show()
        val URL = "http://fipeapi.appspot.com/api/1/carros/marcas.json"
        val req = JsonArrayRequest(URL, MarcaRequisicao(), ErroRequisicao())
        val queue = Volley.newRequestQueue(this)
        queue.add(req)
    }

    fun puxarVeiculo() {
        progressDialog!!.show()
        val marca = spMarca.selectedItem as Marca
        val URL = "http://fipeapi.appspot.com/api/1/carros/veiculos/${marca.id}.json"
        val req = JsonArrayRequest(URL, VeiculoRequisicao(), ErroRequisicao())
        val queue = Volley.newRequestQueue(this)
        queue.add(req)
    }

    fun puxarModelo() {
        progressDialog!!.show()
        val marca = spMarca.selectedItem as Marca
        val veiculo = spVeiculo.selectedItem as Veiculo
        val URL = "http://fipeapi.appspot.com/api/1/carros/veiculo/${marca.id}/${veiculo.id}.json "
        val req = JsonArrayRequest(URL, ModeloRequisicao(), ErroRequisicao())
        val queue = Volley.newRequestQueue(this)
        queue.add(req)
    }
    inner class ErroRequisicao: Response.ErrorListener {
        override fun onErrorResponse(error: VolleyError?) {
            progressDialog!!.dismiss()
            Log.e("Erro Requisicao", error!!.message)
            Log.e("Erro Requisicao", error!!.toString())
        }
    }

    inner class ModeloRequisicao(): Response.Listener<JSONArray> {
        override fun onResponse(response: JSONArray?) {
            var spinner = findViewById<Spinner>(R.id.spModelo)
            progressDialog!!.dismiss()
            if (response == null || response.length() == 0) {
                modeloArray.clear()
                var adapter = ArrayAdapter<Modelo>(this@MainActivity, android.R.layout.simple_spinner_item, modeloArray)
                spinner!!.adapter = adapter
            } else {
                modeloArray.clear()
                for (i in 0 until (response.length() - 1)) {
                    modeloArray.add(Modelo(response.getJSONObject(i).getString("name"),
                                           response.getJSONObject(i).getString("id")))
                }

            }
            var adapter = ArrayAdapter<Modelo>(this@MainActivity, android.R.layout.simple_spinner_item, modeloArray)
            spinner!!.adapter = adapter
        }
    }

    inner class PrecoRequisicao(): Response.Listener<JSONObject>{
        override fun onResponse(response: JSONObject?) {
            progressDialog!!.dismiss()
            if(response == null || response.length() == 0){
                Toast.makeText(this@MainActivity, "Preço não encontrado", Toast.LENGTH_LONG).show()

            }else {
                Toast.makeText(this@MainActivity, response.getString("preco"), Toast.LENGTH_LONG).show()
            }

        }
    }

    inner class VeiculoRequisicao(): Response.Listener<JSONArray>{
        override fun onResponse(response: JSONArray?) {
            var spinner = findViewById<Spinner>(R.id.spVeiculo)
            progressDialog!!.dismiss()
            if(response == null || response.length() == 0){
                veiculoArray.clear()
                var adapter = ArrayAdapter<Veiculo>(this@MainActivity, android.R.layout.simple_spinner_item, veiculoArray)
                spinner!!.adapter = adapter
            }else {
                veiculoArray.clear()
                for (i in 0 until (response!!.length() - 1)) {
                    veiculoArray.add(
                        Veiculo(
                            response.getJSONObject(i).getString("name"),
                            response.getJSONObject(i).getString("id")
                        )
                    )
                }
            }

            var adp = ArrayAdapter<Veiculo>(this@MainActivity, android.R.layout.simple_spinner_item, veiculoArray)



            spinner.adapter = adp

            puxarModelo()
        }
    }



    inner class MarcaRequisicao(): Response.Listener<JSONArray>{
        override fun onResponse(response: JSONArray?){
            progressDialog!!.dismiss()

            var spinner = findViewById<Spinner>(R.id.spMarca)

            if (response == null || response.length() == 0) {
                marcaArray.clear()
                var adapter = ArrayAdapter<Marca>(this@MainActivity, android.R.layout.simple_spinner_item, marcaArray)
                spinner!!.adapter = adapter
            } else {
                for(i in 0 until response.length()){
                    marcaArray.add(Marca(response.getJSONObject(i).getString("name"),
                                         response.getJSONObject(i).getString("fipe_name"),
                                         response.getJSONObject(i).getInt("id")))
                }

            }

            var adp = ArrayAdapter<Marca>(this@MainActivity, android.R.layout.simple_spinner_item, marcaArray)

            spinner.adapter = adp

            puxarVeiculo()

        }


    }
}
