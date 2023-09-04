package com.example.myprofile.ui.contacts


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.databinding.FragmentContactsBinding
import com.example.myprofile.ui.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.contacts.adapter.OnContactClickListener
import com.example.myprofile.ui.contacts.adapter.SwipeToDeleteCallback
import com.example.myprofile.ui.contacts.dialog.AddContactCallback
import com.example.myprofile.ui.contacts.dialog.AddContactDialogFragment
import com.example.myprofile.utils.extentions.showShortToast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ContactsFragment : Fragment(), AddContactCallback, OnContactClickListener {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactsViewModel by activityViewModels {
        ContactsViewModelFactory(
            ContactsRepository()
        )
    }
    private lateinit var adapter: ContactsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = ContactsAdapter(this)
        setupRecyclerView()

        val isLargeLayout = resources.getBoolean(R.bool.large_layout)
        binding.tvAddContact.setOnClickListener {
            showDialog(isLargeLayout)
        }

        // opens SingUpActivity just for convenience to check CustomView
//        binding.btnBack.setOnClickListener {
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsFlow.collect {
                    adapter.submitList(it)
                }
            }
        }

    }

    private fun setupRecyclerView() {
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        val itemDecoration = ContactsItemDecoration(
            R.dimen.basic_layout_horizontal_margins,
            R.dimen.margin_between_items,
            R.dimen.margin_last_item_bottom,
            R.drawable.recyclerview_item_shape
        )
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(root.context)
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.adapter = adapter
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun deleteContactWithSnackbar(contact: Contact, position: Int, view: View) {
        viewModel.deleteContact(contact)
        Snackbar.make(view, R.string.contact_deleted, Snackbar.LENGTH_SHORT)
            .setAction(R.string.undo) { viewModel.addContact(contact, position) }
            .show()
    }

    private val swipeHandler = object : SwipeToDeleteCallback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.bindingAdapterPosition
            val contact = adapter.getContactAtPosition(position)
            deleteContactWithSnackbar(contact, position, viewHolder.itemView)
        }
    }

    // shows fullscreen dialog if small layout and not fullscreen otherwise
    private fun showDialog(isLargeLayout: Boolean) {
        val fragmentManager = childFragmentManager
        val newFragment = AddContactDialogFragment()
        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme)
            newFragment.show(fragmentManager, ADD_CONTACT_DIALOG)
        } else {
            // The device is smaller, so show the fragment fullscreen
            val transaction = fragmentManager.beginTransaction()
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onContactAdded(contact: Contact) {
        viewModel.addContact(contact)
    }

    override fun onContactDelete(contact: Contact) {
        val position = adapter.currentList.indexOf(contact)
        deleteContactWithSnackbar(contact, position, binding.recyclerView)
    }

    override fun onContactClick(contact: Contact) {
        requireContext().showShortToast("Open ${contact.username}'s details")
        val action = ContactsFragmentDirections.actionContactsFragmentToDetailViewFragment(contact.id)
        findNavController().navigate(action)
    }

    companion object {
        const val ADD_CONTACT_DIALOG = "AddContactDialogFragment"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}