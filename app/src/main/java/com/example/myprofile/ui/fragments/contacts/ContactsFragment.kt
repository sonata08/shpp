package com.example.myprofile.ui.fragments.contacts


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.model.ContactMultiselect
import com.example.myprofile.data.repository.impl.ContactsRepositoryImpl
import com.example.myprofile.databinding.FragmentContactsBinding
import com.example.myprofile.ui.fragments.contacts.adapter.ContactsAdapter
import com.example.myprofile.ui.fragments.contacts.adapter.ContactsItemDecoration
import com.example.myprofile.ui.fragments.contacts.adapter.OnContactClickListener
import com.example.myprofile.ui.fragments.contacts.adapter.SwipeToDeleteCallback
import com.example.myprofile.ui.fragments.viewpager.CONTACTS_FRAGMENT
import com.example.myprofile.ui.fragments.viewpager.SETTINGS_FRAGMENT
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragment
import com.example.myprofile.ui.fragments.viewpager.ViewPagerFragmentDirections
import com.example.myprofile.utils.extentions.showShortToast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

const val TAG = "FAT_ContactsFrag"

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

            override fun onContactClick(contact: ContactMultiselect, extras: FragmentNavigator.Extras) {
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
                if(viewModel.isNothingSelected()) {
                    binding.fab.hide()
                }
            }
        })
    }

    // TODO: disable transaction to detail view while in multiselect mode
    // TODO: disable swipe delete

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        setupToolbar()
        setupRecyclerView()
        setupAddContactListener()
        binding.fab.hide()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactsFlow.collect {
                    Log.d(TAG, "adapter.submitList")
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
                viewPagerFragment.viewPager.currentItem = SETTINGS_FRAGMENT
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
        Log.d(TAG, "onDestroyView")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        viewModel.deactivateMultiselectMode()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }
}