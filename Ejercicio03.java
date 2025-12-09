/**
 *
 * @author Ramiro Padierna Delgado 
 * @author 1224100710.rpd@gmail.com
 *
 * Esta aplicación permite al usuario manipular un conjunto (HashSet)
 * mediante una interfaz gráfica. Incluye operaciones básicas como:
 *
 *  1. Agregar elemento
 *  2. Eliminar elemento
 *  3. Buscar elemento
 *  4. Mostrar todos los elementos del conjunto
 *  5. Limpiar el conjunto
 *  6. Unión con otro conjunto
 *
 * El usuario interactúa mediante botones y un campo de texto.
 * Los resultados se muestran en un área de texto en la parte inferior.
 *
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Aplicación para demostrar el uso de Conjuntos (HashSet) en Java
 * utilizando una interfaz gráfica con Swing.
 */
public class AppConjuntos extends JFrame {

    // Conjunto principal donde se guardan los elementos
    private Set<String> conjunto = new HashSet<>();

    // Componentes gráficos
    private JTextField txtElemento;
    private JTextArea txtResultado;

    public AppConjuntos() {
        super("Aplicación con Conjuntos en Java");

        // Campo de texto para introducir elementos
        txtElemento = new JTextField(15);

        // Área de texto donde se muestran resultados
        txtResultado = new JTextArea(10, 30);
        txtResultado.setEditable(false);

        // Panel superior para entrada y botones
        JPanel panel = new JPanel();
        panel.add(new JLabel("Elemento:"));
        panel.add(txtElemento);

        // Botones de operaciones
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrar = new JButton("Mostrar Conjunto");
        JButton btnLimpiar = new JButton("Limpiar Conjunto");
        JButton btnUnion = new JButton("Unión con otro conjunto");

        // Agregar botones al panel
        panel.add(btnAgregar);
        panel.add(btnEliminar);
        panel.add(btnBuscar);
        panel.add(btnMostrar);
        panel.add(btnLimpiar);
        panel.add(btnUnion);

        // Eventos de los botones

        // 1. Agregar elemento
        btnAgregar.addActionListener(e -> {
            String elem = txtElemento.getText().trim();
            if (!elem.isEmpty()) {
                conjunto.add(elem);
                mostrar("Elemento agregado: " + elem);
            }
        });

        // 2. Eliminar elemento
        btnEliminar.addActionListener(e -> {
            String elem = txtElemento.getText().trim();
            if (conjunto.remove(elem)) {
                mostrar("Elemento eliminado: " + elem);
            } else {
                mostrar("Elemento NO encontrado.");
            }
        });

        // 3. Buscar elemento
        btnBuscar.addActionListener(e -> {
            String elem = txtElemento.getText().trim();
            if (conjunto.contains(elem)) {
                mostrar("Elemento encontrado: " + elem);
            } else {
                mostrar("Elemento NO encontrado.");
            }
        });

        // 4. Mostrar todo el conjunto
        btnMostrar.addActionListener(e -> 
            mostrar("Conjunto actual:\n" + conjunto.toString())
        );

        // 5. Limpiar conjunto
        btnLimpiar.addActionListener(e -> {
            conjunto.clear();
            mostrar("Conjunto limpiado.");
        });

        // 6. Unión de conjuntos
        btnUnion.addActionListener(e -> {
            // Otro conjunto adicional
            Set<String> otro = new HashSet<>();
            otro.add("Lapicero");
            otro.add("Regla");
            otro.add("Mouse");

            // Operación de unión
            conjunto.addAll(otro);
            mostrar("Unión realizada con: " + otro);
        });

        // Configurar ventana
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(txtResultado), BorderLayout.CENTER);

        setSize(600, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Muestra un mensaje en el área de texto
     */
    private void mostrar(String msg) {
        txtResultado.append(msg + "\n");
    }

    public static void main(String[] args) {
        new AppConjuntos();
    }
}
