package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

// --- Data Class ---
data class Student(
    var name: String
)

// --- MainActivity ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Here, we use setContent instead of setContentView
        setContent {
            //Here, we wrap our content with the theme
            LAB_WEEK_09Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    // We use Modifier.fillMaxSize() to make the surface fill the whole screen
                    modifier = Modifier.fillMaxSize(),
                    // and set it as the color of the surface
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Call the Home composable without parameters
                    Home()
                }
            }
        }
    }
}

// --- Home Composable (State Management Logic) ---
@Composable
fun Home() { // Removed the extra { and }
    // State for the list of students
    val listData = remember { mutableStateListOf(
        Student("Tanu"),
        Student("Tina"),
        Student("Tono")
    )}
    // State for the text field input
    var inputFieldState = remember { mutableStateOf(Student("")) } // Renamed to avoid conflict

    // We call the HomeContent composable
    HomeContent(
        listData = listData,
        inputField = inputFieldState.value, // Pass the current value
        onInputValueChange = { input ->
            // Update the input field state, using copy for immutability
            inputFieldState.value = inputFieldState.value.copy(name = input)
        },
        onButtonClick = {
            if (inputFieldState.value.name.isNotBlank()) {
                // Add a COPY of the current input field state to the list
                listData.add(inputFieldState.value.copy())
                // Reset the input field state
                inputFieldState.value = Student("")
            }
        }
    )
}

// --- HomeContent Composable (UI Layout) ---
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit
) {
    LazyColumn {
        //Here, we use item to display an item inside the LazyColumn
        item {
            Column(
                // Fixed the Modifier chain syntax
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // IMPORTANT: Ensure R.string.enter_item is defined in strings.xml
                Text(text = stringResource(
                    id = R.string.enter_item)
                )
                //Here, we use TextField to display a text input field
                TextField(
                    //Set the value of the input field
                    value = inputField.name,
                    //Set the keyboard type of the input field
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = {
                        // Pass the new text (it) up via the lambda function
                        onInputValueChange(it)
                    }
                )

                Button(onClick = {
                    //Here, we call the onButtonClick lambda function
                    onButtonClick()
                }) {
                    //Set the text of the button
                    // IMPORTANT: Ensure R.string.button_click is defined in strings.xml
                    Text(text = stringResource(
                        id = R.string.button_click)
                    )
                }
            }
        }

        //This is the RecyclerView replacement
        //We pass the listData as a parameter
        items(listData) { item ->
            Column(
                // Fixed the Modifier chain syntax
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item.name)
            }
        }
    }
}

// --- Preview Composable ---
// The Home composable no longer takes parameters, so this preview needs adjustment.
@Preview(showBackground = true)
@Composable
fun PreviewHomeContent() {
    LAB_WEEK_09Theme {
        // Wrap the state object creation in remember
        val mockList = remember {
            mutableStateListOf(Student("Tanu"), Student("Tina"), Student("Tono"))
        }

        HomeContent(
            listData = mockList,
            inputField = Student(name = ""),
            onInputValueChange = {},
            onButtonClick = {}
        )
    }
}