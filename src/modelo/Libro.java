package modelo;

import java.time.LocalDate;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private int anioPublicacion;
    private String categoria;
    private boolean disponible;
    private String prestatario;
    private LocalDate fechaPrestamo;

    public Libro(String isbn, String titulo, String autor, String editorial,
                 int anioPublicacion, String categoria) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.disponible = true;
        this.prestatario = null;
        this.fechaPrestamo = null;
    }

    // Getters
    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public String getCategoria() { return categoria; }
    public boolean isDisponible() { return disponible; }
    public String getPrestatario() { return prestatario; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }

    // Setters
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public void setPrestatario(String prestatario) { this.prestatario = prestatario; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    @Override
    public String toString() {
        String estado = disponible ? "Disponible" : "Prestado a: " + prestatario + " (desde " + fechaPrestamo + ")";
        return String.format(
            "┌─────────────────────────────────────────────────┐\n" +
            "│ ISBN       : %-35s│\n" +
            "│ Título     : %-35s│\n" +
            "│ Autor      : %-35s│\n" +
            "│ Editorial  : %-35s│\n" +
            "│ Año        : %-35s│\n" +
            "│ Categoría  : %-35s│\n" +
            "│ Estado     : %-35s│\n" +
            "└─────────────────────────────────────────────────┘",
            isbn, titulo.length() > 35 ? titulo.substring(0, 32) + "..." : titulo,
            autor.length() > 35 ? autor.substring(0, 32) + "..." : autor,
            editorial, anioPublicacion, categoria,
            estado.length() > 35 ? estado.substring(0, 32) + "..." : estado
        );
    }
}
