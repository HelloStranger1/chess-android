package com.example.simplechess.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.simplechess.chessComponents.ChessDelegate
import com.example.simplechess.chessComponents.ChessGame
import com.example.simplechess.chessComponents.ChessPiece
import com.example.simplechess.ChessView
import com.example.simplechess.R
import java.io.PrintWriter
import java.net.ConnectException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.Scanner
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), ChessDelegate {

    private val socketPort: Int = 50000
    private val socketGuestPort: Int = 50001 // used for socket server on emulator
    private val socketHost = "10.0.2.2"


    private var btnReset : Button? = null
    private var btnListen : Button? = null
    private var btnConnect : Button? = null
    private lateinit var chessView: ChessView
    private var printWriter: PrintWriter? = null
    private var serverSocket : ServerSocket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("Listen", "Listening ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chessView = findViewById(R.id.chess_view)

        btnReset = findViewById(R.id.btn_reset)
        btnListen = findViewById(R.id.btn_listen)
        btnConnect = findViewById(R.id.btn_connect)

        btnReset?.setOnClickListener{
            ChessGame.reset()
            chessView.invalidate()
            btnListen?.isEnabled = true
            serverSocket?.close()
        }
        btnListen?.setOnClickListener{
            btnListen?.isEnabled = false
            val port = socketGuestPort
            Executors.newSingleThreadExecutor().execute{
                runOnUiThread {
                    Toast.makeText(this, "Socket listening on port $port", Toast.LENGTH_LONG).show()
                }
                try{
                    serverSocket = ServerSocket(port)
                    val socket = serverSocket?.accept()
                    if (socket != null) {
                        receiveMove(socket)
                    }
                } catch ( e : SocketException){
                    Log.e("Socket", "Socket closed")
                }

            }

        }
        btnConnect?.setOnClickListener{
            Executors.newSingleThreadExecutor().execute{
                runOnUiThread {
                    Toast.makeText(this, "Socket Client Connecting...", Toast.LENGTH_LONG).show()
                }

                try{
                    val socket = Socket(socketHost, socketPort)
                    receiveMove(socket)
                }catch (e : ConnectException){
                    runOnUiThread {
                        Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show()
                    }

                }

            }

        }

        val chessView = findViewById<ChessView>(R.id.chess_view)
        chessView.chessDelegate = this
    }

    private fun receiveMove(socket: Socket){
        val scanner = Scanner(socket.getInputStream())
        printWriter = PrintWriter(socket.getOutputStream(), true)
        while(scanner.hasNextLine()){
            val move = scanner.nextLine().split(",").map { it.toInt()}
            runOnUiThread {
                ChessGame.movePiece(move[0], move[1], move[2], move[3])
                chessView.invalidate()
            }
        }
    }
    override fun pieceAt(col: Int, row: Int): ChessPiece? {
        return ChessGame.pieceAt(col, row)
    }

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        ChessGame.movePiece(fromCol, fromRow, toCol, toRow)
        chessView.invalidate()
        val moveStr = "$fromCol,$fromRow,$toCol,$toRow"
        Executors.newSingleThreadExecutor().execute{
            printWriter?.println(moveStr)
        }

    }
}