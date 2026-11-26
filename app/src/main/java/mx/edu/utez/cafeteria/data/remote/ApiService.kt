package mx.edu.utez.cafeteria.data.remote

import mx.edu.utez.cafeteria.data.remote.dto.LoginRequestDto
import mx.edu.utez.cafeteria.data.remote.dto.LoginResponseDto
import mx.edu.utez.cafeteria.data.remote.dto.MenuDelDiaDto
import mx.edu.utez.cafeteria.data.remote.dto.PlatilloDto
import mx.edu.utez.cafeteria.data.remote.dto.TicketRequestDto
import mx.edu.utez.cafeteria.data.remote.dto.TicketResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @GET("api/platillos")
    suspend fun getPlatillos(): Response<List<PlatilloDto>>

    @POST("api/platillos")
    suspend fun createPlatillo(@Body platillo: PlatilloDto): Response<PlatilloDto>

    @PUT("api/platillos/{id}")
    suspend fun updatePlatillo(@Path("id") id: Int, @Body platillo: PlatilloDto): Response<PlatilloDto>

    @PATCH("api/platillos/{id}/desactivar")
    suspend fun desactivarPlatillo(@Path("id") id: Int): Response<Unit>

    @GET("api/menu-del-dia")
    suspend fun getMenuDelDia(): Response<List<MenuDelDiaDto>>

    @PUT("api/menu-del-dia/{id}")
    suspend fun updateMenuDelDia(@Path("id") id: Int, @Body menu: MenuDelDiaDto): Response<MenuDelDiaDto>

    @POST("api/tickets")
    suspend fun createTicket(@Body ticket: TicketRequestDto): Response<TicketResponseDto>

    @GET("api/tickets/cajero/{matricula}")
    suspend fun getTicketsByCajero(@Path("matricula") matricula: String): Response<List<TicketResponseDto>>
}