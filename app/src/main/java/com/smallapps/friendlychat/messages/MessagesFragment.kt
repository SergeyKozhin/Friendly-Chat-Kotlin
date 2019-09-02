package com.smallapps.friendlychat.messages

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.smallapps.friendlychat.database.FriendlyMessageDataBase
import com.smallapps.friendlychat.databinding.FragmentMessagesBinding

// Main chat fragment
class MessagesFragment : Fragment() {

    companion object {
        const val RC_PHOTO_PICKER = 2
    }

    // ViewModel lazy initialization
    private val viewModel: MessagesViewModel by lazy {
        ViewModelProvider(this, MessagesViewModelFactory(requireNotNull(this.activity).application))
            .get(MessagesViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflation and data binding setting
        val binding =
            FragmentMessagesBinding.inflate(inflater, container, false)

        //Binding ViewModel to view
        binding.viewModel = viewModel

        // Setting lifecycle owner for observers to work
        binding.lifecycleOwner = this

        // Setting RecyclerView Adapter for list of messages
        val adapter = MessageAdapter()
        binding.messageListView.adapter = adapter

        // TextChangeListener for input message field to enable or disable send button
        binding.messageEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    viewModel.enableSendButton()
                } else {
                    viewModel.disableSendButton()
                }
            }
        })

        // Observer for handling message sending
        viewModel.sendingMessage.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.sendMessage(
                    FriendlyMessageDataBase(
                        binding.messageEditText.text.toString(),
                        viewModel.username,
                        null))
                binding.messageEditText.setText("")
                viewModel.onSendMessageCompleted()
            }
        })

        // Observer for handling image picking
        viewModel.pickingImage.observe(viewLifecycleOwner, Observer {
            if (it) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                    .setType("image/jpeg")
                    .putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    RC_PHOTO_PICKER)
                viewModel.onPickImageCompleted()
            }
        })

        // Observer for updating list
        viewModel.friendlyMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                Handler().postDelayed({
                        binding.messageListView.smoothScrollToPosition(0)
                }, 200)
            }
        })


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initializeChat()
    }

    override fun onPause() {
        super.onPause()
        viewModel.destroyChat()
    }
}
