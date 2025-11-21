package com.demo.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "recetas")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la receta es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El tipo de cocina es obligatorio")
    @Size(max = 50, message = "El tipo de cocina no puede exceder 50 caracteres")
    private String tipoCocina;

    @NotBlank(message = "Los ingredientes son obligatorios")
    @Size(min = 10, max = 1000, message = "Los ingredientes deben tener entre 10 y 1000 caracteres")
    @Column(length = 1000)
    private String ingredientes;

    @NotBlank(message = "El país de origen es obligatorio")
    @Size(max = 50, message = "El país de origen no puede exceder 50 caracteres")
    private String paisOrigen;

    @NotBlank(message = "La dificultad es obligatoria")
    private String dificultad;

    @NotNull(message = "El tiempo de cocción es obligatorio")
    @Min(value = 1, message = "El tiempo de cocción debe ser al menos 1 minuto")
    private Integer tiempoCoccion;

    @NotBlank(message = "Las instrucciones son obligatorias")
    @Size(min = 20, max = 5000, message = "Las instrucciones deben tener entre 20 y 5000 caracteres")
    @Column(length = 5000)
    private String instrucciones;

    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    private String imagenUrl;

    private boolean popular;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTipoCocina() {
        return tipoCocina;
    }
    public void setTipoCocina(String tipoCocina) {
        this.tipoCocina = tipoCocina;
    }
    public String getIngredientes() {
        return ingredientes;
    }
    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }
    public String getPaisOrigen() {
        return paisOrigen;
    }
    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }
    public String getDificultad() {
        return dificultad;
    }
    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }
    public Integer getTiempoCoccion() {
        return tiempoCoccion;
    }
    public void setTiempoCoccion(Integer tiempoCoccion) {
        this.tiempoCoccion = tiempoCoccion;
    }
    public String getInstrucciones() {
        return instrucciones;
    }
    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }
    public String getImagenUrl() {
        return imagenUrl;
    }
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    public boolean isPopular() {
        return popular;
    }
    public void setPopular(boolean popular) {
        this.popular = popular;
    }
    
}