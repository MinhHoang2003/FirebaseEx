package com.example.firebaseex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var chatAdapter: ChatAdapter
    private var currentKey: String? = null
    lateinit var localBroadcastManager: LocalBroadcastManager

    companion object {
        const val ACTION = "com.example.firebaseex.Broadcast"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("chats")
        recyclerView.layoutManager = LinearLayoutManager(this)
        displayChat()
        buttonSend.setOnClickListener(this)
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        buttonPicture.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
//        chatAdapter.startListening()
    }


    private fun postChat(content: Content) {
        FirebaseDatabase.getInstance()
            .getReference("chats")
            .child("chat01")
            .child("contents")
            .push()
            .setValue(content)
            .addOnCompleteListener {
                Log.d("firebase", "Send content successful")
            }.addOnCanceledListener {
                Log.d("firebasae", "")
            }
    }

    private fun displayChat() {
        val query = databaseReference
            .child("chat01")
            .child("contents")
            .limitToLast(50)


        val firebaseRecyclerOptions: FirebaseRecyclerOptions<Content> =
            FirebaseRecyclerOptions.Builder<Content>().setQuery(query, Content::class.java).build()
        chatAdapter = ChatAdapter(firebaseRecyclerOptions, this)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("items", chatAdapter.itemCount.toString())
                val intent = Intent()
                intent.action = ACTION
                localBroadcastManager.sendBroadcast(intent)
                recyclerView.smoothScrollToPosition(chatAdapter.snapshots.size - 1)
            }
        })
        chatAdapter.startListening()
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                recyclerView.smoothScrollToPosition(chatAdapter.snapshots.size - 1)
            }
        })
        recyclerView.adapter = chatAdapter
    }

    override fun onStop() {
        super.onStop()
        chatAdapter.stopListening()
    }

    override fun onClick(view: View?) {
        when (view) {
            buttonSend -> {
                val content = Content(
                    edtChat.text.toString() + String(Character.toChars(0x1F60A)),
                    "text",
                    "13:53 13/01/20120",
                    "test02",
                    arrayListOf("test01")
                )
                postChat(content)
            }
        }
    }


}
