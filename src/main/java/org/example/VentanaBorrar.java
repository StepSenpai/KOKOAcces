package org.example;

import javax.swing.*;
import java.awt.event.*;

public class VentanaBorrar extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel panelBottom;
    private JPanel panelButton;
    private JPanel panelUp;
    private JTextField textFieldID;
    private JLabel labelID;
    private JTextField textFieldNombre;
    private JTextField textFieldDistrito;
    private JLabel labelNombre;
    private JLabel labelDistrito;
    private JTextField textFieldPoblacion;
    private JLabel labelPoblacion;

    public VentanaBorrar() {
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
    }

    private void onOK() {
        //Crea un objeto de connect con la base de datos que genera una sentencia SQL con el valor seleccionado
        SQLConnectionCiudad sqlConnectionCiudad = new SQLConnectionCiudad();
        //Crea un objeto ciudad para pasarlo al metodo borrar
        Ciudad ciudad = new Ciudad(Long.parseLong(textFieldID.getText()));
        sqlConnectionCiudad.borrar(ciudad);
        VentanaTable.situation = 9;
        dispose();
    }

    private void onCancel() {
        VentanaTable.situation = 0;
        dispose();
    }

    public static void iniciar(VentanaTable ventana) {
        VentanaBorrar dialog = new VentanaBorrar();
        //Pasa los valores de la venatna principal a esta
        dialog.textFieldID.setText(ventana.textFieldID.getText());
        dialog.textFieldNombre.setText(ventana.textFieldNombre.getText());
        dialog.textFieldDistrito.setText(ventana.textFieldDistrito.getText());
        dialog.textFieldPoblacion.setText(ventana.textFieldPoblacion.getText());
        dialog.setBounds(350, 300, 500, 300);
        dialog.setResizable(false);
        dialog.setVisible(true);

        //Actualiza con los metodos el estado actual de la ventana
        ventana.hacerTabla();
        ventana.loadPics();
        ventana.showTalk();
    }
}
