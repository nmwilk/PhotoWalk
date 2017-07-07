package com.nmwilkinson.photowalk

import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class TrackingShould {
    @Test
    fun start_up_inactive() {
        val (_, view) = createMvp(ActiveModel(false, listOf()))

        verify(view, times(1)).updateUI(ActiveContract.UIState(false, listOf()))
    }

    @Test
    fun respond_to_start() {
        val (presenter, view) = createMvp(ActiveModel(false, listOf()))

        presenter.start()

        verify(view, times(1)).startLocationTracking()
        verify(view, times(1)).updateUI(ActiveContract.UIState(true, listOf()))
    }

    @Test
    fun respond_to_stop() {
        val (presenter, view) = createMvp(ActiveModel(true, listOf("A")))

        presenter.stop()

        verify(view, times(1)).stopLocationTracking()
        verify(view, times(1)).updateUI(ActiveContract.UIState(false, listOf("A")))
    }

    @Test
    fun start_up_active() {
        val (_, view) = createMvp(ActiveModel(true, listOf("A")))

        verify(view, times(1)).startLocationTracking()
        verify(view, times(1)).updateUI(ActiveContract.UIState(true, listOf("A")))
    }

    @Test
    fun receive_new_image() {
        val (presenter, view) = createMvp(ActiveModel(true, listOf("A")))

        presenter.newImage("B")

        verify(view, times(2)).updateUI(ActiveContract.UIState(true, listOf("A", "B")))
    }

    private fun createMvp(activeModel: ActiveModel): Pair<ActiveContract.Presenter, ActiveContract.View> {
        val presenter = ActivePresenter(activeModel)
        val view = Mockito.mock(ActiveContract.View::class.java)

        presenter.setView(view)
        return Pair(presenter, view)
    }
}