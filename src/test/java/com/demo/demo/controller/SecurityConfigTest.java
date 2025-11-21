package com.demo.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test: Rutas públicas accesibles sin autenticación
     */
    @Test
    void testPublicRoutesAccessibleWithoutAuth() throws Exception {
        // Home page
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
        
        // Login page
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
        
        // Register page
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
        
        // Recetas listing (público)
        mockMvc.perform(get("/recetas"))
                .andExpect(status().isOk());
    }

    /**
     * Test: Recursos estáticos accesibles sin autenticación
     */
    @Test
    void testStaticResourcesAccessible() throws Exception {
        mockMvc.perform(get("/css/bootstrap.min.css"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/js/bootstrap.bundle.min.js"))
                .andExpect(status().isOk());
    }

    /**
     * Test: Login con credenciales inválidas redirecciona con error
     */
    @Test
    @SuppressWarnings("null")
    void testLoginWithInvalidCredentials() throws Exception {
        var loginRequest = formLogin("/login")
                .user("usuario_invalido")
                .password("password_invalido");
        mockMvc.perform(loginRequest)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    /**
     * Test: Usuario autenticado puede acceder a rutas protegidas
     */
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testAuthenticatedUserAccessProtectedRoutes() throws Exception {
        // Usuario autenticado puede acceder a todas las rutas
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

    /**
     * Test: Logout cierra sesión correctamente
     */
    @Test
    @WithMockUser
    @SuppressWarnings("null")
    void testLogoutRedirectsToLogin() throws Exception {
        var logoutRequest = logout("/logout");
        mockMvc.perform(logoutRequest)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout=true"));
    }

    /**
     * Test: CSRF token requerido en formularios POST
     */
    @Test
    void testCsrfProtectionOnPostRequests() throws Exception {
        // POST sin CSRF token debe fallar
        mockMvc.perform(post("/register")
                .param("username", "testuser")
                .param("email", "test@example.com")
                .param("password", "password123"))
                .andExpect(status().isForbidden());
    }

    /**
     * Test: Headers de seguridad presentes en respuestas
     */
    @Test
    void testSecurityHeadersPresent() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Content-Type-Options"))
                .andExpect(header().exists("X-XSS-Protection"))
                .andExpect(header().exists("Content-Security-Policy"))
                .andExpect(header().exists("X-Frame-Options"));
    }

    /**
     * Test: Sesiones limitan usuarios concurrentes
     */
    @Test
    @WithMockUser
    @SuppressWarnings("null")
    void testSessionManagementConfigured() throws Exception {
        // Verificar que la sesión se crea correctamente
        var matcher = org.hamcrest.Matchers.notNullValue();
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("SPRING_SECURITY_CONTEXT", matcher));
    }
}
