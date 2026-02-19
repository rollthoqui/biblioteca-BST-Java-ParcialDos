package estructura;

import modelo.Libro;
import modelo.NodoBST;

import java.util.ArrayList;
import java.util.List;

public class ArbolBST {

    private NodoBST raiz;

    public ArbolBST() {
        this.raiz = null;
    }

    // ─────────────────────────────────────────────
    // 1. INSERTAR
    // ─────────────────────────────────────────────
    public void insertar(Libro libro) {
        if (libro == null || libro.getAutor() == null || libro.getAutor().isBlank()) {
            throw new IllegalArgumentException("El libro o el autor no pueden ser nulos/vacíos.");
        }
        raiz = insertarRec(raiz, libro);
    }

    private NodoBST insertarRec(NodoBST nodo, Libro libro) {
        if (nodo == null) return new NodoBST(libro);

        int cmp = libro.getAutor().compareToIgnoreCase(nodo.libro.getAutor());
        if (cmp < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, libro);
        } else if (cmp > 0) {
            nodo.derecho = insertarRec(nodo.derecho, libro);
        } else {
            throw new IllegalStateException(
                "Ya existe un libro del autor \"" + libro.getAutor() + "\" en el catálogo.");
        }
        return nodo;
    }

    // ─────────────────────────────────────────────
    // 2. BUSCAR POR AUTOR
    // ─────────────────────────────────────────────
    public Libro buscar(String autor) {
        NodoBST resultado = buscarRec(raiz, autor.trim());
        if (resultado == null) {
            throw new IllegalArgumentException(
                "No se encontró ningún libro del autor \"" + autor + "\".");
        }
        return resultado.libro;
    }

    private NodoBST buscarRec(NodoBST nodo, String autor) {
        if (nodo == null) return null;
        int cmp = autor.compareToIgnoreCase(nodo.libro.getAutor());
        if (cmp == 0) return nodo;
        return cmp < 0 ? buscarRec(nodo.izquierdo, autor) : buscarRec(nodo.derecho, autor);
    }

    // ─────────────────────────────────────────────
    // 3. ELIMINAR
    // ─────────────────────────────────────────────
    public void eliminar(String autor) {
        if (buscarRec(raiz, autor.trim()) == null) {
            throw new IllegalArgumentException(
                "No se encontró ningún libro del autor \"" + autor + "\" para eliminar.");
        }
        raiz = eliminarRec(raiz, autor.trim());
    }

    private NodoBST eliminarRec(NodoBST nodo, String autor) {
        if (nodo == null) return null;

        int cmp = autor.compareToIgnoreCase(nodo.libro.getAutor());

        if (cmp < 0) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, autor);
        } else if (cmp > 0) {
            nodo.derecho = eliminarRec(nodo.derecho, autor);
        } else {
            // Caso 1: sin hijos
            if (nodo.izquierdo == null && nodo.derecho == null) return null;
            // Caso 2: un hijo
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;
            // Caso 3: dos hijos → sucesor in-order (mínimo del subárbol derecho)
            NodoBST sucesor = encontrarMinimoNodo(nodo.derecho);
            nodo.libro = sucesor.libro;
            nodo.derecho = eliminarRec(nodo.derecho, sucesor.libro.getAutor());
        }
        return nodo;
    }

    // ─────────────────────────────────────────────
    // 4. RECORRIDOS
    // ─────────────────────────────────────────────
    public List<Libro> recorridoInOrden() {
        List<Libro> lista = new ArrayList<>();
        inOrdenRec(raiz, lista);
        return lista;
    }

    private void inOrdenRec(NodoBST nodo, List<Libro> lista) {
        if (nodo == null) return;
        inOrdenRec(nodo.izquierdo, lista);
        lista.add(nodo.libro);
        inOrdenRec(nodo.derecho, lista);
    }

    public List<Libro> recorridoPreOrden() {
        List<Libro> lista = new ArrayList<>();
        preOrdenRec(raiz, lista);
        return lista;
    }

    private void preOrdenRec(NodoBST nodo, List<Libro> lista) {
        if (nodo == null) return;
        lista.add(nodo.libro);
        preOrdenRec(nodo.izquierdo, lista);
        preOrdenRec(nodo.derecho, lista);
    }

    public List<Libro> recorridoPostOrden() {
        List<Libro> lista = new ArrayList<>();
        postOrdenRec(raiz, lista);
        return lista;
    }

    private void postOrdenRec(NodoBST nodo, List<Libro> lista) {
        if (nodo == null) return;
        postOrdenRec(nodo.izquierdo, lista);
        postOrdenRec(nodo.derecho, lista);
        lista.add(nodo.libro);
    }

    // ─────────────────────────────────────────────
    // 5. MÍNIMO Y MÁXIMO
    // ─────────────────────────────────────────────
    public Libro encontrarMinimo() {
        if (raiz == null) throw new IllegalStateException("El catálogo está vacío.");
        return encontrarMinimoNodo(raiz).libro;
    }

    private NodoBST encontrarMinimoNodo(NodoBST nodo) {
        while (nodo.izquierdo != null) nodo = nodo.izquierdo;
        return nodo;
    }

    public Libro encontrarMaximo() {
        if (raiz == null) throw new IllegalStateException("El catálogo está vacío.");
        NodoBST nodo = raiz;
        while (nodo.derecho != null) nodo = nodo.derecho;
        return nodo.libro;
    }

    // ─────────────────────────────────────────────
    // 6. CONTAR NODOS Y ALTURA
    // ─────────────────────────────────────────────
    public int contarNodos() {
        return contarRec(raiz);
    }

    private int contarRec(NodoBST nodo) {
        if (nodo == null) return 0;
        return 1 + contarRec(nodo.izquierdo) + contarRec(nodo.derecho);
    }

    public int altura() {
        return alturaRec(raiz);
    }

    private int alturaRec(NodoBST nodo) {
        if (nodo == null) return 0;
        return 1 + Math.max(alturaRec(nodo.izquierdo), alturaRec(nodo.derecho));
    }

    // ─────────────────────────────────────────────
    // 7. BÚSQUEDA POR ISBN (recorrido completo)
    // ─────────────────────────────────────────────
    public Libro buscarPorIsbn(String isbn) {
        return buscarIsbnRec(raiz, isbn.trim());
    }

    private Libro buscarIsbnRec(NodoBST nodo, String isbn) {
        if (nodo == null) return null;
        if (nodo.libro.getIsbn().equalsIgnoreCase(isbn)) return nodo.libro;
        Libro izq = buscarIsbnRec(nodo.izquierdo, isbn);
        return izq != null ? izq : buscarIsbnRec(nodo.derecho, isbn);
    }

    // ─────────────────────────────────────────────
    // 8. BÚSQUEDA POR CATEGORÍA
    // ─────────────────────────────────────────────
    public List<Libro> buscarPorCategoria(String categoria) {
        List<Libro> lista = new ArrayList<>();
        buscarCatRec(raiz, categoria.trim().toLowerCase(), lista);
        return lista;
    }

    private void buscarCatRec(NodoBST nodo, String categoria, List<Libro> lista) {
        if (nodo == null) return;
        if (nodo.libro.getCategoria().toLowerCase().contains(categoria))
            lista.add(nodo.libro);
        buscarCatRec(nodo.izquierdo, categoria, lista);
        buscarCatRec(nodo.derecho, categoria, lista);
    }

    // ─────────────────────────────────────────────
    // 9. LISTAR DISPONIBLES Y PRESTADOS
    // ─────────────────────────────────────────────
    public List<Libro> listarDisponibles() {
        List<Libro> todos = recorridoInOrden();
        List<Libro> disponibles = new ArrayList<>();
        for (Libro l : todos) if (l.isDisponible()) disponibles.add(l);
        return disponibles;
    }

    public List<Libro> listarPrestados() {
        List<Libro> todos = recorridoInOrden();
        List<Libro> prestados = new ArrayList<>();
        for (Libro l : todos) if (!l.isDisponible()) prestados.add(l);
        return prestados;
    }

    public boolean estaVacio() {
        return raiz == null;
    }
}
