package app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.compose.runtime.mutableStateOf



@Composable
fun Chronometre() {

    var viewModel = remember { Timer() }

    Box(
        modifier = Modifier.fillMaxSize().background(viewModel.bgColor),
        contentAlignment = Alignment.Center
    ){
        ElevatedCard(
            modifier = Modifier.height(854.dp)
                .width(480.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = viewModel.displayTime,
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 48.sp
                )

                Text(
                    text = viewModel.displayRep,
                    style = MaterialTheme.typography.bodyLarge
                )


                Text(
                    text = viewModel.displayState,
                    style = MaterialTheme.typography.bodyLarge
                )

                RepeatCounter(
                    value = viewModel.repetitions,
                    onValueChange = { viewModel.repetitions = it },
                    )

                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                TimerInput(
                    minutes = viewModel.workMinutes,
                    seconds = viewModel.workSeconds,
                    onMinutesChange = { viewModel.workMinutes = it },
                    onSecondsChange = { viewModel.workSeconds = it },

                )

                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                // Rest Timer Section

                TimerInput(
                    minutes = viewModel.restMinutes,
                    seconds = viewModel.restSeconds,
                    onMinutesChange = { viewModel.restMinutes = it },
                    onSecondsChange = { viewModel.restSeconds = it }
                )

                // Control Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Button(
                        onClick = { viewModel.startTimer() },
                        enabled = viewModel.etatCourant == EtatTimer.ARRET
                    ) {
                        Icon(Icons.Rounded.PlayArrow, contentDescription = "Start Timer")
                        Spacer(Modifier.width(8.dp))
                        Text("Démarrer")
                    }

                    Button(
                        onClick = { viewModel.stopTimer() },
                        enabled = viewModel.etatCourant != EtatTimer.ARRET
                    ) {
                        Icon(Icons.Rounded.Stop, contentDescription = "Stop Timer")
                        Spacer(Modifier.width(8.dp))
                        Text("Arrêter")
                    }
                }
            }
        }
    }
}

@Composable
fun TimerScreen() {
    val viewModel = remember { Timer() } // Replace with actual timer instance

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = viewModel.displayTime,
            style = MaterialTheme.typography.displayLarge,
            fontSize = 64.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RepeatCounter(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(onClick = { if(value > 0) onValueChange(value - 1) }) {
            Icon(imageVector = Icons.Rounded.Remove, contentDescription = "Decrease repetitions")
        }

        OutlinedTextField(
            value = value.toString(),
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() }
                onValueChange(filtered.toIntOrNull() ?: 0)
            },
            modifier = Modifier.width(80.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        IconButton(onClick = { onValueChange(value + 1) }) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = "Increase repetitions")
        }
    }
}

@Composable
fun TimerInput(
    minutes: Int,
    seconds: Int,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = minutes.toString(),
            onValueChange = {
                val filtered = it.filter {
                    char -> char.isDigit()
                }
                if(filtered.length <= 2){
                    if(filtered == "") onMinutesChange(0)
                    else onMinutesChange(filtered.toInt())
                }
            },
            modifier = Modifier.width(70.dp)
        )
        Text(":", fontSize = 24.sp)
        TextField(
            value = seconds.toString(),
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() }
                if(filtered.length <= 2){
                    if(filtered == "") onSecondsChange(0)
                    else onSecondsChange(filtered.toInt())
                }
            },
            modifier = Modifier.width(70.dp)
        )
    }
}