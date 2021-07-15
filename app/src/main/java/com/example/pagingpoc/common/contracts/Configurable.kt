package com.example.pagingpoc.common.contracts

/**
 * This contract provides a method for configuration.
 *
 * @param T the model type used for configuration.
 */
internal interface Configurable<T> {

    /**
     * Configure the instance using the provided model.
     *
     * @param model the configuration model.
     */
    fun configure(model: T)
}
