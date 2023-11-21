package com.haikalzain.inventorypro.di

import com.haikalzain.inventorypro.viewmodel.AddItemViewModel
import com.haikalzain.inventorypro.viewmodel.SpreadListViewModel
import com.haikalzain.inventorypro.viewmodel.SpreadViewModel
import com.haikalzain.inventorypro.viewmodel.TemplateListViewModel
import com.haikalzain.inventorypro.viewmodel.TemplateViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
        viewModel { TemplateListViewModel(androidContext()) }
        viewModel { SpreadListViewModel(androidContext()) }
    viewModel{ TemplateViewModel(get(),androidContext() ) }
    viewModel{ SpreadViewModel(get(),androidContext() ) }
    viewModel{ AddItemViewModel(get(),androidContext() ) }
}