/*
* Jetpack Compose Basic
*
* Link CodeLab : https://developer.android.com/codelabs/jetpack-compose-basics?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fcompose%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-basics#10
* */
package com.muhammadmayudin.jetpackcomposebasics

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadmayudin.jetpackcomposebasics.ui.theme.JetpackComposeBasicsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeBasicsTheme {
                // A surface container using the 'background' color from the theme
                MyApp()
            }
        }
    }
}

/*Reusing composables*/
@Composable
fun MyApp(modifier: Modifier = Modifier, names: List<String> = List(100) { "$it" }) {
    /*Error: (JetPack Compose) Type 'TypeVariable(T)' has no method 'getValue(Nothing?, KProperty<*>)*/
    /*Solved: ou need to import :
    import androidx.compose.runtime.getValue
    Or import everything using :
    import androidx.compose.runtime.*
    */

    /*Our app has a problem: if you run the app on a device, click on the buttons and then you rotate, the onboarding screen is shown again. The remember function works only as long as the composable is kept in the Composition. When you rotate, the whole activity is restarted so all state is lost. This also happens with any configuration change and on process death.*/
    //var shouldShowOnboarding by remember { mutableStateOf(true) }
    /*
    Instead of using remember you can use rememberSaveable. This will save each state surviving configuration changes (such as rotations) and process death.
    Now replace the use of remember in shouldShowOnboarding with rememberSaveable:
    */
    /*Persisting state*/
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {

        if (shouldShowOnboarding) {
            OnBoardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {

            CreatingLazyColumn(names = names)

            /*Column(modifier = modifier.padding(vertical = 4.dp)) {
                for(name in names){
                    CreatingColumnsAndRowsExpanding(name)
                }
            }*/

        }
    }

}


/*Creating columns and rows*/
@Composable
fun CreatingColumnsAndRows(name: String) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Hello, ")
                Text(text = name)
            }
            ElevatedButton(
                onClick = { /* TODO */ }
            ) {
                Text("Show more")
            }
        }
    }
}

/*State in Compose*/
/*Expanding the item*/
/*Creating columns and rows*/
@Composable
fun CreatingColumnsAndRowsExpanding(
    name: String
) {
    var expanded by remember { mutableStateOf(false) }

    //normal list
    /*val extraPadding = if (expanded.value) 48.dp else 0.dp*/
    //Animate List
    val extraPadding by animateDpAsState(
        if (expanded)
            48.dp
        else
            0.dp, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(text = "Hello, ")
                Text(text = name)
            }
            ElevatedButton(
                onClick = { expanded = !expanded }
            ) {
                Text(if (expanded) "Show less" else "Show more")
            }
        }
    }
}

/*State hoisting*/
@Composable
fun OnBoardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}


/*
* Creating a performant lazy list
* Now let's make the names list more realistic. So far you have displayed two greetings in a Column. But, can it handle thousands of them?
*/
@Composable
fun CreatingLazyColumn(
    modifier: Modifier = Modifier,
    names: List<String> = listOf()
){
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            CardContent(name = name)
        }
    }
}

/*
* Finishing touches!
* Replace button with an icon
* implementation "androidx.compose.material:material-icons-extended:$compose_version"
*
* Use string resources
* contentDescription = if (expanded) "Show less" else "Show more"
* <string name="show_less">Show less</string>
* <string name="show_more">Show more</string>
*
* */
@Composable
private fun CardContent(name: String) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = "Hello, ")
            Text(
                text = name, style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnBoardingPreview() {
    JetpackComposeBasicsTheme {
        OnBoardingScreen(onContinueClicked = {})
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun DefaultPreview() {
    JetpackComposeBasicsTheme {
        Greeting("Android")
    }
}

/*Styling and theming your app*/
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Composable
fun MyAppPreview() {
    JetpackComposeBasicsTheme {
        MyApp(Modifier.fillMaxSize())
    }
}