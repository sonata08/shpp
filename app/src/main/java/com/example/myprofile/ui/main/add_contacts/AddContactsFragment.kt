package com.example.myprofile.ui.main.add_contacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.R
import com.example.myprofile.data.model.User
import com.example.myprofile.data.network.model.UiState
import com.example.myprofile.databinding.FragmentAddContactsBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.main.add_contacts.adapter.AddContactsAdapter
import com.example.myprofile.ui.main.add_contacts.adapter.OnAddContactClickListener
import com.example.myprofile.ui.main.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.utils.extentions.hide
import com.example.myprofile.ui.utils.extentions.show
import com.example.myprofile.utils.showError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactsFragment :
    BaseFragment<FragmentAddContactsBinding>(FragmentAddContactsBinding::inflate) {

    private val viewModel: AddContactsViewModel by viewModels()

    private val adapter: AddContactsAdapter by lazy {
        AddContactsAdapter(object : OnAddContactClickListener {
            override fun onContactClick(
                contact: User,
                extras: FragmentNavigator.Extras
            ) {
                val action =
                    AddContactsFragmentDirections.actionAddContactsFragmentToDetailViewFragment(
                        contact.id
                    )
                findNavController().navigate(action, extras)
            }

            override fun onAddContact(contactId: Long) {
                viewModel.addContactToUser(contactId)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        observeUiState()
        observeSearchState()
        viewModel.getContacts()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateFlow.collect {
                    when (it) {
                        is UiState.Initial -> {}
                        is UiState.Success -> showContactList(it.data)
                        is UiState.Loading -> showProgressBar()
                        is UiState.Error -> showError(binding.root, binding.progressBar, it.message)
                    }
                }
            }
        }
    }

    private fun showContactList(users: List<User>) {
        binding.progressBar.hide()
        adapter.submitList(users)
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

    private fun setupToolbar() {
        with(binding.toolbar) {
            // when back button is pressed
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            inflateMenu(R.menu.add_contacts_menu)
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
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterUsers(newText)
                return true
            }
        })

    }

    private fun setupRecyclerView() {
        val itemDecoration = ContactsItemDecoration(
            R.dimen.basic_layout_horizontal_margins,
            R.dimen.margin_between_items,
            R.dimen.margin_last_item_bottom
        )
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(root.context)
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.adapter = adapter
        }
    }
}