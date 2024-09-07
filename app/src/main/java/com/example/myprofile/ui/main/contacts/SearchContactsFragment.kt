package com.example.myprofile.ui.main.contacts

import android.app.NotificationManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.model.UserMultiselect
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.databinding.FragmentSearchContactsBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.main.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.main.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.main.contacts.adapter.OnContactClickListener
import com.example.myprofile.ui.main.contacts.adapter.SwipeToDeleteCallback
import com.example.myprofile.ui.main.viewpager.ViewPagerFragmentDirections
import com.example.myprofile.ui.utils.extentions.hide
import com.example.myprofile.ui.utils.extentions.show
import com.example.myprofile.utils.showError
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchContactsFragment : BaseFragment<FragmentSearchContactsBinding>(FragmentSearchContactsBinding::inflate) {

    @Inject lateinit var notificationManager: NotificationManager
    private val viewModel: ContactsViewModel by activityViewModels()

    private val adapter: ContactsAdapter by lazy {
        ContactsAdapter(object : OnContactClickListener {
            override fun onContactDelete(contactId: Long) {
                deleteContactWithSnackbar(contactId)
            }

            override fun onContactClick(
                contact: UserMultiselect,
                extras: FragmentNavigator.Extras
            ) {
                val action =
                    SearchContactsFragmentDirections.actionSearchContactsFragmentToDetailViewFragment(contact.contact.id)
                findNavController().navigate(action, extras)
            }

            override fun onContactLongClick(contactPosition: Int) {
                viewModel.activateMultiselectMode(contactPosition)
                binding.fabDeleteContacts.show()
                setFabOnclickListener()
            }

            override fun onItemSelect(contactPosition: Int, isChecked: Boolean) {
                viewModel.makeSelected(contactPosition, isChecked)
                // If no contacts are selected -> deactivate MultiselectMode and hide FAB
                if (viewModel.isNothingSelected()) {
                    binding.fabDeleteContacts.hide()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // close notification
        notificationManager.cancelAll()
        setupSearchListener()
        setupRecyclerView()
        observeUiState()
        observeSearchState()
        deactivateMultiselectMode()
        viewModel.getUserContacts()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateFlow.collect {
                    when (it) {
                        is UiState.Initial -> {}
                        is UiState.Success -> showContacts()
                        is UiState.Loading -> showProgressBar()
                        is UiState.Error -> showError(binding.root, binding.progressBar, it.message)
                    }
                }
            }
        }
    }

    private fun showContacts() {
        binding.progressBar.hide()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsListFlow.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun observeSearchState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResult.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun setupSearchListener() {
        with(binding.searchView) {
            isIconified = false
            requestFocus()
        }
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filterUsers(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterUsers(newText)
                return true
            }
        })
    }

    private fun setFabOnclickListener() {
        binding.fabDeleteContacts.setOnClickListener { _ ->
            viewModel.deleteContacts()
            viewModel.deactivateMultiselectMode()
            binding.fabDeleteContacts.hide()
        }
    }

    private fun setupRecyclerView() {
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        val itemDecoration = ContactsItemDecoration(
            R.dimen.basic_layout_horizontal_margins,
            R.dimen.margin_between_items,
            R.dimen.margin_0
        )
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(root.context)
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(itemDecoration)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun deleteContactWithSnackbar(contactId: Long) {
        viewModel.deleteContactFromUser(contactId)
        showUndoSnackbar()
    }

    private fun showUndoSnackbar() {
        Snackbar.make(binding.root, R.string.contact_deleted, Snackbar.LENGTH_SHORT)
            .setAction(R.string.undo) { viewModel.restoreLastDeletedContact() }
            .show()
    }

    private val swipeHandler = object : SwipeToDeleteCallback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.bindingAdapterPosition
            val userMultiselect = adapter.currentList[position]
            val contactId = userMultiselect.contact.id
            deleteContactWithSnackbar(contactId)
        }
    }

    private fun deactivateMultiselectMode() {
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                viewModel.deactivateMultiselectMode()
            }
        })
    }

}