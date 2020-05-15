package com.chelseatroy.canary

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.chelseatroy.canary.ui.main.MoodEntryFragment
import com.chelseatroy.canary.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {
    lateinit var sectionsPagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        val viewPager = findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val floatingActionButton: FloatingActionButton = findViewById(R.id.add_entry_button)
        floatingActionButton.setOnClickListener { view ->
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val existingMoodEntryFragment = supportFragmentManager.findFragmentByTag(getString(R.string.mood_entry_fragment_tag))
            if (existingMoodEntryFragment != null) {
                fragmentTransaction.remove(existingMoodEntryFragment)
            }
            fragmentTransaction.addToBackStack(null)
            val dialogFragment = MoodEntryFragment()
            dialogFragment.show(fragmentTransaction, getString(R.string.mood_entry_fragment_tag))
        }
    }
}