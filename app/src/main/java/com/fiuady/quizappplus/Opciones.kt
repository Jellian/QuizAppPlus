package com.fiuady.quizappplus

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


class Opciones : AppCompatActivity() {

    private lateinit var elige_tema: TextView
    private lateinit var todoschek: CheckBox
    private lateinit var ciencia_checkbox: CheckBox
    private lateinit var cine_checkbox: CheckBox
    private lateinit var deporte_checkbox: CheckBox
    private lateinit var musica_checkbox: CheckBox
    private lateinit var arte_checkbox: CheckBox
    private lateinit var videojuegos_checkbox: CheckBox
    private lateinit var spinner: Spinner
    private lateinit var spinnerpistas: Spinner
    var radioGroup: RadioGroup? = null
    private lateinit var switch: Switch
    private lateinit var Alto: RadioButton
    private lateinit var medio: RadioButton
    private lateinit var bajo: RadioButton

    var cont=1
    var adapterQuestions: ArrayAdapter<Int>? = null
    val num_pre = arrayOf(5, 6, 7, 8, 9, 10)
    val preg = arrayListOf<Int>(6, 7, 8, 9, 10)
    val num_pistas = arrayOf(1, 2, 3)
    var adapterpistas: ArrayAdapter<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opciones)
        val dbBuilder: DbBuilder by viewModels()
        val db = dbBuilder.buildBd(this)

        todoschek = findViewById(R.id.todos_checkbox)
        ciencia_checkbox = findViewById(R.id.ciencia_checkbox)
        cine_checkbox = findViewById(R.id.cine_checkbox)
        deporte_checkbox = findViewById(R.id.deporte_checkbox)
        musica_checkbox = findViewById(R.id.musica_checkbox)
        arte_checkbox = findViewById(R.id.arte_checkbox)
        videojuegos_checkbox = findViewById(R.id.videojuegos_checkbox)
        radioGroup = findViewById(R.id.radioGroup1)
        switch = findViewById(R.id.pistas)
        spinner = findViewById(R.id.numero_preguntas)
        spinnerpistas = findViewById(R.id.numero_pistas)
        Alto = findViewById(R.id.nivel_alto)
        medio = findViewById(R.id.nivel_medio)
        bajo = findViewById(R.id.nivel_bajo)

        spinadp()
        val usuario_activo = db.usersDao().getActiveUser()
        val settings = db.settingsDao().getsettings(usuario_activo.id)

        adapterpistas = ArrayAdapter<Int>(this, R.layout.support_simple_spinner_dropdown_item, num_pistas)
        spinnerpistas.setAdapter(adapterpistas)

        cine_checkbox.isChecked = settings.cine != 0
        ciencia_checkbox.isChecked = settings.ciencia != 0
        deporte_checkbox.isChecked = settings.deporte != 0
        musica_checkbox.isChecked = settings.musica != 0
        arte_checkbox.isChecked = settings.arte != 0
        videojuegos_checkbox.isChecked = settings.videojuegos != 0

        if(settings.cine==1 && settings.ciencia==1 && settings.deporte==1 && settings.musica==1 && settings.arte==1 && settings.videojuegos==1){
            todoschek.isChecked=true
            todoschek.isEnabled=false
        }

        when (settings.dificulty) {
            1 -> bajo.isChecked = true
            2 -> medio.isChecked = true
            3 -> Alto.isChecked = true
        }

        if(settings.hints==0){
            switch.isChecked=false
            spinnerpistas.isEnabled=false
        }
        else{
            switch.isChecked=true
            spinnerpistas.isEnabled=true
        }
        spinnerpistas.setSelection(obtenerPosicionItem(spinnerpistas, settings.hintsquantity.toString()));


        if (!cine_checkbox.isChecked && !ciencia_checkbox.isChecked && !deporte_checkbox.isChecked && !arte_checkbox.isChecked && !musica_checkbox.isChecked && videojuegos_checkbox.isChecked || cine_checkbox.isChecked && !ciencia_checkbox.isChecked && !deporte_checkbox.isChecked && !arte_checkbox.isChecked && !musica_checkbox.isChecked && !videojuegos_checkbox.isChecked ||
            !cine_checkbox.isChecked && ciencia_checkbox.isChecked && !deporte_checkbox.isChecked && !arte_checkbox.isChecked && !musica_checkbox.isChecked && !videojuegos_checkbox.isChecked || !cine_checkbox.isChecked && !ciencia_checkbox.isChecked && deporte_checkbox.isChecked && !arte_checkbox.isChecked && !musica_checkbox.isChecked && !videojuegos_checkbox.isChecked ||
            !cine_checkbox.isChecked && !ciencia_checkbox.isChecked && !deporte_checkbox.isChecked && arte_checkbox.isChecked && !musica_checkbox.isChecked && !videojuegos_checkbox.isChecked || !cine_checkbox.isChecked && !ciencia_checkbox.isChecked && !deporte_checkbox.isChecked && !arte_checkbox.isChecked && musica_checkbox.isChecked && !videojuegos_checkbox.isChecked) {
            spinner.isEnabled = false
            spinner.setSelection(0)
        } else {
            spinner.isEnabled = true
        }

        todoschek.setOnClickListener {
            if (todoschek.isChecked==true) {
                todoschek.isEnabled = false
                ciencia_checkbox.isChecked = true
                cine_checkbox.isChecked = true
                deporte_checkbox.isChecked = true
                musica_checkbox.isChecked = true
                arte_checkbox.isChecked = true
                videojuegos_checkbox.isChecked = true
                spinner.setSelection(0)
            }
        }

        cine_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settings.cine=1
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos

            }  else if(cont==1) {
                settings.cine==1
                db.settingsDao().actualizar(settings)
                cine_checkbox.isChecked=true
            }
            else {
                settings.cine=0
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }
            validar()
        }

        ciencia_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settings.ciencia=1
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }else if(cont==1) {
                settings.ciencia==1
                db.settingsDao().actualizar(settings)
                ciencia_checkbox.isChecked=true
            }
            else  {
                settings.ciencia=0
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }
           validar()
        }

        deporte_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settings.deporte=1
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }else if(cont==1) {
                settings.deporte==1
                db.settingsDao().actualizar(settings)
                deporte_checkbox.isChecked=true
            } else {
                settings.deporte=0
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }
            validar()
        }
        arte_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settings.arte=1
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }else if(cont==1) {
                settings.arte==1
                db.settingsDao().actualizar(settings)
                arte_checkbox.isChecked=true
            }
            else  {
                settings.arte=0
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }
            validar()
        }
        musica_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settings.musica=1
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }else if(cont==1) {
                settings.musica==1
                db.settingsDao().actualizar(settings)
                musica_checkbox.isChecked=true
            }
            else   {
                settings.musica=0
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }
            validar()
        }
        videojuegos_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settings.videojuegos=1
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }else if(cont==1) {
                settings.videojuegos==1
                db.settingsDao().actualizar(settings)
                videojuegos_checkbox.isChecked=true
            }
            else  {
                settings.videojuegos=0
                db.settingsDao().actualizar(settings)
                cont=settings.cine+settings.ciencia+settings.deporte+settings.musica+settings.arte+settings.videojuegos
            }
            validar()
        }

        Alto.setOnClickListener {
            settings.dificulty=3
            db.settingsDao().actualizar(settings)
        }
        medio.setOnClickListener {
            settings.dificulty=2
            db.settingsDao().actualizar(settings)
        }
        bajo.setOnClickListener {
            settings.dificulty=1
            db.settingsDao().actualizar(settings)
        }

        switch.setOnCheckedChangeListener { _, _ ->
            if (!switch.isChecked) {
                settings.hints=0
                spinnerpistas.isEnabled = false
                db.settingsDao().actualizar(settings)
            }
            if (switch.isChecked) {
                settings.hints=1
                spinnerpistas.isEnabled = true
                db.settingsDao().actualizar(settings)

            }
        }
            spinnerpistas.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    settings.hintsquantity=num_pistas[position]
                    db.settingsDao().actualizar(settings)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if(cont<6) {
                    settings.questionquantity = num_pre[position]
                    db.settingsDao().actualizar(settings)
                }else if(cont>=6) {
                    settings.questionquantity = preg[position]
                    db.settingsDao().actualizar(settings)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }
    fun obtenerPosicionItem(spinner: Spinner, numero: String?): Int {
        //Creamos la variable posicion y lo inicializamos en 0
        var posicion = 0
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (i in 0 until spinner.count) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equals(numero, ignoreCase = true)) {
                posicion = i
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion
    }

    fun spinadp() {
        if (cont < 6) { val num_pre = arrayOf(5, 6, 7, 8, 9, 10)
            adapterQuestions = ArrayAdapter<Int>(
                this, R.layout.support_simple_spinner_dropdown_item, num_pre)
            spinner.setAdapter(adapterQuestions)

        }

        if (cont >= 6) {
            val preg = arrayListOf<Int>(6, 7, 8, 9, 10)
            adapterQuestions = ArrayAdapter<Int>(this, R.layout.support_simple_spinner_dropdown_item, preg)
            spinner.setAdapter(adapterQuestions)
        }

    }
    fun validar() {
        when (cont) {
            1 -> {
                spinadp()
                todoschek.isEnabled = true
                todoschek.isChecked = false
                spinner.setSelection(0)
                spinner.isEnabled = false
            }
            in 2..5 -> {
                spinadp()
                todoschek.isEnabled = true
                todoschek.isChecked = false
                spinner.setSelection(0)
                spinner.isEnabled = true
            }
            in 6..7 -> {
                spinadp()
                todoschek.isChecked = true
                todoschek.isEnabled = false
                spinner.setSelection(0)
                spinner.isEnabled = true
            }
        }
    }

    

}