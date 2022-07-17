package com.example.footballapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.adapter.ClubAdapter
import com.example.footballapp.databinding.ActivityLeagueBinding
import com.example.footballapp.model.Data
import com.example.footballapp.network.ApiInstance
import com.example.footballapp.repository.LeagueRepository
import com.example.footballapp.util.Status
import com.example.footballapp.viewmodel.LeagueViewModel
import com.example.footballapp.viewmodel.LeagueViewModelFactory
import com.squareup.picasso.Picasso

class LeagueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeagueBinding
    private lateinit var clubAdapter: ClubAdapter
    private lateinit var viewModel: LeagueViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeagueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {
        val data = intent.getParcelableExtra<Data>("data")!!

        binding.textView.text = "${data.name} 2021-2022"
        Picasso.get()
            .load(data.logos.light)
            .into(binding.imageView)

        val leagueRepository = LeagueRepository(ApiInstance.apiService)
        viewModel = ViewModelProvider(
            this,
            LeagueViewModelFactory(leagueRepository)
        )[LeagueViewModel::class.java]
        clubAdapter = ClubAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@LeagueActivity)
            adapter = clubAdapter
        }
        viewModel.getLeagueById(data.id)
        observe()
        binding.btnBack.setOnClickListener {
            onBackPressed()
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
                    clubAdapter.submitList(it.data!!.data.standings)
                }
            }
        }
    }
}