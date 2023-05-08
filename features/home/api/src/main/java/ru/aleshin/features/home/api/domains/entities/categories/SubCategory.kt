/*
 * Copyright 2023 Stanislav Aleshin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 */
package ru.aleshin.features.home.api.domains.entities.categories

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Stanislav Aleshin on 21.02.2023.
 */
@Parcelize
data class SubCategory(
    val id: Int = 0,
    val name: String,
    val mainCategory: MainCategory,
    val description: String? = null,
) : Parcelable {

    companion object {
        fun absentSubCategory(mainCategory: MainCategory) = SubCategory(
            id = -1,
            name = "Отсутствует",
            mainCategory = mainCategory,
        )
    }
}
