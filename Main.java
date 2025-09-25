// 📦 Main.java

import interfaces.vistas.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}