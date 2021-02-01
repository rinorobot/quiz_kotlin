package com.rinosystems.quiz

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var all_questions: Array<String>
    private lateinit var btn_next : Button
    private lateinit var btn_previus : Button
    private lateinit var group:RadioGroup
    private lateinit var ivQuestion : ImageView
    var preguntas_correctas : ArrayList<Int> = ArrayList()
    var preguntas_incorrectas : ArrayList<Int> = ArrayList()



    private lateinit var text_question:TextView
    private val ids_answers = listOf(R.id.answer1,R.id.answer2,R.id.answer3,R.id.answer4)
    private lateinit var tvIndicador : TextView

//Variables a preservar
    private var correct_answer: Int=0
    private var current_question:Int=0
    private lateinit var answer_is_correct : BooleanArray
    private lateinit var answer : IntArray

//para pasar datos cuando se camnbia a landscape, mejor bloquear esa vista
   override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("corrent_answer",correct_answer)
        outState.putInt("current_question",current_question)
        outState.putBooleanArray("answer_is_correct",answer_is_correct)
        outState.putIntArray("answer",answer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text_question = findViewById(R.id.text_question)
        group = answer_group
        btn_next = findViewById(R.id.btn_check)
        btn_previus = findViewById(R.id.btn_previus)
        ivQuestion = findViewById(R.id.ivQuestion)


        all_questions = resources.getStringArray(R.array.all_questions)


            startOver()





        btn_next.setOnClickListener {
         checkAnswer()

            if (current_question<all_questions.size-1){
                current_question++

                showQuestion()

            }else{
                checkResults()


            }

        }



        btn_previus.setOnClickListener {
            checkAnswer()
            if (current_question > 0){
                current_question--
                showQuestion()
            }
        }



    }

    private fun startOver() {
        answer_is_correct = BooleanArray(all_questions.size)
        answer = IntArray(all_questions.size)

        for (i in 0..answer.size - 1) {
            answer[i] = -1
        }

        current_question = 0

        preguntas_incorrectas.clear()
       preguntas_correctas.clear()
        showQuestion()
    }

    private fun checkResults() {
        var correctas = 0
        var incorrectas = 0
        var nocontestadas = 0
        for (i in 0..all_questions.size-1) {
            if (answer_is_correct[i]) correctas++
            else if (answer[i] == -1) nocontestadas++
            else incorrectas++
        }



        val builder = AlertDialog.Builder(this)

        if (correctas>4) {
            builder.setIcon(R.drawable.icon_muy_bien)
            builder?.setTitle("Muy bien")
        }
        else if (correctas<4&&correctas>2){
            builder.setIcon(R.drawable.icon_regular)
            builder?.setTitle("Puedes mejorar")
        }else{
            builder.setIcon(R.drawable.icon_mal)
            builder?.setTitle("Estudiando puedes mejorar")
        }





        builder.setMessage("Calificación: ${correctas}\n" +"Preguntas correctas: ${preguntas_correctas} \nPreguntas incorrectas: ${preguntas_incorrectas} \nPreguntas no contestadas: ${nocontestadas}")

        builder.setCancelable(false)//tal vez sea mejor quitarlo

        builder?.setPositiveButton("Ver respuestas",DialogInterface.OnClickListener { dialog, which ->
            finish()

        })

        builder?.setNegativeButton("Volver a empezar", DialogInterface.OnClickListener { dialog, which ->

            startOver()//Borra todas las preguntas

        })


        builder?.show()

    }

    private fun checkAnswer() { //Guarda el número de pregunta clickada
        var id = group.checkedRadioButtonId //Regresa el id del radiobutton clickado
        // Log.d("btn_next",id.toString())
        var ans = -1


        for (i in 0..ids_answers.size - 1) {
            if (ids_answers[i] == id) {
                ans = i  //Guarda el número de pregunta seleccionada
            }


        }


        answer_is_correct[current_question] = (ans == correct_answer)
        answer[current_question] = ans

        if (answer_is_correct[current_question]==true){
            var respuesta = current_question
            preguntas_correctas.add(++respuesta)
        }

        if (answer_is_correct[current_question]==false){
            var respuesta = current_question
            preguntas_incorrectas.add(++respuesta)
        }



    }

    private fun showQuestion() {
        val q: String = all_questions[current_question]
        val parts = q.split(";")

        group.clearCheck()
        text_question.setText(parts[0])



        for (i in 0..ids_answers.size - 1) {
            val rb = findViewById<RadioButton>(ids_answers[i])
            var ans: String = parts[i + 1]
            if (ans.get(0) == '*') {
                correct_answer = i
                ans=ans.substring(1)
            }

            rb.setText(ans)

            if (answer[current_question]==i){
                rb.isChecked=true
            }

        }
        if (current_question==0){
            btn_previus.visibility=View.GONE
        }else{
            btn_previus.visibility=View.VISIBLE
        }
        if (current_question == all_questions.size-1){
            btn_next.setText("Finalizar")
        }else{
            btn_next.setText("Next")
        }

        //Para la imagen
        if (current_question==1){
            ivQuestion.visibility=View.VISIBLE
            ivQuestion.setImageResource(R.drawable.parabola)
        }else{
            ivQuestion.visibility=View.GONE
        }


    }

}
