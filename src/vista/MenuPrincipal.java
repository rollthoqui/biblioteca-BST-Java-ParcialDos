package vista;

import modelo.Libro;
import servicio.BibliotecaService;

import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private static final BibliotecaService servicio = new BibliotecaService();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n  Cargando datos de prueba (10 libros)...");
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");
            System.out.println();
            procesarOpcion(opcion);
        } while (opcion != 0);
        sc.close();
    }

    // ─────────────────────────────────────────────
    // MENÚ
    // ─────────────────────────────────────────────
    private static void mostrarMenu() {
        System.out.println("""

                ============================================
                   SISTEMA DE GESTIÓN DE BIBLIOTECA (BST)
                ============================================
                  1.  Registrar nuevo libro
                  2.  Buscar libro por autor
                  3.  Buscar libro por ISBN
                  4.  Eliminar libro del catálogo
                  5.  Listar libros (InOrden - alfabético)
                  6.  Listar libros (PreOrden - estructura)
                  7.  Listar libros (PostOrden)
                  8.  Registrar préstamo de libro
                  9.  Registrar devolución de libro
                 10.  Listar libros disponibles
                 11.  Listar libros prestados
                 12.  Buscar libros por categoría
                 13.  Estadísticas del catálogo
                  0.  Salir
                ============================================""");
    }

    private static void procesarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1  -> registrarLibro();
                case 2  -> buscarPorAutor();
                case 3  -> buscarPorIsbn();
                case 4  -> eliminarLibro();
                case 5  -> listar("InOrden",   servicio.listarInOrden());
                case 6  -> listar("PreOrden",  servicio.listarPreOrden());
                case 7  -> listar("PostOrden", servicio.listarPostOrden());
                case 8  -> registrarPrestamo();
                case 9  -> registrarDevolucion();
                case 10 -> listar("Disponibles", servicio.listarDisponibles());
                case 11 -> listar("Prestados",   servicio.listarPrestados());
                case 12 -> buscarPorCategoria();
                case 13 -> System.out.println(servicio.estadisticas());
                case 0  -> System.out.println("  ¡Hasta luego!");
                default -> System.out.println("  ⚠  Opción no válida. Ingrese un número del 0 al 13.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("  ⚠  " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // ACCIONES
    // ─────────────────────────────────────────────
    private static void registrarLibro() {
        System.out.println("── Registrar nuevo libro ──");
        String isbn    = leerTexto("  ISBN           : ");
        String titulo  = leerTexto("  Título         : ");
        String autor   = leerTexto("  Autor (Apellido, Nombre): ");
        String edit    = leerTexto("  Editorial      : ");
        int    anio    = leerEntero("  Año publicación: ");
        String cat     = leerTexto("  Categoría      : ");

        Libro libro = new Libro(isbn, titulo, autor, edit, anio, cat);
        servicio.registrarLibro(libro);
        System.out.println("  ✔  Libro registrado exitosamente.");
    }

    private static void buscarPorAutor() {
        String autor = leerTexto("  Apellido (o nombre completo) del autor: ");
        System.out.println(servicio.buscarPorAutor(autor));
    }

    private static void buscarPorIsbn() {
        String isbn = leerTexto("  ISBN: ");
        System.out.println(servicio.buscarPorIsbn(isbn));
    }

    private static void eliminarLibro() {
        String autor = leerTexto("  Autor del libro a eliminar: ");
        servicio.eliminarLibro(autor);
        System.out.println("  ✔  Libro eliminado del catálogo.");
    }

    private static void listar(String tipo, List<Libro> libros) {
        if (libros.isEmpty()) {
            System.out.println("  No hay libros que mostrar en esta categoría.");
            return;
        }
        System.out.println("── Listado " + tipo + " (" + libros.size() + " libro(s)) ──");
        for (int i = 0; i < libros.size(); i++) {
            System.out.println("  [" + (i + 1) + "]");
            System.out.println(libros.get(i));
        }
    }

    private static void registrarPrestamo() {
        String autor = leerTexto("  Autor del libro: ");
        String prest = leerTexto("  Nombre del estudiante: ");
        servicio.registrarPrestamo(autor, prest);
        System.out.println("  ✔  Préstamo registrado exitosamente.");
    }

    private static void registrarDevolucion() {
        String autor = leerTexto("  Autor del libro devuelto: ");
        servicio.registrarDevolucion(autor);
        System.out.println("  ✔  Devolución registrada. Libro disponible nuevamente.");
    }

    private static void buscarPorCategoria() {
        String cat = leerTexto("  Categoría a buscar: ");
        listar("Categoría: " + cat, servicio.buscarPorCategoria(cat));
    }

    // ─────────────────────────────────────────────
    // UTILIDADES DE ENTRADA
    // ─────────────────────────────────────────────
    private static String leerTexto(String prompt) {
        String entrada;
        do {
            System.out.print(prompt);
            entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) System.out.println("  ⚠  El campo no puede estar vacío.");
        } while (entrada.isEmpty());
        return entrada;
    }

    private static int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Por favor ingrese un número entero válido.");
            }
        }
    }
}
