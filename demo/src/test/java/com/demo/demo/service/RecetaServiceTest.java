package com.demo.demo.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.demo.models.Receta;
import com.demo.demo.repository.RecetaRepository;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class RecetaServiceTest {

    @Mock
    private RecetaRepository recetaRepository;

    @InjectMocks
    private RecetaService recetaService;

    private final Receta receta1 = crearReceta(1L, "Pasta Carbonara", "Italiana", true);
    private final Receta receta2 = crearReceta(2L, "Tacos", "Mexicana", false);

    private static Receta crearReceta(Long id, String nombre, String tipoCocina, boolean popular) {
        Receta receta = new Receta();
        receta.setId(id);
        receta.setNombre(nombre);
        receta.setTipoCocina(tipoCocina);
        receta.setPopular(popular);
        return receta;
    }

    @Test
    void testListarTodas() {
        // Arrange
        when(recetaRepository.findAll()).thenReturn(Arrays.asList(receta1, receta2));

        // Act
        List<Receta> result = recetaService.listarTodas();

        // Assert
        assertEquals(2, result.size());
        verify(recetaRepository, times(1)).findAll();
    }

    @Test
    void testListarPopulares() {
        // Arrange
        when(recetaRepository.findByPopularTrue()).thenReturn(Arrays.asList(receta1));

        // Act
        List<Receta> result = recetaService.listarPopulares();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).isPopular());
        verify(recetaRepository, times(1)).findByPopularTrue();
    }

    @Test
    void testObtenerPorId_Existe() {
        // Arrange
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta1));

        // Act
        Optional<Receta> result = recetaService.obtenerPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Pasta Carbonara", result.get().getNombre());
        verify(recetaRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // Arrange
        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Receta> result = recetaService.obtenerPorId(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(recetaRepository, times(1)).findById(999L);
    }

    @Test
    void testGuardar() {
        // Arrange
        when(recetaRepository.save(receta1)).thenReturn(receta1);

        // Act
        Receta result = recetaService.guardar(receta1);

        // Assert
        assertNotNull(result);
        assertEquals("Pasta Carbonara", result.getNombre());
        verify(recetaRepository, times(1)).save(receta1);
    }

    @Test
    void testBuscarPorNombre() {
        // Arrange
        when(recetaRepository.findByNombreContainingIgnoreCase("Pasta"))
                .thenReturn(Arrays.asList(receta1));

        // Act
        List<Receta> result = recetaService.buscar("Pasta", null, null, null, null, null);

        // Assert
        assertEquals(1, result.size());
        String nombre = result.get(0).getNombre();
        assertEquals("Pasta Carbonara", nombre);
        verify(recetaRepository, times(1)).findByNombreContainingIgnoreCase("Pasta");
    }

    @Test
    void testBuscarPopulares() {
        // Arrange
        when(recetaRepository.findByPopularTrue()).thenReturn(Arrays.asList(receta1));

        // Act
        List<Receta> result = recetaService.buscar(null, null, null, null, null, true);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).isPopular());
        verify(recetaRepository, times(1)).findByPopularTrue();
    }
}
