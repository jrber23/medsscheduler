package com.example.mitfg.data.pharmacies

import com.example.mitfg.data.pharmacies.model.PharmacyDto
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PharmacyDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PharmacyDataSource {
    override suspend fun getAllPharmaciesWithinRadius(ubication: com.google.android.gms.maps.model.LatLng): Result<List<PharmacyDto?>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val docRef = firestore.collection("pharmacies")

                val list = mutableListOf<PharmacyDto?>()

                val center = GeoLocation(ubication.latitude, ubication.longitude)
                val radiusKm = 60.0 * 1000.0

                val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusKm)

                for (b in bounds) {
                    val q = docRef
                        .orderBy("geohash")
                        .startAt(b.startHash)
                        .startAt(b.endHash)

                    val document = q.get().await().toObjects<PharmacyDto>()
                    list.addAll(document)
                }

                Result.success(list)
            } catch (e: FirebaseFirestoreException) {
                Result.failure(e)
            }
        }
    }