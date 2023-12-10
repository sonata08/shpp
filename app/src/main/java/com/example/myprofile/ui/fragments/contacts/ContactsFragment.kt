package com.example.myprofile.ui.fragments.contacts


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
import com.example.myprofile.data.model.ContactMultiselect
import com.example.myprofile.databinding.FragmentContactsBinding
import com.example.myprofile.ui.fragments.BaseFragment
import com.example.myprofile.ui.fragments.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.fragments.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.fragments.contacts.adapter.OnContactClickListener
import com.example.myprofile.ui.fragments.contacts.adapter.SwipeToDeleteCallback
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragment
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragment.Companion.SETTINGS_FRAGMENT
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragmentDirections
import com.example.myprofile.utils.extentions.showShortToast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val viewModel: ContactsViewModel by activityViewModels()

    private val adapter: ContactsAdapter by lazy {
        ContactsAdapter(object : OnContactClickListener {
            override fun onContactDelete(contactPosition: Int) {
                deleteContactWithSnackbar(contactPosition)
            }

            override fun onContactClick(
                contact: ContactMultiselect,
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
        showContacts()
        deactivateMultiselectMode()
    }

    private fun showContacts() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsFlow.collect {
                    adapter.submitList(it){
                        // scrolls to the selected contact in multiselect mode
                        binding.recyclerView.scrollToPosition(viewModel.scrollPosition)
                    }
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
                ViewPagerFragmentDirections.actionViewPagerFragmentToAddContactDialogFragment()
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
            R.dimen.margin_last_item_bottom
        )
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(root.context)
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.adapter = adapter
            // uncomment to activate swipe-to-delete behavior
//            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun deleteContactWithSnackbar(position: Int) {
        viewModel.deleteContact(position)
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
            deleteContactWithSnackbar(position)
        }
    }

    private fun deactivateMultiselectMode() {
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                viewModel.deactivateMultiselectMode()            }
        })

    }

//    override fun onPause() {
//        super.onPause()
//        viewModel.deactivateMultiselectMode()
//    }
}