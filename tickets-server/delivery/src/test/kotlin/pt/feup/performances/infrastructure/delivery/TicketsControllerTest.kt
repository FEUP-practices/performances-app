//package pt.feup.performances.infrastructure.delivery
//
//import pt.feup.performances.core.usecases.ListBou
//import pt.feup.performances.core.usecases.LogClickUseCase
//import pt.feup.performances.core.usecases.BuyTicket
//import org.junit.jupiter.api.Test
//import org.mockito.BDDMockito.given
//import org.mockito.BDDMockito.never
//import org.mockito.kotlin.verify
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.http.MediaType
//import org.springframework.test.context.ContextConfiguration
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
//import pt.feup.performances.core.*
//
//@WebMvcTest
//@ContextConfiguration(classes = [
//    TicketsControllerImpl::class,
//    RestResponseEntityExceptionHandler::class])
////class TicketsControllerTest {
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    @MockBean
//    private lateinit var buyTicket: BuyTicket
//
//    @MockBean
//    private lateinit var logClickUseCase: LogClickUseCase
//
//    @MockBean
//    private lateinit var registerNewUserUseCase: ListBou
//
//    @Test
//    fun `redirectTo returns a redirect when the key exists`() {
//        given(buyTicket.redirectTo("key")).willReturn(Redirection("http://example.com/"))
//
//        mockMvc.perform(get("/tiny-{id}", "key"))
//            .andExpect(status().isTemporaryRedirect)
//            .andExpect(redirectedUrl("http://example.com/"))
//
//        verify(logClickUseCase).logClick("key", ClickProperties(ip = "127.0.0.1"))
//    }
//
//    @Test
//    fun `redirectTo returns a not found when the key does not exist`() {
//        given(buyTicket.redirectTo("key"))
//            .willAnswer { throw RedirectionNotFound("key") }
//
//        mockMvc.perform(get("/tiny-{id}", "key"))
//            .andDo(print())
//            .andExpect(status().isNotFound)
//            .andExpect(jsonPath("$.statusCode").value(404))
//
//        verify(logClickUseCase, never()).logClick("key", ClickProperties(ip = "127.0.0.1"))
//    }
//
//    @Test
//    fun `creates returns a basic redirect if it can compute a hash`() {
//        given(registerNewUserUseCase.create(
//            url = "http://example.com/",
//            data = ShortUrlProperties(ip = "127.0.0.1")
//        )).willReturn(ShortUrl("f684a3c4", Redirection("http://example.com/")))
//
//        mockMvc.perform(post("/api/link")
//            .param("url", "http://example.com/")
//            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
//            .andDo(print())
//            .andExpect(status().isCreated)
//            .andExpect(redirectedUrl("http://localhost/tiny-f684a3c4"))
//            .andExpect(jsonPath("$.url").value("http://localhost/tiny-f684a3c4"))
//    }
//
//    @Test
//    fun `creates returns bad request if it can compute a hash`() {
//        given(registerNewUserUseCase.create(
//            url = "ftp://example.com/",
//            data = ShortUrlProperties(ip = "127.0.0.1")
//        )).willAnswer { throw InvalidUrlException("ftp://example.com/") }
//
//        mockMvc.perform(post("/api/link")
//            .param("url", "ftp://example.com/")
//            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
//            .andExpect(status().isBadRequest)
//            .andExpect(jsonPath("$.statusCode").value(400))
//    }
//}