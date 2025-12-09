import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 *
 * @author Ramiro Padierna Delgado 
 * @author 1224100710.rpd@gmail.com
 * 
 * Esta aplicación permite:
 *   Insertar nodos
 *   Eliminar nodos
 *   Buscar un valor dentro del árbol
 *   Mostrar recorridos InOrden / PreOrden / PostOrden
 *   Dibujar gráficamente el ABB y actualizarlo en tiempo real.
 *
 * La interfaz fue desarrollada con Java Swing, y el árbol se
 * renderiza en un JPanel personalizado.
 * 
 */

public class ArbolBinarioApp extends JFrame {

    // --------------- NODO DEL ÁRBOL -----------------
    class Nodo {
        int valor;
        Nodo izq, der;

        public Nodo(int valor) {
            this.valor = valor;
        }
    }

    // RAÍZ DEL ÁRBOL
    private Nodo raiz = null;

    // PANEL DE DIBUJO
    private LienzoArbol lienzo;

    // CAMPOS UI
    private JTextField txtValor;
    private JTextArea txtRecorridos;

    // ========== CONSTRUCTOR ==========
    public ArbolBinarioApp() {
        super("Visualizador de Arbol Binario de Búsqueda (ABB)");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.add(new JLabel("Valor:"));
        txtValor = new JTextField(5);
        panelSuperior.add(txtValor);

        JButton btnInsertar = new JButton("+ Insertar");
        JButton btnEliminar = new JButton("- Eliminar");
        JButton btnBuscar = new JButton("🔍 Buscar");
        JButton btnLimpiar = new JButton("🗑 Limpiar");

        panelSuperior.add(btnInsertar);
        panelSuperior.add(btnEliminar);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnLimpiar);

        // Botones de recorridos
        JButton btnInOrden = new JButton("Recorrido Inorden");
        JButton btnPreOrden = new JButton("Recorrido Preorden");
        JButton btnPostOrden = new JButton("Recorrido Postorden");

        panelSuperior.add(new JLabel("Recorridos:"));
        panelSuperior.add(btnInOrden);
        panelSuperior.add(btnPreOrden);
        panelSuperior.add(btnPostOrden);

        add(panelSuperior, BorderLayout.NORTH);

        // PANEL DE DIBUJO
        lienzo = new LienzoArbol();
        add(lienzo, BorderLayout.CENTER);

        // PANEL DE RECORRIDOS
        txtRecorridos = new JTextArea(3, 20);
        txtRecorridos.setEditable(false);
        add(new JScrollPane(txtRecorridos), BorderLayout.SOUTH);

        // ====== EVENTOS ======

        btnInsertar.addActionListener(e -> {
            agregarNodo();
            lienzo.repaint();
        });

        btnEliminar.addActionListener(e -> {
            eliminarNodo();
            lienzo.repaint();
        });

        btnBuscar.addActionListener(e -> {
            int valor = Integer.parseInt(txtValor.getText());
            lienzo.buscarValor(valor);
        });

        btnLimpiar.addActionListener(e -> {
            raiz = null;
            txtRecorridos.setText("");
            lienzo.setRaiz(null);
            lienzo.repaint();
        });

        btnInOrden.addActionListener(e -> {
            txtRecorridos.setText("InOrden: " + recorridoInOrden(raiz));
        });

        btnPreOrden.addActionListener(e -> {
            txtRecorridos.setText("PreOrden: " + recorridoPreOrden(raiz));
        });

        btnPostOrden.addActionListener(e -> {
            txtRecorridos.setText("PostOrden: " + recorridoPostOrden(raiz));
        });

        setVisible(true);
    }

    // ------------ OPERACIONES DEL ÁRBOL -------------------

    private void agregarNodo() {
        int valor = Integer.parseInt(txtValor.getText());
        raiz = insertar(raiz, valor);
        lienzo.setRaiz(raiz);
    }

    private Nodo insertar(Nodo nodo, int valor) {
        if (nodo == null) return new Nodo(valor);
        if (valor < nodo.valor) nodo.izq = insertar(nodo.izq, valor);
        else if (valor > nodo.valor) nodo.der = insertar(nodo.der, valor);
        return nodo;
    }

    private void eliminarNodo() {
        int valor = Integer.parseInt(txtValor.getText());
        raiz = eliminar(raiz, valor);
        lienzo.setRaiz(raiz);
    }

    private Nodo eliminar(Nodo nodo, int valor) {
        if (nodo == null) return null;

        if (valor < nodo.valor) {
            nodo.izq = eliminar(nodo.izq, valor);
        } else if (valor > nodo.valor) {
            nodo.der = eliminar(nodo.der, valor);
        } else {
            // Caso 1: sin hijos
            if (nodo.izq == null && nodo.der == null) return null;

            // Caso 2: un hijo
            if (nodo.izq == null) return nodo.der;
            if (nodo.der == null) return nodo.izq;

            // Caso 3: dos hijos → reemplazar por el menor del lado derecho
            Nodo sucesor = minimo(nodo.der);
            nodo.valor = sucesor.valor;
            nodo.der = eliminar(nodo.der, sucesor.valor);
        }
        return nodo;
    }

    private Nodo minimo(Nodo nodo) {
        while (nodo.izq != null) nodo = nodo.izq;
        return nodo;
    }

    // ------------ RECORRIDOS -------------------

    private String recorridoInOrden(Nodo nodo) {
        if (nodo == null) return "";
        return recorridoInOrden(nodo.izq) + nodo.valor + " " + recorridoInOrden(nodo.der);
    }

    private String recorridoPreOrden(Nodo nodo) {
        if (nodo == null) return "";
        return nodo.valor + " " + recorridoPreOrden(nodo.izq) + recorridoPreOrden(nodo.der);
    }

    private String recorridoPostOrden(Nodo nodo) {
        if (nodo == null) return "";
        return recorridoPostOrden(nodo.izq) + recorridoPostOrden(nodo.der) + nodo.valor + " ";
    }

    // -------------- PANEL DE DIBUJO ----------------------

    class LienzoArbol extends JPanel {
        private Nodo nodoRaiz;
        private int valorBusqueda = -1;

        public void setRaiz(Nodo raiz) {
            this.nodoRaiz = raiz;
        }

        public void buscarValor(int valor) {
            this.valorBusqueda = valor;
            repaint();
            
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.LIGHT_GRAY);

            if (nodoRaiz != null) {
                dibujarNodo(g, nodoRaiz, getWidth() / 2, 50, getWidth() / 4);
            }
        }

        private void dibujarNodo(Graphics g, Nodo nodo, int x, int y, int separacion) {
            if (nodo == null) return;

            g.setColor(Color.BLACK);

            // Conexiones
            if (nodo.izq != null)
                g.drawLine(x, y, x - separacion, y + 50);

            if (nodo.der != null)
                g.drawLine(x, y, x + separacion, y + 50);

            // Dibujo del nodo
            if (nodo.valor == valorBusqueda)
                g.setColor(Color.RED);
            else
                g.setColor(Color.BLUE);

            g.fillOval(x - 20, y - 20, 40, 40);

            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(nodo.valor), x - 6, y + 5);

            dibujarNodo(g, nodo.izq, x - separacion, y + 50, separacion / 2);
            dibujarNodo(g, nodo.der, x + separacion, y + 50, separacion / 2);
        }
    }

    // ---------- MAIN ----------
    public static void main(String[] args) {
        new ArbolBinarioApp();
    }
}
