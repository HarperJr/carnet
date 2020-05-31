package com.harper.carnet.domain.map.regions

import com.harper.carnet.domain.model.Region
import io.reactivex.Observable

class RegionsProvider {

    fun provideRegions(): Observable<List<Region>> {
        return Observable.just(listOf(
            Region(0L, "vladimir_city", "Vladimir", 0, 20564, 56.222505, 56.091189, 56.138768, 40.578226),
            Region(0L, "moscow_city", "Moscow", 0, 149532, 55.971268, 55.531235, 37.982399, 37.206352)
        ))
    }
}