package com.example.footballapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.adapter.FootballAdapter
import com.example.footballapp.database.FootballDatabase
import com.example.footballapp.databinding.ActivityMainBinding
import com.example.footballapp.network.ApiInstance
import com.example.footballapp.repository.FootballRepository
import com.example.footballapp.util.NetworkHelper
import com.example.footballapp.util.Status
import com.example.footballapp.viewmodel.FootballViewModel
import com.example.footballapp.viewmodel.FootballViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: FootballViewModel
    private lateinit var footballAdapter: FootballAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {
        val networkHelper = NetworkHelper(this)
        val footballRepository =
            FootballRepository(ApiInstance.apiService, FootballDatabase.invoke(this))
        val footballViewModelFactory = FootballViewModelFactory(networkHelper, footballRepository)
        viewModel = ViewModelProvider(this, footballViewModelFactory)[FootballViewModel::class.java]

        setupRv()
        observe()
    }

    private fun setupRv() {
        footballAdapter = FootballAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = footballAdapter
        }
        footballAdapter.onItemClick = {
            val bundle = bundleOf("data" to it)
            val intent = Intent(this, LeagueActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun observe() {
        viewModel.response.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.recyclerView.isVisible = false
                    binding.progress.isVisible = true
                }
                Status.ERROR -> {
                    binding.progress.isVisible = false
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    binding.progress.isVisible = false
                    binding.recyclerView.isVisible = true
                    footballAdapter.submitList(it.data!!.data)
                }
            }
        }
    }
}