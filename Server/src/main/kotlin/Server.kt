package org.example
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server(port: Int = 8080) {
    var socket: ServerSocket = ServerSocket(port)
    var clientSocket: Socket? = null


    fun start(){
        clientSocket = socket.accept()
        thread {
            var is_client = true
            val connection = clientSocket?.let { Connection(it) }!!
            val connectedClient = ConnectedClient(clientSocket!!)
            println(connectedClient)
            println(connection)
            while (is_client) {
                try {
                    println("wait")
                    val text = connection.receive()
                    println("receive "+connection)
                    println(text)
                    if (text != null) {
                        connectedClient.sendAll(text.toString())
                    }
                    println("sended")
                }
                catch(e:Exception){
                    connectedClient.remove_client(connectedClient)
                    connection.finish()
                    println("disconnected")
                    is_client = false
                }
            }
        }
    }
}