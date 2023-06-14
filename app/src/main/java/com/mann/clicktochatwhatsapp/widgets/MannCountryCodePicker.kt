package com.mann.clicktochatwhatsapp.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import com.togitech.ccp.component.CountryDialog
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.*
import com.togitech.ccp.transformation.PhoneNumberTransformation

private var fullNumberState: String by mutableStateOf("")
var checkNumberState: Boolean by mutableStateOf(false)
private var phoneNumberState: String by mutableStateOf("")
private var countryCodeState: String by mutableStateOf("")
private var countryCode: String by mutableStateOf("")


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MannCountryCodePicker(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    shape: Shape = RoundedCornerShape(24.dp),
    color: Color = MaterialTheme.colorScheme.background,
    showCountryCode: Boolean = true,
    showCountryFlag: Boolean = true,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.onSecondary,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    bottomStyle: Boolean = false,

    ) {
    val context = LocalContext.current
    var textFieldValue by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalTextInputService.current
    var phoneCode by rememberSaveable {
        mutableStateOf(
            getDefaultPhoneCode(
                context
            )
        )
    }
    var defaultLang by rememberSaveable {
        mutableStateOf(
            getDefaultLangCode(context)
        )
    }

    fullNumberState = phoneCode + textFieldValue
    phoneNumberState = textFieldValue
    countryCodeState = defaultLang
    countryCode = phoneCode.substring(1)

    val primaryColor = MaterialTheme.colorScheme.primary
    var borderColor by remember { mutableStateOf(primaryColor) }

    Surface(color = color) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            if (bottomStyle) {
                MannCodeDialog(
                    pickedCountry = {
                        phoneCode = it.countryPhoneCode
                        defaultLang = it.countryCode
                    },
                    defaultSelectedCountry = getLibCountries.single { it.countryCode == defaultLang },
                    showCountryCode = showCountryCode,
                    showFlag = showCountryFlag,
                    showCountryName = true
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    shape = shape,
                    value = textFieldValue,
                    textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
                    onValueChange = {
                        textFieldValue = it
                        if (text != it) {
                            onValueChange(it)
                        }
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor =
                        if (getErrorStatus()) MaterialTheme.colorScheme.error else focusedBorderColor,
                        unfocusedBorderColor = if (getErrorStatus()) MaterialTheme.colorScheme.error else unfocusedBorderColor,
                        cursorColor = cursorColor,
                        focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    visualTransformation = PhoneNumberTransformation(getLibCountries.single { it.countryCode == defaultLang }.countryCode.uppercase()),
                    label = {
                        Text(
                            text = "Phone Number", fontSize = 14.sp
                        )
                    },//stringResource(id = getNumberHint(getLibCountries.single { it.countryCode == defaultLang }.countryCode.lowercase())) //, color = if (getErrorStatus()) MaterialTheme.colorScheme.error else focusedBorderColor)
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        autoCorrect = true,
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hideSoftwareKeyboard()
                    }),
                    leadingIcon = {
                        if (!bottomStyle) Row {
                            Column {
                                MannCodeDialog(
                                    pickedCountry = {
                                        phoneCode = it.countryPhoneCode
                                        defaultLang = it.countryCode
                                    },
                                    defaultSelectedCountry = getLibCountries.single { it.countryCode == defaultLang },
                                    showCountryCode = showCountryCode,
                                    showFlag = showCountryFlag,
                                    showCountryName = false
                                )
//                                    TogiCodeDialog(
//                                        pickedCountry = {
//                                            phoneCode = it.countryPhoneCode
//                                            defaultLang = it.countryCode
//                                        },
//                                        defaultSelectedCountry = getLibCountries.single { it.countryCode == defaultLang },
//                                        showCountryCode = showCountryCode,
//                                        showFlag = showCountryFlag,
//                                    )
                            }
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            textFieldValue = ""
                            onValueChange("")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear",
                                tint = if (getErrorStatus()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    supportingText = {
                        if (getErrorStatus()) Text(
                            text =  "Invalid Number",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 0.8.dp)
                        )
                    })
            }

        }
    }
}

@Composable
fun MannCodeDialog(
    modifier: Modifier = Modifier,
    padding: Dp = 15.dp,
    defaultSelectedCountry: CountryData = getLibCountries.first(),
    showCountryCode: Boolean = true,
    pickedCountry: (CountryData) -> Unit,
    showFlag: Boolean = true,
    showCountryName: Boolean = false,

    ) {
    val context = LocalContext.current

    val countryList: List<CountryData> = getLibCountries
    var isPickCountry by remember {
        mutableStateOf(defaultSelectedCountry)
    }
    var isOpenDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier
        .padding(padding)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
        ) {
            isOpenDialog = true
        }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showFlag) {
                Image(
                    modifier = modifier.width(34.dp), painter = painterResource(
                        id = getFlags(
                            isPickCountry.countryCode
                        )
                    ), contentDescription = null
                )
            }
            if (showCountryCode) {
                Text(
                    text = isPickCountry.countryPhoneCode,
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (showCountryName) {
                Text(
                    text = stringResource(id = getCountryName(isPickCountry.countryCode.lowercase())),
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 14.sp,
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown, contentDescription = null
                )
            }
        }


        if (isOpenDialog) {
            CountryDialog(countryList = countryList,
                onDismissRequest = { isOpenDialog = false },
                context = context,
                dialogStatus = isOpenDialog,
                onSelected = { countryItem ->
                    pickedCountry(countryItem)
                    isPickCountry = countryItem
                    isOpenDialog = false
                })
        }
    }
}


fun getFullPhoneNumber(): String {
    return fullNumberState
}

fun getOnlyPhoneNumber(): String {
    return phoneNumberState
}

fun getErrorStatus(): Boolean {
    return checkNumberState
}

fun getCountrycode(): String {
    return countryCode
}

fun isPhoneNumber(): Boolean {
    val check = com.togitech.ccp.data.utils.checkPhoneNumber(
        phone = phoneNumberState, fullPhoneNumber = fullNumberState, countryCode = countryCodeState
    )
    checkNumberState = check
    return check
}