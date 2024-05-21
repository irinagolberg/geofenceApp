package com.test.employeepresence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.ar.core.Config
import com.test.employeepresence.places.data.db.HoursDao
import com.test.employeepresence.places.data.db.HoursDatabase
import com.test.employeepresence.places.data.db.HoursEntity
import com.test.employeepresence.places.domain.WorkingPlace
import com.test.employeepresence.utils.DEFAULT_LOCATION
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class WorkingHoursDaoTest {
    companion object {
        val defaultWorkingPlace = WorkingPlace(
        latitude = DEFAULT_LOCATION.latitude,
        longitude = DEFAULT_LOCATION.longitude,
        address = "Tel Aviv"
        )
    }
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: HoursDatabase

    lateinit var dao: HoursDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.hoursDao()

    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun checkPlaceKey() {
        assertEquals(
            WorkingPlace.parseWorkingPlace(defaultWorkingPlace.toPlaceIdKey()),
            defaultWorkingPlace
        )
    }

    @Test
    fun insertWorkingPlace() = runBlocking {
        val timestamp = Date().time
        val hoursEntity = HoursEntity(timestamp = timestamp, placeId = defaultWorkingPlace.toPlaceIdKey(), inside = true)
        dao.insertHourRecord(hoursEntity)

        val hoursRecords = dao.getHours(defaultWorkingPlace.toPlaceIdKey())
        assertNotNull(hoursRecords.filter { it.timestamp == timestamp })
    }
}