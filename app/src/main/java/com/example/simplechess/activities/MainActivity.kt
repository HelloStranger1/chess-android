package com.example.simplechess.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.simplechess.chessComponents.ChessDelegate
import com.example.simplechess.chessComponents.ChessGame
import com.example.simplechess.chessComponents.ChessPiece
import com.example.simplechess.ChessView
import com.example.simplechess.R
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
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
    private val isEmulator = Build.FINGERPRINT.contains("generic")
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
        }
        btnListen?.setOnClickListener{
            Log.e("Listen", "Listening1")
            val port = socketGuestPort
            Executors.newSingleThreadExecutor().execute{

                val serverSocket = ServerSocket(port)
                Log.e("Listen", "Listening1 on port $port...")
                val socket = serverSocket.accept()
                Log.e("Listen", "Listening on port $port...")
                receiveMove(socket)

            }

        }
        btnConnect?.setOnClickListener{
            Executors.newSingleThreadExecutor().execute{
                Log.e("ConnectTag", "Connect")
                val socket = Socket(socketHost, socketPort)
                receiveMove(socket)
                Log.e("ConnectTag", "Connect1")
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