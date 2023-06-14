package com.mann.clicktochatwhatsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mann.clicktochatwhatsapp.ui.theme.ClickToChatWhatsAppTheme
import com.mann.clicktochatwhatsapp.widgets.MannCountryCodePicker
import com.mann.clicktochatwhatsapp.widgets.*


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClickToChatWhatsAppTheme {
                var number by remember {
                    mutableStateOf("")
                }
                // A surface container using the 'background' color from the theme
                Scaffold() {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(it),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(50.dp))
                            Image(painter = painterResource(id = R.drawable.whatsapp), contentDescription = "",Modifier.size(200.dp))
                            Text(text = "Whatsapp Me", fontSize = 25.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(100.dp))
                            CountryCodePickerExample()
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(text = "DEVELOPED BY MANN AMBANI", fontSize = 12.sp, fontWeight = FontWeight.Light, modifier = Modifier.align(
                                    Alignment.BottomCenter
                                ))
                            }

                        }

                }
            }
        }
    }



@Composable
fun CountryCodePickerExample() {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val phoneNumber = rememberSaveable { mutableStateOf("") }
        val fullPhoneNumber = rememberSaveable { mutableStateOf("") }
        val onlyPhoneNumber = rememberSaveable { mutableStateOf("") }

        MannCountryCodePicker(
            text = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            bottomStyle =false, //  if true the text-field is below the country code selector at the top.
            shape = RoundedCornerShape(24.dp)
        )
        Button(onClick = {
            if (!isPhoneNumber()) {
//                fullPhoneNumber.value = getFullPhoneNumber()
//                onlyPhoneNumber.value = getOnlyPhoneNumber()

                val url =
                    "https://api.whatsapp.com/send/?phone=${getFullPhoneNumber().removePrefix("+")}&text&type=phone_number&app_absent=0"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)

            } else {
                fullPhoneNumber.value = "Error"
                onlyPhoneNumber.value = "Error"
            }
        }) {
            Text(text = "CLICK TO START CHAT")
        }

    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ClickToChatWhatsAppTheme {
        Greeting("Android")
    }
}

}