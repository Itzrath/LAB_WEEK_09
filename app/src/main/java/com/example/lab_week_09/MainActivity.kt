package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// --- NAVIGATION IMPORTS ---
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
// --- THEME/UI IMPORTS (Custom Elements) ---
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton

import com.google.gson.Gson
import java.net.URLEncoder


// --- Data Class ---
data class Student(
    var name: String
)

// --- MainActivity ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(navController = navController)
                }
            }
        }
    }
}

// --- App Composable (Navigation Setup) ---
@Composable
fun App(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            // Pass the navigation function down to Home
            Home(onNavigateToResult = { listData ->
                // MODIFICATION 1: Use the standard Kotlin List toString() representation
                val listString = listData.toList().toString()

                // URL encode the string to safely pass it as an argument
                val encodedList = URLEncoder.encode(listString, "UTF-8")

                // Navigate with the encoded string
                navController.navigate("resultContent/?listData=$encodedList")
            })
        }
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            ResultContent(
                backStackEntry.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

// --- Home Composable (State Management Logic) ---
@Composable
fun Home(onNavigateToResult: (SnapshotStateList<Student>) -> Unit) {
    // State for the list of students
    val listData = remember { mutableStateListOf(
        Student("Tanu"),
        Student("Tina"),
        Student("Tono")
    )}
    // State for the text field input
    var inputFieldState by remember { mutableStateOf(Student("")) }

    // We call the HomeContent composable
    HomeContent(
        listData = listData,
        inputField = inputFieldState, // Pass the value
        onInputValueChange = { input ->
            // Update the state using .copy and the new name
            inputFieldState = inputFieldState.copy(name = input)
        },
        onButtonClick = {
            if (inputFieldState.name.isNotBlank()) {
                // Add a copy of the current state to the list
                listData.add(inputFieldState.copy())
                // Reset the input field state
                inputFieldState = Student("")
            }
        },
        onNavigateToResult = {
            // Call the lambda passed from App, giving it the list data
            onNavigateToResult(listData)
        }
    )
}

// --- HomeContent Composable (UI Layout) ---
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onNavigateToResult: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Using custom UI component
                OnBackgroundTitleText(text = stringResource(id = R.string.enter_item))

                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    onValueChange = onInputValueChange,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Using custom UI component
                    PrimaryTextButton(text = stringResource(id = R.string.button_click)) {
                        onButtonClick()
                    }
                    // Using custom UI component
                    PrimaryTextButton(text = stringResource(id = R.string.button_navigate)) {
                        onNavigateToResult()
                    }
                }
            }
        }
        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Using custom UI component
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

// --- Result Content Composable ---
@Composable
fun ResultContent(listData: String) {
    // Decode the URL encoded string
    val decodedListString = java.net.URLDecoder.decode(listData, "UTF-8")

    // MODIFICATION 2: Remove the parsing/splitting logic and display the raw string.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Display the entire decoded string in a single custom Text component
        OnBackgroundItemText(text = decodedListString)
    }
}

// --- Preview Composable ---
@Preview(showBackground = true)
@Composable
fun PreviewHomeContent() {
    LAB_WEEK_09Theme {
        val mockList = remember {
            mutableStateListOf(Student("Tanu"), Student("Tina"), Student("Tono"))
        }

        HomeContent(
            listData = mockList,
            inputField = Student(name = "New Student"),
            onInputValueChange = {},
            onButtonClick = {},
            onNavigateToResult = {}
        )
    }
}