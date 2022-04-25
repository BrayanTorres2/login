package com.login.firestore

import Mundo.Libros
import Mundo.Tiendas_objeto
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.config.GservicesValue.value
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class productos : AppCompatActivity() {

    // [START storage_field_declaration]
    val storage = Firebase.storage
    // [END storage_field_declaration]
    // Create a storage reference from our app


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)
        var storageRef = storage.reference
        // Create a reference to a la carpeta que creados con los libros
        val libro_con = ArrayList<String>()
        val listRef = storage.reference.child("libros")
        listRef.listAll()
            .addOnSuccessListener {
                for(i in it.items) {
                    libro_con.add(i.name+"")
                }
                Log.d("Firebase", "Files $libro_con")
            }

            .addOnFailureListener {
                Log.d("Firebase", "Error $it")
            }
        //----------------------------------------------------
        val boton_refrescar=findViewById<Button>(R.id.bt_refrescar_ap)
        boton_refrescar.setOnClickListener{
            val lista_de_productos= findViewById<ListView>(R.id.listview_ap_productos)
            val x=Tiendas_objeto.mostrar_libros()
            val adaptCuentas = ArrayAdapter(this,android.R.layout.simple_list_item_1,
                libro_con)
            lista_de_productos.adapter= adaptCuentas
            lista_de_productos.setOnItemClickListener(){parent,view,position,id->
                var nombre_libro=parent.getItemAtPosition(position).toString()
                Toast.makeText(this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show()
                val intent= Intent(this,leer_pdf::class.java)
                intent.putExtra("TITULO LIBRO",nombre_libro)
                startActivity(intent)
            }


        }


    }
}


