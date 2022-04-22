package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.fitness.result.DataReadResult
import com.google.android.gms.tasks.Tasks
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private val objective: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

        val googleSignInAccount = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        accessGoogleFit(fitnessOptions) {
            val dataSet = it.getDataSet(DataType.TYPE_STEP_COUNT_DELTA)
        }
    }
    @SuppressLint("NewApi")
    private fun accessGoogleFit(fitnessOptions: FitnessOptions,onFetched:(response:DataReadResponse) -> Unit) {
        val end = LocalDateTime.now()
        val start = end.minusYears(1)
        val endSeconds = end.atZone(ZoneId.systemDefault()).toEpochSecond()
        val startSeconds = start.atZone(ZoneId.systemDefault()).toEpochSecond()

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startSeconds, endSeconds, TimeUnit.SECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        Fitness.getHistoryClient(this, account)
            .readData(readRequest)
            .addOnSuccessListener({ response ->
                // Use response data here
                Log.i(TAG, "OnSuccess()")
                onFetched(response)
            })
            .addOnFailureListener({ e -> Log.d(TAG, "OnFailure()", e) })
    }
    function onSignIn(googleUser) {
        val googleUser
        var profile = googleUser.getBasicProfile();
        console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
        console.log('Name: ' + profile.getName());
        console.log('Image URL: ' + profile.getImageUrl());
        console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
    }
    private fun readGoals() {
        val goalsReadRequest
        Fitness.getGoalsClient(requireContext(), getGoogleAccount())
            .readCurrentGoals(goalsReadRequest)
            .addOnSuccessListener { goals ->
                // There should be at most one heart points goal currently.
                goals.firstOrNull()?.apply {
                    // What is the value of the goal
                    val goalValue = metricObjective.value
                    Log.i(TAG, "Goal value: $goalValue")
                    val objective
                    // How is the goal measured?
                    Log.i(TAG, "Objective: $objective")

                    // How often does the goal repeat?
                    val recurrenceDetails
                    Log.i(TAG, "Recurrence: $recurrenceDetails")
                }
            }
    }
   




