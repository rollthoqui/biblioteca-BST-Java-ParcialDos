package servicio;

import estructura.ArbolBST;
import modelo.Libro;

import java.time.LocalDate;
import java.util.List;

public class BibliotecaService {

    private final ArbolBST catalogo;

    public BibliotecaService() {
        this.catalogo = new ArbolBST();
        cargarDatosDePrueba();
    }

    // ─────────────────────────────────────────────
    // DELEGACIÓN AL BST
    // ─────────────────────────────────────────────
    public void registrarLibro(Libro libro) {
        catalogo.insertar(libro);
    }

    public Libro buscarPorAutor(String autor) {
        return catalogo.buscar(autor);
    }

    public Libro buscarPorIsbn(String isbn) {
        Libro l = catalogo.buscarPorIsbn(isbn);
        if (l == null) throw new IllegalArgumentException(
            "No se encontró ningún libro con ISBN \"" + isbn + "\".");
        return l;
    }

    public void eliminarLibro(String autor) {
        Libro l = catalogo.buscar(autor);   // lanza excepción si no existe
        if (!l.isDisponible()) throw new IllegalStateException(
            "No se puede eliminar: el libro está prestado a " + l.getPrestatario() + ".");
        catalogo.eliminar(autor);
    }

    public List<Libro> listarInOrden()    { return catalogo.recorridoInOrden(); }
    public List<Libro> listarPreOrden()   { return catalogo.recorridoPreOrden(); }
    public List<Libro> listarPostOrden()  { return catalogo.recorridoPostOrden(); }
    public List<Libro> listarDisponibles(){ return catalogo.listarDisponibles(); }
    public List<Libro> listarPrestados()  { return catalogo.listarPrestados(); }
    public List<Libro> buscarPorCategoria(String cat){ return catalogo.buscarPorCategoria(cat); }

    // ─────────────────────────────────────────────
    // PRÉSTAMO
    // ─────────────────────────────────────────────
    public void registrarPrestamo(String autor, String nombreEstudiante) {
        if (nombreEstudiante == null || nombreEstudiante.isBlank())
            throw new IllegalArgumentException("El nombre del prestatario no puede estar vacío.");
        Libro l = catalogo.buscar(autor);
        if (!l.isDisponible())
            throw new IllegalStateException(
                "El libro ya está prestado a \"" + l.getPrestatario() + "\" desde " + l.getFechaPrestamo() + ".");
        l.setDisponible(false);
        l.setPrestatario(nombreEstudiante.trim());
        l.setFechaPrestamo(LocalDate.now());
    }

    // ─────────────────────────────────────────────
    // DEVOLUCIÓN
    // ─────────────────────────────────────────────
    public void registrarDevolucion(String autor) {
        Libro l = catalogo.buscar(autor);
        if (l.isDisponible())
            throw new IllegalStateException(
                "El libro de \"" + autor + "\" no está registrado como prestado.");
        l.setDisponible(true);
        l.setPrestatario(null);
        l.setFechaPrestamo(null);
    }

    // ─────────────────────────────────────────────
    // ESTADÍSTICAS
    // ─────────────────────────────────────────────
    public String estadisticas() {
        if (catalogo.estaVacio()) return "El catálogo está vacío.";
        int total = catalogo.contarNodos();
        int disponibles = catalogo.listarDisponibles().size();
        int prestados   = catalogo.listarPrestados().size();
        return String.format(
            "╔══════════════════════════════════════════════╗\n" +
            "║         ESTADÍSTICAS DEL CATÁLOGO            ║\n" +
            "╠══════════════════════════════════════════════╣\n" +
            "║ Total de libros       : %-20d║\n" +
            "║ Altura del árbol      : %-20d║\n" +
            "║ Primer autor (A-Z)    : %-20s║\n" +
            "║ Último autor  (A-Z)   : %-20s║\n" +
            "║ Libros disponibles    : %-20d║\n" +
            "║ Libros prestados      : %-20d║\n" +
            "╚══════════════════════════════════════════════╝",
            total, catalogo.altura(),
            abreviar(catalogo.encontrarMinimo().getAutor(), 20),
            abreviar(catalogo.encontrarMaximo().getAutor(), 20),
            disponibles, prestados
        );
    }

    private String abreviar(String s, int max) {
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }

    // ─────────────────────────────────────────────
    // DATOS DE PRUEBA
    // ─────────────────────────────────────────────
    private void cargarDatosDePrueba() {
        Libro[] libros = {
            new Libro("978-84-376-0494-7", "Cien años de soledad",     "García Márquez, Gabriel", "Sudamericana",  1967, "Literatura"),
            new Libro("978-84-206-3584-0", "Ficciones",                "Borges, Jorge Luis",      "Alianza",       1944, "Literatura"),
            new Libro("978-84-322-0556-8", "Veinte poemas de amor",    "Neruda, Pablo",           "Seix Barral",   1924, "Poesía"),
            new Libro("978-84-397-0088-5", "La casa de los espíritus", "Allende, Isabel",         "Plaza & Janés", 1982, "Literatura"),
            new Libro("978-84-204-2253-4", "Rayuela",                  "Cortázar, Julio",         "Sudamericana",  1963, "Literatura"),
            new Libro("978-84-9838-226-2", "La ciudad y los perros",   "Vargas Llosa, Mario",     "Seix Barral",   1963, "Literatura"),
            new Libro("978-956-8237-28-7", "Desolación",               "Mistral, Gabriela",       "Nascimento",    1922, "Poesía"),
            new Libro("978-84-233-1498-2", "El túnel",                 "Sábato, Ernesto",         "Sur",           1948, "Literatura"),
            new Libro("978-84-663-0550-0", "Pedro Páramo",             "Rulfo, Juan",             "FCE",           1955, "Literatura"),
            new Libro("978-84-376-2068-8", "El amor en los tiempos",   "García Lorca, Federico",  "Cátedra",       1936, "Poesía"),
        };
        for (Libro l : libros) catalogo.insertar(l);
    }
}
