package com.example.myprofile.ui.main.add_contacts

import android.os.Bundle
import android.util.Log
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
import com.example.myprofile.utils.extentions.showShortToast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactsFragment : BaseFragment<FragmentAddContactsBinding>(FragmentAddContactsBinding::inflate) {

    private val viewModel: AddContactsViewModel by viewModels()

    private val adapter: AddContactsAdapter by lazy {
        AddContactsAdapter(object : OnAddContactClickListener {
            override fun onContactClick(
                contact: User,
                extras: FragmentNavigator.Extras
            ) {
                val action = AddContactsFragmentDirections.actionAddContactsFragmentToDetailViewFragment(contact.id)
                findNavController().navigate(action, extras)
            }

            override fun onAddContact(contactId: Long) {
                viewModel.addContactToUser(contactId)
            }

//            override fun onContactDelete(contactId: Long) {
//                viewModel.deleteContactFromUser(contactId)
//                requireContext().showShortToast("onContactDelete")
//            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        observeUiState()
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
                        is UiState.Error -> showError(it.message)
                    }
                }
            }
        }
    }

    private fun showContactList(users: List<User>) {
        binding.progressBar.visibility = View.GONE
        adapter.submitList(users)
    }

    private fun showError(error: String) {
        binding.progressBar.visibility = View.GONE
        Log.d("FAT_AddContactFrag", "UiState.Error = $error")
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }



    private fun setupToolbar() {
        with(binding.toolbar) {
            // when back button is pressed
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            inflateMenu(R.menu.contacts_menu)
            setOnMenuItemClickListener {
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