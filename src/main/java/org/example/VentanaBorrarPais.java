package org.example;

import javax.swing.*;
import java.awt.event.*;

public class VentanaBorrarPais extends JDialog {
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

    public VentanaBorrarPais() {
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
        SQLConnectionPais sqlConnectionPais = new SQLConnectionPais();
        //Crea un objeto ciudad para pasarlo al metodo borrar
        Pais pais = new Pais(textFieldCode.getText());
        sqlConnectionPais.borrar(pais);
        VentanaTable.situation = 17;
        dispose();
    }

    private void onCancel() {
        VentanaTable.situation = 0;
        dispose();
    }

    public static void iniciar(VentanaTable ventana) {
        VentanaBorrarPais dialog = new VentanaBorrarPais();
        //Pasa los valores de la ventana principal a esta
        dialog.textFieldCode.setText(ventana.codePaisSelect);
        dialog.textFieldNombre.setText(ventana.nombrePaisSelect);
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
