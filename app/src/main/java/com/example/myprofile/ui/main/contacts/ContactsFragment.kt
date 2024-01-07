package com.example.myprofile.ui.main.contacts


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
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
import com.example.myprofile.data.model.User
import com.example.myprofile.data.model.UserMultiselect
import com.example.myprofile.data.network.model.AuthUiStateTest
import com.example.myprofile.databinding.FragmentContactsBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.main.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.main.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.main.contacts.adapter.OnContactClickListener
import com.example.myprofile.ui.main.contacts.adapter.SwipeToDeleteCallback
import com.example.myprofile.ui.main.viewpager.ViewPagerFragment
import com.example.myprofile.ui.main.viewpager.ViewPagerFragment.Companion.SETTINGS_FRAGMENT
import com.example.myprofile.ui.main.viewpager.ViewPagerFragmentDirections
import com.example.myprofile.utils.extentions.showShortToast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val viewModelTest: ContactsTestViewModel by activityViewModels()
    private val viewModel: ContactsViewModel by viewModels()

    private val adapter: ContactsAdapter by lazy {
        ContactsAdapter(object : OnContactClickListener {
            override fun onContactDelete(contactPosition: Int) {
                deleteContactWithSnackbar(contactPosition)
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
                viewModelTest.activateMultiselectMode(contactPosition)
                binding.fab.show()
                setFabOnclickListener()
            }

            override fun onItemSelect(contactPosition: Int, isChecked: Boolean) {
                viewModelTest.makeSelected(contactPosition, isChecked)
                // If no contacts are selected -> deactivate MultiselectMode and hide FAB
                if (viewModelTest.isNothingSelected()) {
                    binding.fab.hide()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        observeUiState()
        setupAddContactListener()
        viewModel.getUserContacts()
//        showContacts()
        deactivateMultiselectMode()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authStateFlow.collect {
                    when (it) {
                        is AuthUiStateTest.Initial -> {}
                        is AuthUiStateTest.Success -> showContacts(it.data)
                        is AuthUiStateTest.Loading -> showProgressBar()
                        is AuthUiStateTest.Error -> showError(it.message)
                    }
                }
            }
        }
    }



    private fun showContacts(contacts: List<User>) {
        binding.progressBar.visibility = View.GONE
        adapter.submitList(contacts.map { UserMultiselect(it) }) {
            // scrolls to the selected contact in multiselect mode
            binding.recyclerView.scrollToPosition(viewModelTest.scrollPosition)
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(error: String) {
        // TODO: think what to do in case of error
        binding.progressBar.visibility = View.GONE
        Log.d("FAT_AddContactFrag", "UiState.Error = $error")
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun setupToolbar() {
        with(binding.toolbar) {
            // when back button is pressed
            setNavigationOnClickListener {
                val viewPagerFragment = requireParentFragment() as ViewPagerFragment
                viewPagerFragment.goToFragment(SETTINGS_FRAGMENT)
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

    private fun setupAddContactListener() {
        binding.tvAddContact.setOnClickListener {
            val action =
                ViewPagerFragmentDirections.actionViewPagerFragmentToAddContactsFragment()
            findNavController().navigate(action)
        }
    }

    private fun setFabOnclickListener() {
        binding.fab.setOnClickListener { _ ->
            viewModelTest.deleteContacts()
            viewModelTest.deactivateMultiselectMode()
            binding.fab.hide()
        }
    }

    private fun setupRecyclerView() {
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        val itemDecoration = ContactsItemDecoration(
            R.dimen.basic_layout_horizontal_margins,
            R.dimen.margin_between_items,
            R.dimen.margin_last_item_bottom
        )
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(root.context)
            recyclerView.addItemDecoration(itemDecoration)
//            recyclerView.adapter = adapter
            // uncomment to activate swipe-to-delete behavior
//            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun deleteContactWithSnackbar(position: Int) {
        viewModelTest.deleteContact(position)
        showUndoSnackbar()
    }

    private fun showUndoSnackbar() {
        Snackbar.make(binding.root, R.string.contact_deleted, Snackbar.LENGTH_SHORT)
            .setAction(R.string.undo) { viewModelTest.restoreLastDeletedContact() }
            .show()
    }

    private val swipeHandler = object : SwipeToDeleteCallback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.bindingAdapterPosition
            deleteContactWithSnackbar(position)
        }
    }

    private fun deactivateMultiselectMode() {
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                viewModelTest.deactivateMultiselectMode()            }
        })

    }

//    override fun onPause() {
//        super.onPause()
//        viewModel.deactivateMultiselectMode()
//    }
}