package org.example;

import javax.swing.*;
import java.awt.event.*;

public class VentanaInsertarPais extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel panelMain;
    private JPanel panelField;
    private JPanel panelButton;
    private JTextField textFieldCode;
    private JTextField textFieldNombre;
    private JLabel labelCode;
    private JLabel labelNombre;

    public VentanaInsertarPais() {
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

        //KeyListener para el campo code, solo permitira 3 caracteres
        //SQL permite insertar como codigo lo que sea, pero trunca el valor hacia las 3 primeras letras
        textFieldCode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //Variable de caracter insertado en el teclado
                char caracterTecleado = e.getKeyChar();
                //Cuando el textfield sea mayor que 3 caracteres no permitira mas caracteres
                if (textFieldCode.getText().length() >= 3) e.consume();
            }
        });
    }

    private void onOK() {
        //Si algun campo esta vacio entonces saltara una ventana que informara que rellene los datos
        if (textFieldCode.getText().isEmpty() || textFieldNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Rellena los campos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        //En la base de datos code es la clave primaria y no esta permitido repetirla en dos paises
        //Se utiliza la cadena de la clase VentanaTable que contienene todos los codigos, si esta cadena contiene a los valores introducidos,
        //entonces no permitira la inserccion del pais, los code estan separados por "," para que el final de un code no cuente con el inicio de otro
        //SQL ve a mayusculas y minusculas como el mismo codigo, mientras que el contains no lo hace, para solucionarlo, se pasa a mayusculas ambos en esta comprobacion
        else if (VentanaTable.allCodePais.toUpperCase().contains(textFieldCode.getText().toUpperCase())) {
            JOptionPane.showMessageDialog(null, "Codigo insertado ya existe", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            //Creara un objeto Pais para pasarlo a la clase SQL que lo pasara como sentencia SQL
            Pais pais = new Pais(textFieldCode.getText(),textFieldNombre.getText());
            SQLConnectionPais sqlConnectionPais = new SQLConnectionPais();
            sqlConnectionPais.insertar(pais);
            VentanaTable.situation = 16;
            dispose();
        }
    }

    private void onCancel() {
        VentanaTable.situation = 0;
        dispose();
    }

    public static void iniciar(VentanaTable ventana) {
        VentanaInsertarPais dialog = new VentanaInsertarPais();
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
