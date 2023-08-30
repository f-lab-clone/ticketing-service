package com.group4.ticketingservice.bookmark

import com.group4.ticketingservice.config.ClockConfig
import com.group4.ticketingservice.dto.BookmarkFromdto
import com.group4.ticketingservice.entity.Bookmark
import com.group4.ticketingservice.entity.Event
import com.group4.ticketingservice.entity.User
import com.group4.ticketingservice.repository.BookmarkRepository
import com.group4.ticketingservice.repository.EventRepository
import com.group4.ticketingservice.repository.UserRepository
import com.group4.ticketingservice.service.BookmarkService
import com.group4.ticketingservice.utils.Authority
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import java.time.Clock
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*

@ContextConfiguration(
    classes = [ClockConfig::class],
    loader = AnnotationConfigContextLoader::class
)
@SpringBootTest
class BookmarkServiceTest(
    @Autowired private val clock: Clock
) {
    private val userRepository: UserRepository = mockk()
    private val eventRepository: EventRepository = mockk()
    private val repository: BookmarkRepository = mockk()
    private val modelMapper: ModelMapper = ModelMapper()
    private val bookmarkService: BookmarkService = BookmarkService(userRepository, eventRepository, repository, modelMapper, clock)

    val sampleUser = User(
        name = "james",
        email = "james@example.com",
        password = "12345678",
        authority = Authority.USER
    )

    private val sampleEvent: Event = Event(
        id = 1,
        title = "test title",
        date = OffsetDateTime.now(clock),
        reservationEndTime = OffsetDateTime.now(clock) + Duration.ofHours(2),
        reservationStartTime = OffsetDateTime.now(clock) + Duration.ofHours(1),
        maxAttendees = 10
    )

    private val sampleBookmark = Bookmark(
        user = sampleUser,
        event = sampleEvent
    )

    private val sampleBookmarkDto = BookmarkFromdto(
        show_id = 1
    )

    @Test
    fun `bookmarkService_getList() invoke repository_findByUser`() {
        // given
        every { userRepository.findByEmail(sampleUser.username) } returns sampleUser
        every { repository.findByUser(sampleUser) } returns listOf(sampleBookmark)

        // when
        bookmarkService.getList(sampleUser.username)

        // then
        verify(exactly = 1) { repository.findByUser(sampleUser) }
    }

    @Test
    fun `bookmarkService_getList() should return emptyList`() {
        // given
        every { userRepository.findByEmail(sampleUser.username) } returns sampleUser
        every { repository.findByUser(sampleUser) } returns listOf()

        // when
        val result: List<Bookmark> = bookmarkService.getList(sampleUser.username)

        // then
        verify(exactly = 1) { repository.findByUser(sampleUser) }
        assert(result == listOf<Bookmark>())
    }

    @Test
    fun `bookmarkService_get() invoke repository_findByIdAndUser`() {
        // given
        every { userRepository.findByEmail(sampleUser.username) } returns sampleUser
        every { repository.findByIdAndUser(1, sampleUser) } returns sampleBookmark

        // when
        val result: Bookmark? = bookmarkService.get(sampleUser.username, 1)

        // then
        verify(exactly = 1) { repository.findByIdAndUser(1, sampleUser) }
        assert(result == sampleBookmark)
    }

    @Test
    fun `bookmarkService_create() invoke repository_save`() {
        // given
        every { userRepository.findByEmail(any()) } returns sampleUser
        every { eventRepository.findById(any()) } returns Optional.of(sampleEvent)
        every { repository.save(any()) } returns sampleBookmark

        // when
        bookmarkService.create(sampleUser.name, sampleBookmarkDto)

        // then
        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `bookmarkService_delete() invoke repository_deleteByIdAndUser`() {
        // given
        every { userRepository.findByEmail(sampleUser.username) } returns sampleUser
        every { repository.deleteByIdAndUser(1, sampleUser) } returns Unit

        // when
        bookmarkService.delete(sampleUser.username, 1)

        // then
        verify(exactly = 1) { repository.deleteByIdAndUser(1, sampleUser) }
    }
}
