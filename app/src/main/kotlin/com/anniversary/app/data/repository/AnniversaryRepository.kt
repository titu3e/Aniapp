package com.anniversary.app.data.repository

import com.anniversary.app.data.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class AnniversaryRepository @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth,
    private val messaging: FirebaseMessaging
) {

    private val anniversariesRef = database.getReference("anniversaries")
    private val usersRef = database.getReference("users")
    private val wishesRef = database.getReference("wishes")
    private val deliveryStatusRef = database.getReference("delivery_status")

    suspend fun createAnniversary(anniversary: Anniversary): Result<Anniversary> {
        return try {
            val id = anniversariesRef.push().key ?: throw Exception("Failed to generate ID")
            val newAnniversary = anniversary.copy(
                id = id,
                coupleCode = generateCoupleCode()
            )
            anniversariesRef.child(id).setValue(newAnniversary).await()
            Result.success(newAnniversary)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun findAnniversaryByCode(code: String): Result<Anniversary?> {
        return try {
            val snapshot = anniversariesRef
                .orderByChild("coupleCode")
                .equalTo(code)
                .limitToFirst(1)
                .get()
                .await()
            
            val anniversary = snapshot.children.firstOrNull()?.getValue(Anniversary::class.java)
            Result.success(anniversary)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUser(user: UserProfile): Result<UserProfile> {
        return try {
            val id = usersRef.push().key ?: throw Exception("Failed to generate ID")
            val deviceToken = messaging.token.await()
            val newUser = user.copy(id = id, deviceToken = deviceToken)
            usersRef.child(id).setValue(newUser).await()
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveWishMessage(wish: WishMessage): Result<WishMessage> {
        return try {
            val id = wishesRef.push().key ?: throw Exception("Failed to generate ID")
            val newWish = wish.copy(id = id)
            wishesRef.child(id).setValue(newWish).await()
            Result.success(newWish)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWishesForAnniversary(anniversaryId: String): Result<List<WishMessage>> {
        return try {
            val snapshot = wishesRef
                .orderByChild("anniversaryId")
                .equalTo(anniversaryId)
                .get()
                .await()
            
            val wishes = snapshot.children.mapNotNull { 
                it.getValue(WishMessage::class.java) 
            }.sortedBy { it.monthNumber }
            
            Result.success(wishes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun observeWishesForAnniversary(anniversaryId: String): Flow<List<WishMessage>> {
        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val wishes = snapshot.children.mapNotNull { 
                        it.getValue(WishMessage::class.java) 
                    }.filter { it.anniversaryId == anniversaryId }
                     .sortedBy { it.monthNumber }
                    
                    trySend(wishes)
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            
            wishesRef.addValueEventListener(listener)
            awaitClose { wishesRef.removeEventListener(listener) }
        }
    }

    suspend fun updateWishDeliveryStatus(wishId: String, isDelivered: Boolean) {
        try {
            val updates = mapOf(
                "isDelivered" to isDelivered,
                "deliveredAt" to if (isDelivered) System.currentTimeMillis() else null
            )
            wishesRef.child(wishId).updateChildren(updates).await()
        } catch (e: Exception) {
            // Handle error silently or log
        }
    }

    suspend fun saveDeliveryStatus(status: DeliveryStatus): Result<Unit> {
        return try {
            deliveryStatusRef.child(status.wishId).setValue(status).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateCoupleCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6).map { chars.random() }.joinToString("")
    }

    suspend fun linkGirlToAnniversary(anniversaryId: String, girlId: String): Result<Unit> {
        return try {
            anniversariesRef.child(anniversaryId).child("girlId").setValue(girlId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            usersRef.child(userId).updateChildren(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}