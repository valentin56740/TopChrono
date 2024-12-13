package app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


enum class EtatTimer {
    ARRET,
    TRAVAIL,
    REPOS,
    TERMINE
}

class Timer{
    // Variables rentrées pas l'utilisateur
    var repetitions by mutableStateOf(0)
    var workMinutes by mutableStateOf(0)
    var workSeconds by mutableStateOf(0)
    var restMinutes by mutableStateOf(0)
    var restSeconds by mutableStateOf(0)

    // état du timer
    var etatCourant by mutableStateOf(EtatTimer.ARRET)
    var travailRestant by mutableStateOf(0)
    var reposRestant by mutableStateOf(0)
    var repetitionsRestant by mutableStateOf(0)
    var bgColor by mutableStateOf(Color.White)

    // Lancement du Timer
    fun startTimer() {
        // On vérifie si l'utilisateur à rentré des données correctes
        if (repetitions <= 0 || (workMinutes * 60 + workSeconds) <= 0) return

        // On attribue les valeurs de l'utilisateur aux valeurs du Timer
        repetitionsRestant = repetitions
        travailRestant = workMinutes * 60 + workSeconds
        reposRestant = restMinutes * 60 + restSeconds
        etatCourant = EtatTimer.TRAVAIL


        CoroutineScope(Dispatchers.Default).launch {
            // Cette première boucle correspond aux répétitions
            while (repetitionsRestant > 0) {
                // écoulement du temps de travail
                while (travailRestant > 0) {
                    bgColor = Color(0xFFFF6B6B)
                    delay(1000) //Attente d'une seconde
                    travailRestant-- // Décrémentation du temps de travail c'est ainsi que l'on créer un chronomètre
                }

                // même principe que pour le temps de travail ce sont deux chrono qui s'enchaînent
                etatCourant = EtatTimer.REPOS
                while (reposRestant > 0) {
                    bgColor = Color.Green
                    delay(1000)
                    reposRestant--
                }

                // Une répétition correspond à 1 travail + 1 repos donc on décrémente
                repetitionsRestant--

                // On reset les valeurs de temps
                travailRestant = workMinutes * 60 + workSeconds
                reposRestant = restMinutes * 60 + restSeconds
                etatCourant = EtatTimer.TRAVAIL
            }

            // Il n'y plus de répétitions donc l'état du Timer est termine
            etatCourant = EtatTimer.TERMINE
            bgColor = Color.White
        }
    }

    fun stopTimer() {
        etatCourant = EtatTimer.ARRET
        travailRestant = 0
        reposRestant = 0
        repetitionsRestant = 0
        bgColor = Color.White
    }

    // Utility function to format time
    fun formatTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    // Computed properties for UI display
    val displayTime: String
    get() = when (etatCourant) {
        EtatTimer.TRAVAIL -> formatTime(travailRestant)
        EtatTimer.REPOS -> formatTime(reposRestant)
            else -> "00:00"
    }

    val displayRep: String
        get () = when(etatCourant) {
            EtatTimer.TRAVAIL -> repetitionsRestant.toString()
            EtatTimer.REPOS -> repetitionsRestant.toString()
            else -> ""
        }

    val displayState: String
    get() = when (etatCourant) {
        EtatTimer.ARRET -> "Arrêt"
        EtatTimer.TRAVAIL -> "Travail"
        EtatTimer.REPOS -> "Repos"
        EtatTimer.TERMINE -> "Terminé"
    }
}