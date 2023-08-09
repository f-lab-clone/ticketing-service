package com.group4.ticketingservice.controller

import com.group4.ticketingservice.dto.ReservationCreateRequest
import com.group4.ticketingservice.dto.ReservationResponse
import com.group4.ticketingservice.dto.ReservationUpdateRequest
import com.group4.ticketingservice.entity.Reservation
import com.group4.ticketingservice.service.ReservationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reservations")
class ReservationController(val reservationService: ReservationService) {
    @PostMapping
    fun createReservation(@RequestBody request: ReservationCreateRequest): ResponseEntity<ReservationResponse> {
        val reservation: Reservation = reservationService.createReservation(
            request.eventId,
            request.userId
        )
        val response = ReservationResponse(
            id = reservation.id!!,
            eventId = reservation.event.id!!,
            userId = reservation.user.id!!,
            bookedAt = reservation.bookedAt
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getReservation(@PathVariable id: Long): ResponseEntity<ReservationResponse> {
        val reservation = reservationService.getReservation(id)
        val response = ReservationResponse(
            id = reservation.id!!,
            eventId = reservation.event.id!!,
            userId = reservation.user.id!!,
            bookedAt = reservation.bookedAt
        )
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateReservation(
        @PathVariable id: Long,
        @RequestBody request: ReservationUpdateRequest
    ): ResponseEntity<ReservationResponse> {
        val reservation = reservationService.updateReservation(id, request.eventId)
        val response = ReservationResponse(
            id = reservation.id!!,
            eventId = reservation.event.id!!,
            userId = reservation.user.id!!,
            bookedAt = reservation.bookedAt
        )
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteReservation(@PathVariable id: Long): ResponseEntity<Void> {
        reservationService.deleteReservation(id)
        return ResponseEntity.noContent().build()
    }
}