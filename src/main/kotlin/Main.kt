package org.example

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.concurrent.thread
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*


lateinit var client: Client




@Composable
@Preview
fun App() {
    var login by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var is_login by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var messages = remember { mutableStateListOf<String>() }

    /*if(msg!=null){
        messages.add(msg)
    }*/
    fun send_message (message:String)
    {
        if (is_login) {
            if (message != "") {
                text = login + ": " + message
                println(text)
                client.send(text)
            }
        } else {
            is_login = true
            login = message
            text = login + " вошел в чат."

            client = Client()
            client.start()
            client.send(text)
            thread{
                while(true) {
                    var t = client.receive()
                    if (t != null) {
                        messages.add(t)
                    }
                }
            }

        }
    }

    Column {

        //messages
        Box(modifier = Modifier.weight(9f)) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize()) {
                if (is_login) {
                    for(m in messages) {
                        Text(m)
                    }
                }
            }


        }


        //message and send message
        Row {
            TextField(modifier = Modifier.weight(8f).onKeyEvent { event ->
                if (event.key == Key.Enter) {

                    send_message(message)
                    message = ""
                    true
                } else {
                    false
                }
            }, value = message, onValueChange = { message = it }, singleLine = true)
            Button(
                onClick = {
                    send_message(message)
                    message = ""
                }) {
                if (!is_login) {
                    Text("Ввести логин")
                } else {
                    Text("Отправить")
                }
            }
        }
    }

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}