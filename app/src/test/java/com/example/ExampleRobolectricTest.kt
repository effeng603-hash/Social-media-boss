package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.GameRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SOCIAL MEDIA BOSS", appName)
  }

  @Test
  fun `game repository initialization and level calculation`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = GameRepository(context)

    assertEquals("Boss Player", repository.playerState.value.name)
    assertEquals(1, repository.careerLevels.first().levelNumber)
    assertEquals(6, repository.missions.size)
    assertEquals(6, repository.businesses.value.size)

    val beginner = repository.calculateLevel(100)
    assertEquals("Beginner", beginner.title)

    val strategist = repository.calculateLevel(1500)
    assertEquals("Social Media Strategist", strategist.title)
  }

  @Test
  fun `caption analyzer evaluates hooks and ctas`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = GameRepository(context)

    val result = repository.analyzeCaptionLocally(
      "Why do 90% of brands fail at organic video? Here are 3 rules we use to generate 100k views.\n\nSave this for your next post! #marketing #growth"
    )
    assertTrue(result.totalScore > 50)
    assertTrue(result.hookScore > 10)
    assertNotNull(result.suggestedHook)
  }
}
