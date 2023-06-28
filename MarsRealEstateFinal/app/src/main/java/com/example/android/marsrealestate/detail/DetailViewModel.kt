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
import androidx.lifecycle.*
import com.example.android.marsrealestate.network.MarsProperty
import com.example.android.marsrealestate.R

/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [MarsProperty].
 *
 *  @param marsProperty the [MarsProperty] we are to display
 *  @param app the [Application] this activity is running in
 */
class DetailViewModel(
    marsProperty: MarsProperty,
    app: Application
) : AndroidViewModel(app) {

    /**
     * The internal [MutableLiveData] of the selected property
     */
    private val _selectedProperty = MutableLiveData<MarsProperty>()

    /**
     * The external read-only [LiveData] for the selected property [_selectedProperty]. A binding
     * expression for the "app:imageUrl" attribute in the layout file layout/fragment_detail.xml
     * references this property in order to feed its `imgSrcUrl` URL to the `bindImage` binding
     * adapter handling the "app:imageUrl" attribute. In addition the [displayPropertyPrice] and
     * [displayPropertyType] properties observe this property in order to transform it into their
     * own values.
     */
    val selectedProperty: LiveData<MarsProperty>
        get() = _selectedProperty

    /**
     * Initialize the `_selectedProperty` `MutableLiveData` with our parameter `marsProperty`
     */
    init {
        _selectedProperty.value = marsProperty
    }

    /**
     * The displayPropertyPrice formatted Transformation Map [LiveData], which displays the sale
     * or rental price. This gets generated whenever the [selectedProperty] property changes value
     * by producing a [LiveData] wrapped [String] from the `price` field of [selectedProperty]
     * formatted using the format string R.string.display_price_monthly_rental ("$%,.0f/month")
     * if the `isRental` property of [selectedProperty] is `true` or the format string
     * R.string.display_price ("$%,.0f") if it is `false`. It is referenced by a binding expression
     * for the "android:text" attribute of the `TextView` with ID R.id.price_value_text in the
     * layout file layout/fragment_detail.xml and sets the text of that `TextView` whenever
     * [displayPropertyPrice] changes value.
     */
    val displayPropertyPrice: LiveData<String> = selectedProperty.map {
        app.applicationContext.getString(
            when (it.isRental) {
                true -> R.string.display_price_monthly_rental
                false -> R.string.display_price
            }, it.price)
    }

    /**
     * The displayPropertyType formatted Transformation Map [LiveData], which displays the
     * "For Rent/Sale" [String]. This gets generated whenever the [selectedProperty] property
     * changes value by producing a [LiveData] wrapped [String] depending on the value of the
     * `isRental` property of [selectedProperty]: "Rent" if it is `true` or "Sale" if it is `false`.
     * It is referenced by a binding expression for the "android:text" attribute of the `TextView`
     * with ID R.id.property_type_text in the layout file layout/fragment_detail.xml and sets the
     * text of that `TextView` whenever [displayPropertyType] changes value.
     */
    val displayPropertyType: LiveData<String> = selectedProperty.map {
        app.applicationContext.getString(R.string.display_type,
            app.applicationContext.getString(
                when (it.isRental) {
                    true -> R.string.type_rent
                    false -> R.string.type_sale
                }))
    }
}

