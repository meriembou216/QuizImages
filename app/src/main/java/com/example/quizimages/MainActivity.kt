package com.example.quizimages

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Une question = texte + image + 3 choix + indice de la bonne réponse + description (accessibilité)
data class Question(
    val texte: String,
    val imageRes: Int,
    val choix: Array<String>,
    val bonneReponse: Int,      // 0, 1 ou 2
    val description: String
)

class MainActivity : AppCompatActivity() {

    // Les 5 questions statiques (la bonne réponse change de position)
    private val questions = arrayOf(
        Question("Combien de côtés possède cette forme ?", R.drawable.triangle,
            arrayOf("4", "3", "5"), 1, "Un triangle rouge"),
        Question("Cette forme a 4 côtés égaux. Comment s'appelle-t-elle ?", R.drawable.carre,
            arrayOf("Rectangle", "Cercle", "Carré"), 2, "Un carré bleu"),
        Question("Cette forme a-t-elle des angles ?", R.drawable.cercle,
            arrayOf("Non, aucun", "Oui, 4", "Oui, 3"), 0, "Un cercle vert"),
        Question("Combien de branches a cette étoile ?", R.drawable.etoile,
            arrayOf("6", "5", "4"), 1, "Une étoile orange"),
        Question("Comment s'appelle cette forme allongée à 4 angles droits ?", R.drawable.rectangle,
            arrayOf("Rectangle", "Triangle", "Cercle"), 0, "Un rectangle violet")
    )

    // Variables d'état du jeu
    private var indexCourant = 0        // question affichée
    private var score = 0               // nombre de bonnes réponses
    private var nbReponses = 0          // nombre de réponses données (progression)
    private var aRepondu = false        // a-t-on déjà répondu à la question courante ?
    private var partieTerminee = false

    // Widgets
    private lateinit var txtScore: TextView
    private lateinit var progressQuiz: ProgressBar
    private lateinit var txtNumero: TextView
    private lateinit var imgQuestion: ImageView
    private lateinit var txtQuestion: TextView
    private lateinit var boutons: List<Button>
    private lateinit var txtFeedback: TextView
    private lateinit var btnSuivant: Button
    private lateinit var txtResultat: TextView
    private lateinit var btnRejouer: Button
    private lateinit var zoneQuestion: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Liaison avec le XML
        txtScore = findViewById(R.id.txt_score)
        progressQuiz = findViewById(R.id.progress_quiz)
        txtNumero = findViewById(R.id.txt_numero)
        imgQuestion = findViewById(R.id.img_question)
        txtQuestion = findViewById(R.id.txt_question)
        txtFeedback = findViewById(R.id.txt_feedback)
        btnSuivant = findViewById(R.id.btn_suivant)
        txtResultat = findViewById(R.id.txt_resultat)
        btnRejouer = findViewById(R.id.btn_rejouer)
        boutons = listOf(
            findViewById(R.id.btn_choix1),
            findViewById(R.id.btn_choix2),
            findViewById(R.id.btn_choix3)
        )

        // Tout ce qui disparaît à la fin du jeu
        zoneQuestion = listOf(txtNumero, imgQuestion, txtQuestion,
            boutons[0], boutons[1], boutons[2], txtFeedback)

        // Un clic sur le bouton i envoie son indice i à verifierReponse
        boutons.forEachIndexed { i, bouton ->
            bouton.setOnClickListener { verifierReponse(i) }
        }
        btnSuivant.setOnClickListener { passerSuivante() }
        btnRejouer.setOnClickListener { rejouer() }

        afficherQuestion()
    }

    // Affiche la question courante
    private fun afficherQuestion() {
        val q = questions[indexCourant]
        txtNumero.text = "Question ${indexCourant + 1} sur ${questions.size}"
        imgQuestion.setImageResource(q.imageRes)
        imgQuestion.contentDescription = q.description
        txtQuestion.text = q.texte
        boutons.forEachIndexed { i, bouton ->
            bouton.text = q.choix[i]
            bouton.isEnabled = true
        }
        txtFeedback.text = ""
        btnSuivant.visibility = View.GONE
        aRepondu = false
        majBandeau()
    }

    // Vérifie le choix de l'utilisateur
    private fun verifierReponse(choixIndex: Int) {
        if (aRepondu || partieTerminee) return   // empêche un 2e point
        aRepondu = true
        boutons.forEach { it.isEnabled = false }  // désactive les 3 boutons

        val q = questions[indexCourant]
        if (choixIndex == q.bonneReponse) {
            score++
            txtFeedback.text = "✔ Bonne réponse !"
            txtFeedback.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            txtFeedback.text = "✘ Mauvaise réponse. La bonne réponse était : ${q.choix[q.bonneReponse]}"
            txtFeedback.setTextColor(Color.parseColor("#C62828"))
        }
        nbReponses++                              // la progression avance dans les 2 cas
        majBandeau()

        btnSuivant.text = if (indexCourant == questions.size - 1) "Voir le résultat" else "Question suivante"
        btnSuivant.visibility = View.VISIBLE
    }

    // Met à jour le score et la barre de progression
    private fun majBandeau() {
        txtScore.text = "Score : $score / ${questions.size}"
        progressQuiz.progress = nbReponses
    }

    private fun passerSuivante() {
        if (!aRepondu) return                      // pas de passage sans réponse
        if (indexCourant < questions.size - 1) {
            indexCourant++
            afficherQuestion()
        } else {
            afficherResultat()                     // dernière question terminée
        }
    }

    private fun afficherResultat() {
        partieTerminee = true
        zoneQuestion.forEach { it.visibility = View.GONE }
        btnSuivant.visibility = View.GONE
        txtResultat.text = "Quiz terminé !\nVotre score final : $score / ${questions.size}"
        txtResultat.visibility = View.VISIBLE
        btnRejouer.visibility = View.VISIBLE
    }

    private fun rejouer() {
        indexCourant = 0
        score = 0
        nbReponses = 0
        partieTerminee = false
        txtResultat.visibility = View.GONE
        btnRejouer.visibility = View.GONE
        zoneQuestion.forEach { it.visibility = View.VISIBLE }
        afficherQuestion()
    }
}