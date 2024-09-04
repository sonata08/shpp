package com.example.myprofile.ui.main.contacts


import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import com.example.myprofile.data.notifications.NotificationBuilder
import com.example.myprofile.data.notifications.PermissionUtils
import com.example.myprofile.databinding.FragmentContactsBinding
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.ui.main.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.main.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.main.contacts.adapter.OnContactClickListener
import com.example.myprofile.ui.main.contacts.adapter.SwipeToDeleteCallback
import com.example.myprofile.ui.main.viewpager.TabFragments
import com.example.myprofile.ui.main.viewpager.ViewPagerFragment
import com.example.myprofile.ui.main.viewpager.ViewPagerFragmentDirections
import com.example.myprofile.ui.permissions.NotificationRationale
import com.example.myprofile.ui.utils.NOTIFICATION_RATIONALE_TAG
import com.example.myprofile.ui.utils.extentions.hide
import com.example.myprofile.ui.utils.extentions.show
import com.example.myprofile.utils.extentions.openAppSettings
import com.example.myprofile.utils.showError
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    @Inject
    lateinit var permissionUtils: PermissionUtils
    @Inject
    lateinit var notificationBuilder: NotificationBuilder
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

    private fun setupToolbar() {
        with(binding.toolbar) {
            // when back button is pressed
            setNavigationOnClickListener {
                val viewPagerFragment = requireParentFragment() as ViewPagerFragment
                viewPagerFragment.goToFragment(TabFragments.SETTINGS_FRAGMENT.ordinal)
            }
            inflateMenu(R.menu.contacts_menu)
//            setupSearchListener()
            menu.findItem(R.id.search).setOnMenuItemClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkPermissionAndShowNotification()
                } else {
                    notificationBuilder.sendNotification()
                }
                true
            }
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                notificationBuilder.sendNotification()
            } else {
                showRationale()
            }
        }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionAndShowNotification() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        when {
            permissionUtils.isNotificationPermissionGranted() -> {
                notificationBuilder.sendNotification()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                showRationale()
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }


    private fun showRationale() {
        val rationale = NotificationRationale()
        rationale.show(parentFragmentManager, NOTIFICATION_RATIONALE_TAG)
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