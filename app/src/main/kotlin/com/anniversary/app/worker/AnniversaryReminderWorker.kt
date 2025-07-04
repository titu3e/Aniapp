package com.anniversary.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anniversary.app.data.repository.AnniversaryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*
import java.util.concurrent.TimeUnit

@HiltWorker
class AnniversaryReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val repository: AnniversaryRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val anniversaryId = inputData.getString("anniversary_id") 
                ?: return Result.failure()
            val monthNumber = inputData.getInt("month_number", 1)

            // Get the specific wish for this month
            val wishesResult = repository.getWishesForAnniversary(anniversaryId)
            if (wishesResult.isFailure) {
                return Result.retry()
            }

            val wishes = wishesResult.getOrNull() ?: emptyList()
            val targetWish = wishes.find { it.monthNumber == monthNumber }

            if (targetWish != null && !targetWish.isDelivered) {
                // Mark as delivered
                repository.updateWishDeliveryStatus(targetWish.id, true)
                
                // Send push notification to the girl
                // Note: In a real app, you'd use Firebase Functions or your backend
                // to send the actual push notification to the girl's device
                
                // Schedule next month's reminder
                scheduleNextMonthReminder(anniversaryId, monthNumber + 1)
                
                return Result.success()
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun scheduleNextMonthReminder(anniversaryId: String, nextMonth: Int) {
        // This would typically be done through your ViewModel or Repository
        // using WorkManager.enqueueUniquePeriodicWork()
    }

    companion object {
        const val WORK_NAME = "anniversary_reminder"
        
        fun createInputData(anniversaryId: String, monthNumber: Int) = 
            androidx.work.Data.Builder()
                .putString("anniversary_id", anniversaryId)
                .putInt("month_number", monthNumber)
                .build()
    }
}