package com.haikalzain.inventorypro.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haikalzain.inventorypro.R
import com.haikalzain.inventorypro.viewmodel.SpreadViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<SpreadViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.spreadsheet

    }

}