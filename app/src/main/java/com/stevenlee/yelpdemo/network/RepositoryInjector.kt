package com.stevenlee.yelpdemo.network

import com.stevenlee.yelpdemo.main.YelpRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(RetrofitModule::class)])
interface RepositoryInjector {
    fun inject(yelpRepository: YelpRepository)

    @Component.Builder
    interface Builder {
        fun build(): RepositoryInjector

        fun retrofitModule(retrofitModule: RetrofitModule): Builder
    }
}