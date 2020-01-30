package com.example.firebaseex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.items_chat_receive_first.view.*
import kotlinx.android.synthetic.main.items_chat_receive_last.view.*

class ChatAdapter(query: FirebaseRecyclerOptions<Content>, private val context: Context) :
    FirebaseRecyclerAdapter<Content, ChatAdapter.BaseViewHolder>(query) {

    companion object {
        const val CHAT_RECEIVE: Int = 1
        const val CHAT_SEND: Int = 2
        const val CHAT_RECEIVE_LAST = 3
    }

    var onClick: ((Content, String) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        val content = snapshots[position]

        return when {
            content.from == Util.user -> CHAT_SEND
            position == snapshots.size - 1 || content.from != snapshots[position + 1].from -> return CHAT_RECEIVE_LAST
            else -> return CHAT_RECEIVE
        }
    }

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(data: Content)
    }

    inner class ChatSendViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(data: Content) {
//            itemView.textSend.text = data.content
        }
    }

    inner class ChatReceiveViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(data: Content) {
            itemView.textReceive.text = data.content
        }
    }

    inner class ChatReciveLastViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(data: Content) {
            itemView.textDate.text = data.date
            itemView.textContent.text = data.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            CHAT_SEND -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.chat_send_first, parent, false)
                ChatSendViewHolder(view)
            }
            CHAT_RECEIVE -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.items_chat_receive_first, parent, false)
                ChatReceiveViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.items_chat_receive_last, parent, false)
                ChatReciveLastViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, model: Content) {
        holder.bind(model)
    }
}