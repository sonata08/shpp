package com.example.myprofile.ui.fragments.contacts


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
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
import com.example.myprofile.data.repository.impl.ContactsRepositoryImpl
import com.example.myprofile.databinding.FragmentContactsBinding
import com.example.myprofile.ui.fragments.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.fragments.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.fragments.contacts.adapter.OnContactClickListener
import com.example.myprofile.ui.fragments.contacts.adapter.SwipeToDeleteCallback
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragment
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragmentDirections
import com.example.myprofile.utils.extentions.showShortToast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


//@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

//    private val viewModel: ContactsViewModel by navGraphViewModels(
//        R.id.contactsFragment,
//        factoryProducer = {
//            ContactsViewModelFactory(
//                ContactsRepositoryImpl()
//            )
//        }
//    )

    private val viewModel: ContactsViewModel by activityViewModels {
        ContactsViewModelFactory(
                ContactsRepositoryImpl()
            )
    }

    private val adapter: ContactsAdapter by lazy {
        ContactsAdapter(object : OnContactClickListener {
            override fun onContactDelete(contactPosition: Int) {
                deleteContactWithSnackbar(contactPosition)
            }

            override fun onContactClick(contact: Contact, extras: FragmentNavigator.Extras) {
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToDetailViewFragment(contact.id)
                findNavController().navigate(action, extras)
            }

            override fun onContactLongClick(contact: Contact) {
                Snackbar.make(binding.root, "LONG CLICK", Snackbar.LENGTH_SHORT).show()
                viewModel.toMultiSelectMode()
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.isMultiSelectMode.collect {
                            if (it) {
                                // TODO:
                            }
                        }
                    }
                }

            }
        })
    }

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
        with(binding.toolbar) {
            setNavigationOnClickListener {
                findNavController().popBackStack()
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





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}