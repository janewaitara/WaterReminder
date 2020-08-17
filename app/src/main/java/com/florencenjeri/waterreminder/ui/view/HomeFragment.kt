package com.florencenjeri.waterreminder.ui.view

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.florencenjeri.waterreminder.R
import com.florencenjeri.waterreminder.ui.viewModel.HomeViewModel
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_home.*
import me.tankery.lib.circularseekbar.CircularSeekBar
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HomeFragment : Fragment() {
    val homeViewModel: HomeViewModel by viewModel()
    private var userId: Long = 1
    var dailyGoal: Int = 0
    var dailyProgressAchieved: Int = 0
    lateinit var glassCapacity: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        zTransitionFromProfileSettingsFragment()
        zTransitionToUserProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        homeViewModel.setUpReminder()
        dailyProgressButton.isSelected = true
        homeViewModel.getUserSettingsData().observe(viewLifecycleOwner, Observer { settings ->
            //Initialize the global variables
            dailyGoal = settings.numOfGlasses
            glassCapacity = settings.cupMeasurements
            userId = settings.id
            welcomeTextView.text = String.format(getString(R.string.hello_user), settings.name)
            String.format(getString(R.string.notification_title), settings.name)
            Log.d("Settings", settings.toString())
            if (checkWaterConsumptionGoalAchieved()) {
                homeViewModel.stopReminder()
            }

            val firstLetter = settings.name.substring(0, 1).toUpperCase()
            val drawable = homeViewModel.generateProfileImage(firstLetter)
            myProfileImage.setImageDrawable(drawable)
            val goalText = String.format(
                getString(R.string.water_consumption_goal),
                dailyGoal - dailyProgressAchieved
            )
            val styledText: CharSequence = Html.fromHtml(goalText)
            goalsTextView.text = styledText
        })

        fab.setOnClickListener {
            //TODO : Increase the capacity of water consumed that day
            dailyProgressAchieved++
            Log.d("Progress", dailyProgressAchieved.toString())

            //TODO : Fix this bug
            if (dailyGoal - dailyProgressAchieved >= 0) {
                setUpCircularSeekbar()
                val goalText = String.format(
                    getString(R.string.water_consumption_goal),
                    dailyGoal - dailyProgressAchieved
                )
                val styledText: CharSequence = Html.fromHtml(goalText)
                goalsTextView.text = styledText
            }
        }
    }

    fun checkWaterConsumptionGoalAchieved(): Boolean = dailyProgressAchieved == dailyGoal

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.userProfileFragment) {
            navigateToSettingsScreen()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToSettingsScreen() {
        val action =
            HomeFragmentDirections.actionHomeFragmentToSettingsFragment(userId)
        findNavController().navigate(action)
    }

    private fun setUpCircularSeekbar() {
        circularSeekBar.max = dailyGoal.toFloat()
        circularSeekBar.progress = dailyProgressAchieved.toFloat()

        circularSeekBar.setOnSeekBarChangeListener(object :
            CircularSeekBar.OnCircularSeekBarChangeListener {
            override fun onProgressChanged(
                circularSeekBar: CircularSeekBar?,
                progress: Float,
                fromUser: Boolean
            ) {
                //On fab clicked, update the progress bar
                circularSeekBar?.progress = dailyProgressAchieved.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
                TODO("Not yet implemented")
            }


        })
    }

    private fun zTransitionFromProfileSettingsFragment() {
        //From Profile to Home Fragment
        val forward = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        enterTransition = forward
        //Back to the ProfileSettings Fragment
        val backward = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        returnTransition = backward
    }

    private fun zTransitionToUserProfileFragment() {
        //UserProfile back to Home Fragment Navigation
        val backward = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        reenterTransition = backward
        //Home to UserProfile navigation
        val forward = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = forward
    }

}