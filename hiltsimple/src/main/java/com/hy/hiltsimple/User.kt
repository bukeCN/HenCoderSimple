package com.hy.hiltsimple

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
data class User constructor(var name: String,var mood: String) {

    @Inject constructor() : this("静静","无语")

}