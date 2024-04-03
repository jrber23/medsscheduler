package com.example.mitfg.data.adverseEffects.model

data class AdverseEffectDto(
    var id: String,
    val name: String
) {

    constructor() : this("", "")

}