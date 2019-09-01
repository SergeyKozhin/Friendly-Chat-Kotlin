package com.smallapps.friendlychat.messages


import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.smallapps.friendlychat.databinding.MessangerListBinding
import kotlinx.android.synthetic.main.messanger_list.*

class MessagesFragment : Fragment() {

    private val viewModel: MessagesViewModel by lazy {
        ViewModelProviders.of(this).get(MessagesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding =
            MessangerListBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = MessageAdapter()
        binding.messageListView.adapter = adapter

        viewModel.hideProgressBar()
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

        binding.messageEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MESSAGE_LENGTH))

        viewModel.sendingMessage.observe(viewLifecycleOwner, Observer {
            if (it) {


                binding.messageEditText.setText("")
                viewModel.onSendMessageCompleted()
            }
        })

        viewModel.pickingImage.observe(viewLifecycleOwner, Observer {
            if (it) {
                // TODO
                viewModel.onPickImageCompleted()
            }
        })


        return binding.root
    }


}
