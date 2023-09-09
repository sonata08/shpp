package com.example.myprofile.ui.contacts


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
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
import com.example.myprofile.utils.extentions.showShortToast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ContactsFragment : Fragment(), OnContactClickListener {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactsViewModel by navGraphViewModels(
        R.id.contactsFragment,
        factoryProducer = {
            ContactsViewModelFactory(
                ContactsRepository()
            )
        }
    )
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

        setupToolbar()
        adapter = ContactsAdapter(this)
        setupRecyclerView()
        setupAddContactListener()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsFlow.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun setupToolbar() {
        with(binding) {
            toolbar.inflateMenu(R.menu.contacts_menu)
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search -> {
                        requireContext().showShortToast("SEARCH")
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun setupAddContactListener() {
        binding.tvAddContact.setOnClickListener {
            val action =
                ContactsFragmentDirections.actionContactsFragmentToAddContactDialogFragment()
            findNavController().navigate(action)
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

    override fun onContactDelete(contact: Contact) {
        val position = adapter.currentList.indexOf(contact)
        deleteContactWithSnackbar(contact, position, binding.recyclerView)
    }

    override fun onContactClick(contact: Contact, extras: FragmentNavigator.Extras) {
        val action =
            ContactsFragmentDirections.actionContactsFragmentToDetailViewFragment(contact.id)
        findNavController().navigate(action, extras)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}