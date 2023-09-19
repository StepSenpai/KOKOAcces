package org.example;

import javax.swing.*;
import java.awt.event.*;

public class VentanaActualizarPais extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel panelMain;
    private JPanel panelButton;
    private JPanel panelField;
    private JTextField textFieldCode;
    private JTextField textFieldNombre;
    private JLabel labelCode;
    private JLabel labelNombre;

    public VentanaActualizarPais() {
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

        //Si algun campo esta vacio entonces saltara una ventana que informara que rellene los datos
        if (textFieldCode.getText().isEmpty() || textFieldNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Rellena todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            //Crea un objeto Pais con los datos de los campos y lo envia a la base de datos utilizando la connectionSQL
            Pais pais = new Pais(textFieldCode.getText(),textFieldNombre.getText());
            SQLConnectionPais sqlConnectionPais = new SQLConnectionPais();
            sqlConnectionPais.actualizar(pais);
            VentanaTable.situation = 15;
            dispose();
        }
    }

    private void onCancel() {
        VentanaTable.situation = 0;
        dispose();
    }

    void rellenarCampos(VentanaTable ventana) {
        textFieldCode.setText(ventana.codePaisSelect);
        textFieldNombre.setText(ventana.nombrePaisSelect);
    }

    public static void iniciar(VentanaTable ventana) {
        VentanaActualizarPais dialog = new VentanaActualizarPais();
        //Pasa los valores de la ventana Table esta Clase
        dialog.rellenarCampos(ventana);
        dialog.setBounds(350, 300, 500, 300);
        dialog.setResizable(false);
        dialog.setVisible(true);

        //Actualiza con los metodos el estado actual de la ventana
        //Vacia la lista de todos los paises
        VentanaTable.listaPaises.clear();
        ventana.ponerPaisesBox();
        ventana.hacerTabla();
        ventana.loadPics();
        ventana.showTalk();

    }
}
