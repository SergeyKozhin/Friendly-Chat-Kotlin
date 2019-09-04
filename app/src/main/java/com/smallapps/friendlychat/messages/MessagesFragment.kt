package com.smallapps.friendlychat.messages

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.smallapps.friendlychat.databinding.FragmentMessagesBinding
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.util.Log
import android.view.*
import com.google.firebase.auth.FirebaseAuth
import com.smallapps.friendlychat.R

// Main chat fragment
class MessagesFragment : Fragment() {

    companion object {
        const val RC_PHOTO_PICKER = 2
        const val RECYCLER_STATE = "recycler state"
    }

    // ViewModel lazy initialization
    private val viewModel: MessagesViewModel by lazy {
        ViewModelProvider(this, MessagesViewModelFactory(requireNotNull(this.activity).application))
            .get(MessagesViewModel::class.java)
    }

    private lateinit var binding: FragmentMessagesBinding
    private var savedState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflation and data binding setting
        binding =
            FragmentMessagesBinding.inflate(inflater, container, false)

        //Binding ViewModel to view
        binding.viewModel = viewModel

        // Setting lifecycle owner for observers to work
        binding.lifecycleOwner = this

        // Setting RecyclerView Adapter for list of messages
        val adapter = MessageAdapter()
        binding.messageListView.adapter = adapter
        if (savedInstanceState != null) {
            savedState = savedInstanceState.getParcelable(RECYCLER_STATE)
        }

        // TextChangeListener for input message field to enable or disable send button
        binding.messageEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    viewModel.setMessageText(p0.toString())
                } else {
                    viewModel.setMessageText(null)
                }

            }
        })

        // Observer for handling message sending
        viewModel.sendingMessage.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.sendMessage()
                binding.messageEditText.setText("")
                viewModel.onSendMessageCompleted()
            }
        })

        // Observer for handling image picking
        viewModel.pickingImage.observe(viewLifecycleOwner, Observer {
            if (it) {
                initiatePhotoPicking()
                viewModel.onPickImageCompleted()
            }
        })

        // Observer for updating list
        viewModel.friendlyMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                val initialSize = adapter.itemCount
                adapter.submitList(
                    it
                ) {
                    if (savedState != null) {
                        binding.messageListView.layoutManager!!.onRestoreInstanceState(savedState)
                        savedState = null
                    } else if (it.size >= initialSize) {
                        binding.messageListView.scrollToPosition(it.size - 1)
                    }
                }
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initiatePhotoPicking() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("image/jpeg")
            .putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(
            Intent.createChooser(intent, "Complete action using"),
            RC_PHOTO_PICKER
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(
            RECYCLER_STATE,
            binding.messageListView.layoutManager?.onSaveInstanceState()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                FirebaseAuth.getInstance().signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(
                context!!.contentResolver.openInputStream(data?.data!!),
                null,
                options
            )
            viewModel.currentMessageImgHeight = options.outHeight
            viewModel.currentMessageImgWidth = options.outWidth
            viewModel.uploadImage(data.data)
        }
    }
}
