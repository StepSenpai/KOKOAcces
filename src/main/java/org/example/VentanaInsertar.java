package org.example;

import javax.swing.*;
import java.awt.event.*;

public class VentanaInsertar extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel panelBottom;
    private JPanel panelUp;
    private JPanel panelButton;
    private JTextField textFieldID;
    private JTextField textFieldNombre;
    private JTextField textFieldDistrito;
    private JTextField textFieldPoblacion;
    private JLabel labelID;
    private JLabel labelNombre;
    private JLabel labelDistrito;
    private JLabel labelPoblacion;
    private String code;

    public VentanaInsertar() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        textFieldPoblacion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //Variable de caracter insertado en el teclado
                char caracterTecleado = e.getKeyChar();
                //Comprueba de que sea otra cosa que un digito, si lo es, lo elimina
                if (caracterTecleado < '0' || caracterTecleado > '9') e.consume();
            }
        });
    }

    private void onOK() {
        //Si algun campo esta vacio entonces saltara una ventana que informara que rellene los datos
        if (textFieldNombre.getText().isEmpty() || textFieldDistrito.getText().isEmpty() || textFieldPoblacion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Rellena los campos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        //En la base de datos poblacion esta como int, para que no salga un error al insertar este numero,
        // se comprueba que no sea superior al valor maximo de int
        else if (Double.parseDouble(textFieldPoblacion.getText()) > 2147483647) {
            JOptionPane.showMessageDialog(null, "Poblacion insertada supera al maximo posible", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            //Creara un objeto Ciudad para pasarlo a la clase SQL que lo pasara como sentencia SQL
            Ciudad ciudad = new Ciudad(textFieldNombre.getText(), textFieldDistrito.getText(), Long.parseLong(textFieldPoblacion.getText()), code);
            SQLConnectionCiudad sqlConnectionCiudad = new SQLConnectionCiudad();
            sqlConnectionCiudad.insertar(ciudad);
            VentanaTable.situation = 8;
            dispose();
        }
    }

    private void onCancel() {
        VentanaTable.situation = 0;
        dispose();
    }

    public static void iniciar(VentanaTable ventana) {
        VentanaInsertar dialog = new VentanaInsertar();
        dialog.code = ventana.codePaisSelect;
        dialog.setBounds(350, 300, 500, 300);
        dialog.setResizable(false);
        dialog.setVisible(true);

        //Actualiza con los metodos el estado actual de la ventana
        ventana.hacerTabla();
        ventana.loadPics();
        ventana.showTalk();
    }
}
