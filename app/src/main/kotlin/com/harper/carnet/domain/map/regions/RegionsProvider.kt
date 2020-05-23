package com.harper.carnet.domain.map.regions

import com.harper.carnet.domain.model.Region
import io.reactivex.Observable

class RegionsProvider {

    fun provideRegions(): Observable<List<Region>> {
        return Observable.just(listOf(
            Region(0L, "vladimir_city", "Vladimir", 0, 20564, 40.406601, 40.423445, 56.138768, 56.175505),
            Region(0L, "moscow_city", "Moscow", 0, 149532, 40.406601, 40.423445, 56.138768, 56.175505)
        ))
    }
}