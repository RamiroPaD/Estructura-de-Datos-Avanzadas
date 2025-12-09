
/**
 * @author Ramiro Padierna Delgado
 * @author 1224100710.rpd@gmail.com
 * Simulador de DOM en Java Swing
 * ---------------------------------------
 * Esta aplicación permite:
 *  - Crear nodos HTML dentro de un árbol (DOM)
 *  - Visualizar cómo se construye el HTML real
 *  - Agregar y eliminar nodos dinámicamente
 * Incluye:
 *  - Panel personalizado
 *  - Botones personalizados
 */
public class DomSimulator extends JFrame {

    // Árbol visual (lado izquierdo)
    private JTree tree;

    // Modelo del árbol DOM (estructura interna)
    private DefaultTreeModel treeModel;

    // Panel donde se muestra el código HTML (lado derecho)
    private JTextPane htmlPane;

    // Selección de etiqueta (h1, h2, p, etc.)
    private JComboBox<String> tagSelector;

    // Campo de texto para escribir contenido del nodo
    private JTextField textInput;

    // Botones para acciones del DOM
    private JButton addButton, deleteButton;

    /**
     * Constructor principal: crea toda la interfaz y funcionalidad
     */
    public DomSimulator() {

        // Configuración básica de la ventana
        setTitle("Simulación de DOM – Creación de Página Web");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // SplitPane separa vista izquierda (árbol) y derecha (HTML)
        JSplitPane splitPane = new JSplitPane();

        // --------------------------------------------------------------
        // 1. PANEL IZQUIERDO (Árbol DOM) con color #90E0EF
        // --------------------------------------------------------------

        // Nodo raíz del árbol <html>
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("html");

        // Modelo que controla la estructura del árbol
        treeModel = new DefaultTreeModel(root);

        // Componente visual del árbol
        tree = new JTree(treeModel);

        // Panel izquierdo con color personalizado
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.decode("#90E0EF"));
        leftPanel.add(new JScrollPane(tree), BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);

        // --------------------------------------------------------------
        // 2. PANEL DERECHO (Vista del HTML generado) color #9EECFF
        // --------------------------------------------------------------

        htmlPane = new JTextPane();
        htmlPane.setEditable(false); // Solo lectura
        htmlPane.setBackground(Color.decode("#9EECFF"));

        updateHTML(); // Generar HTML inicial (<html></html>)

        splitPane.setRightComponent(new JScrollPane(htmlPane));

        // --------------------------------------------------------------
        // 3. PANEL INFERIOR (Controles para agregar / eliminar nodos)
        // --------------------------------------------------------------

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ComboBox para elegir etiqueta HTML
        tagSelector = new JComboBox<>(new String[]{"h1", "h2", "p", "div"});

        // Campo para ingresar texto del nodo
        textInput = new JTextField(20);

        // Botón verde para agregar nodo
        addButton = new JButton("Agregar Nodo");
        addButton.setBackground(Color.GREEN);
        addButton.setForeground(Color.BLACK);

        // Botón rojo para eliminar nodo
        deleteButton = new JButton("Eliminar Nodo");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);

        // Añadir componentes al panel inferior
        bottomPanel.add(new JLabel("Etiqueta:"));
        bottomPanel.add(tagSelector);
        bottomPanel.add(new JLabel("Texto:"));
        bottomPanel.add(textInput);
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);

        // Agregar paneles principales a la ventana
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Eventos de los botones
        addButton.addActionListener(e -> addNode());
        deleteButton.addActionListener(e -> deleteNode());

        setVisible(true); // Mostrar ventana
    }

    /**
     * Método que agrega un nodo al árbol DOM
     */
    private void addNode() {

        // Obtener nodo seleccionado
        TreePath selectedPath = tree.getSelectionPath();

        if (selectedPath == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un nodo del árbol.");
            return;
        }

        DefaultMutableTreeNode parentNode =
                (DefaultMutableTreeNode) selectedPath.getLastPathComponent();

        // Obtener datos de interfaz
        String tag = tagSelector.getSelectedItem().toString();
        String text = textInput.getText();

        // Crear el nodo HTML completo
        String newNodeName = "<" + tag + ">" + text + "</" + tag + ">";

        // Crear nodo visual
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newNodeName);

        // Insertar en árbol
        treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());

        // Expandir para que se vea
        tree.expandPath(selectedPath);

        // Actualizar vista HTML
        updateHTML();
    }

    /**
     * Elimina el nodo seleccionado del árbol DOM
     */
    private void deleteNode() {

        TreePath selectedPath = tree.getSelectionPath();

        if (selectedPath == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un nodo para eliminar.");
            return;
        }

        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode) selectedPath.getLastPathComponent();

        // No permitir borrar la raíz <html>
        if (node.isRoot()) {
            JOptionPane.showMessageDialog(this, "No se puede eliminar la raíz (html).");
            return;
        }

        // Eliminar del modelo
        treeModel.removeNodeFromParent(node);

        // Actualizar vista HTML
        updateHTML();
    }

    /**
     * Actualiza el texto HTML del panel derecho, basado en el árbol DOM
     */
    private void updateHTML() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        String html = generateHTML(root, 0);

        htmlPane.setText(""); // Limpiar panel
        appendStyledHTML(html); // Pintar el nuevo HTML
    }

    /**
     * Genera el HTML con indentación (espacios) recorriendo el árbol
     */
    private String generateHTML(DefaultMutableTreeNode node, int indent) {

        StringBuilder sb = new StringBuilder();

        // Crear indentación
        String indentSpaces = " ".repeat(indent * 2);

        // Obtener nodo
        String nodeText = node.getUserObject().toString();

        // Escribir línea de nodo
        sb.append(indentSpaces).append(nodeText).append("\n");

        // Recorrer hijos de manera recursiva
        for (int i = 0; i < node.getChildCount(); i++) {
            sb.append(generateHTML((DefaultMutableTreeNode) node.getChildAt(i), indent + 1));
        }

        return sb.toString();
    }

    /**
     * Inserta el HTML generado con estilo.
     * (Todo en color negro para etiquetas y texto)
     */
    private void appendStyledHTML(String fullHTML) {

        StyledDocument doc = htmlPane.getStyledDocument();

        // Estilo general negro
        Style normalStyle = htmlPane.addStyle("normalStyle", null);
        StyleConstants.setForeground(normalStyle, Color.BLACK);

        try {
            doc.insertString(doc.getLength(), fullHTML, normalStyle);
        } catch (Exception ex) {
            // Ignorar errores menores
        }
    }

    /**
     * Método principal: inicia la aplicación
     */
    public static void main(String[] args) {
        new DomSimulator();
    }
}
