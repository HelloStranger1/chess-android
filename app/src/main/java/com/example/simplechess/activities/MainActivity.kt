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
import com.example.simplechess.chessComponents.Square
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

    private var isPlayerWhite : Boolean = true

    private var btnReset : Button? = null
    private var btnListen : Button? = null
    private var btnConnect : Button? = null
    private lateinit var chessView: ChessView
    private var printWriter: PrintWriter? = null
    private var serverSocket : ServerSocket? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chessView = findViewById(R.id.chess_view)

        btnReset = findViewById(R.id.btn_reset)
        btnListen = findViewById(R.id.btn_listen)
        btnConnect = findViewById(R.id.btn_connect)

        Log.e("TAG", ChessGame.toString())
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
                        runOnUiThread {
                            Toast.makeText(this, "You will play white", Toast.LENGTH_SHORT).show()
                        }
                        receiveMove(socket)
                    }
                } catch ( e : SocketException){
                    Log.e("TAG", "Socket closed")
                }
            }
        }
        btnConnect?.setOnClickListener{
            isPlayerWhite = false
            Executors.newSingleThreadExecutor().execute{
                runOnUiThread {
                    Toast.makeText(this, "Socket Client Connecting...", Toast.LENGTH_LONG).show()
                }
                Log.e("TAG", "btn connect")
                try{
                    Log.e("TAG", "HERE1")
                    val socket = Socket(socketHost, socketPort)
                    Log.e("TAG", "HERE2")
                    receiveMove(socket)
                    isPlayerWhite = false
                    runOnUiThread {
                        Toast.makeText(this, "You will play Black", Toast.LENGTH_SHORT).show()
                    }
                }catch (e : ConnectException){
                    Log.e("TAG", "Connection Failed")
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
        Log.e("TAG", "isPlaying white2: $isPlayerWhite")
        val scanner = Scanner(socket.getInputStream())
        printWriter = PrintWriter(socket.getOutputStream(), true)
        while(scanner.hasNextLine()){
            val move = scanner.nextLine().split(",").map { it.toInt()}
            runOnUiThread {
                val isValid = ChessGame.movePiece(Square(move[0], move[1]), Square(move[2], move[3]), !isPlayerWhite)
                chessView.invalidate()
                if(isValid){
                    ChessGame.isWhitesTurn = !ChessGame.isWhitesTurn
                }


                Log.e("TAG", "receive: ${ChessGame.isWhitesTurn}")
            }
        }
    }
    override fun pieceAt(square: Square): ChessPiece? {
        return ChessGame.pieceAt(square)
    }

    override fun movePiece(from : Square, to : Square) {
        Log.e("TAG", "isPlaying white1: $isPlayerWhite")
        if(isPlayerWhite && !ChessGame.isWhitesTurn) return
        if(!isPlayerWhite && ChessGame.isWhitesTurn) return
        val isValid = ChessGame.movePiece(from, to, isPlayerWhite)

        chessView.invalidate()
        val moveStr = "${from.col},${from.row},${to.col},${to.row}"
        Executors.newSingleThreadExecutor().execute{
            printWriter?.println(moveStr)
        }
        if(isValid){
            ChessGame.isWhitesTurn = !ChessGame.isWhitesTurn
        }

        Log.e("TAG", "write: ${ChessGame.isWhitesTurn}")

    }
}