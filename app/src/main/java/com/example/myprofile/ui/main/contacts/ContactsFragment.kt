package com.example.myprofile.ui.main.contacts


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
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
import com.example.myprofile.databinding.FragmentContactsBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.main.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.main.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.main.contacts.adapter.OnContactClickListener
import com.example.myprofile.ui.main.contacts.adapter.SwipeToDeleteCallback
import com.example.myprofile.ui.main.viewpager.ViewPagerFragment
import com.example.myprofile.ui.main.viewpager.ViewPagerFragment.Companion.SETTINGS_FRAGMENT
import com.example.myprofile.ui.main.viewpager.ViewPagerFragmentDirections
import com.example.myprofile.utils.showError
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val viewModel: ContactsViewModel by viewModels()

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
                    ViewPagerFragmentDirections.actionViewPagerFragmentToDetailViewFragment(contact.contact.id)
                findNavController().navigate(action, extras)
            }

            override fun onContactLongClick(contactPosition: Int) {
                viewModel.activateMultiselectMode(contactPosition)
                binding.fab.show()
                setFabOnclickListener()
            }

            override fun onItemSelect(contactPosition: Int, isChecked: Boolean) {
                viewModel.makeSelected(contactPosition, isChecked)
                // If no contacts are selected -> deactivate MultiselectMode and hide FAB
                if (viewModel.isNothingSelected()) {
                    binding.fab.hide()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        setupAddContactListener()
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
        binding.progressBar.visibility = View.GONE
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsListFlow.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
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

    private fun setupToolbar() {
        with(binding.toolbar) {
            // when back button is pressed
            setNavigationOnClickListener {
                val viewPagerFragment = requireParentFragment() as ViewPagerFragment
                viewPagerFragment.goToFragment(SETTINGS_FRAGMENT)
            }
            inflateMenu(R.menu.contacts_menu)
            setupSearchListener()
        }
    }

    private fun setupSearchListener() {
        val searchView =
            binding.toolbar.menu.findItem(R.id.search).actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(object :
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

    private fun setupAddContactListener() {
        binding.tvAddContact.setOnClickListener {
            val action =
                ViewPagerFragmentDirections.actionViewPagerFragmentToAddContactsFragment()
            findNavController().navigate(action)
        }
    }

    private fun setFabOnclickListener() {
        binding.fab.setOnClickListener { _ ->
            viewModel.deleteContacts()
            viewModel.deactivateMultiselectMode()
            binding.fab.hide()
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