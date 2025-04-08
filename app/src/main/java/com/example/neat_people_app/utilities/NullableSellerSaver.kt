package com.example.neat_people_app.utilities

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.example.neat_people_app.ui.content.Seller

// Saver for nullable Seller
val NullableSellerSaver: Saver<Seller?, Any> = listSaver(
    save = { seller ->
        (if (seller != null) listOf(seller.id, seller.name) else null)!!
    },
    restore = { list ->
        if (list.size == 2) Seller(list[0], list[1]) else null
    }
)