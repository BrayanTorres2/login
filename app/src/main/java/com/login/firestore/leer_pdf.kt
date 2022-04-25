package com.login.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class leer_pdf : AppCompatActivity() {
    val storage = Firebase.storage
    val ONE_MEGABYTE: Long = 1024 * 1024*90

    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "leer_pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leer_pdf)
        //Publicidad de gogle AdMob

        MobileAds.initialize(this) {}
        var adRequest = AdRequest.Builder().build()
        //en ese codigo de cap-app-pub deberiamos agregar el que genera google admod
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                Log.d(TAG, "EL ANUNCION NO CARGA :(")
                mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "EL ANUNCIO ESTA CARGADO")
                mInterstitialAd = interstitialAd
            }
        })

        //Leer el pdf de storageRef
        var nombre_libro=intent.getStringExtra("TITULO LIBRO").toString()
        var proceesbar = findViewById<ProgressBar>(R.id.progressBar)
        var pdf = findViewById<PDFView>(R.id.pdf_viewer)
        val Ref = storage.reference.child("libros")
        Log.d("Firebase", "Files $nombre_libro")
        Ref.child(nombre_libro).getBytes(ONE_MEGABYTE).addOnSuccessListener {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            } else {
                Log.d("TAG", "El anuncio intersticial aún no estaba listo.")
            }
            pdf.fromBytes(it).load()
            proceesbar.visibility=View.INVISIBLE
        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(this,"No se pudo descargar",Toast.LENGTH_LONG).show()
        }


    }

}