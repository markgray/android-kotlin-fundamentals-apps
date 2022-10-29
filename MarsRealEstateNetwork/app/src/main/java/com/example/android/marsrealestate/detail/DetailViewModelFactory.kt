/*
 *  Copyright 2019, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.marsrealestate.network.MarsProperty

/**
 * Simple ViewModel factory that provides the MarsProperty and context to the ViewModel.
 * Skeleton code for later code lab
 */
@Suppress("unused") // Skeleton code for later code lab
class DetailViewModelFactory(
    private val marsProperty: MarsProperty,
    private val application: Application) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the given [Class]. After a sanity check to make sure we are only
     * being asked to create a [DetailViewModel] we return a new instance of [DetailViewModel]
     * constructed to use our field [marsProperty] as the [MarsProperty] it is to display, and
     * our field [application] as the [Application] to use for context when needed.
     *
     * @param modelClass a [Class] whose instance is requested
     * @param T          The type parameter for the ViewModel.
     * @return a newly [DetailViewModel] ViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("unchecked_cast") // The above if statement checks the cast
            return DetailViewModel(marsProperty, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
