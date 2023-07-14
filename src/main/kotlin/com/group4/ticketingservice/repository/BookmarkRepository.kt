package com.group4.ticketingservice.repository

import com.group4.ticketingservice.model.Bookmark
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookmarkRepository : JpaRepository<Bookmark, Long>