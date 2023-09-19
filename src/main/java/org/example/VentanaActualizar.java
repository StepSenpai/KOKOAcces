package org.example;

import javax.swing.*;
import java.awt.event.*;

public class VentanaActualizar extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldID;
    private JTextField textFieldNombre;
    private JTextField textFieldDistrito;
    private JLabel labelID;
    private JLabel labelNombre;
    private JLabel labelDistrito;
    private JTextField textFieldPoblacion;
    private JLabel labelPoblacion;
    private JPanel panelButton;
    private JPanel panelField;
    private JPanel panelMain;

    private String codePais;

    public VentanaActualizar() {
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
        textFieldPoblacion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //Variable de caracter insertado en el teclado
                char caracterTecleado = e.getKeyChar();
                //Comprueba de que sea otra cosa que un digito, si lo es, lo elimina
                if (caracterTecleado < '0' || caracterTecleado > '9') e.consume();
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

    void rellenarCampos(VentanaTable ventana) {
        textFieldID.setText(ventana.textFieldID.getText());
        textFieldNombre.setText(ventana.textFieldNombre.getText());
        textFieldDistrito.setText(ventana.textFieldDistrito.getText());
        textFieldPoblacion.setText(ventana.textFieldPoblacion.getText());
    }

    //Cuando se da click en OK, actualiza los datos, guarda en la base de datos y cierra
    private void onOK() {
        //TODO
        // Se podria utilizar VentanaTable.situation para modificar el estado de la ventana y cambiar la imagen y texto
        // Pero no se modifica su valor hasta que la ventana de dialogo este cerrada, por lo que no resulta util
        // Debido a esto se utilizan las ventanillas de error
        //Si algun campo esta vacio entonces saltara una ventana que informara que rellene los datos
        if (textFieldID.getText().isEmpty() || textFieldNombre.getText().isEmpty() || textFieldDistrito.getText().isEmpty() || textFieldPoblacion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Rellena todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        //En la base de datos poblacion esta como int, para que no salga un error al modificar este numero,
        // se comprueba que no sea superior al valor maximo de int
        else if (Double.parseDouble(textFieldPoblacion.getText()) > 2147483647) {
            JOptionPane.showMessageDialog(null, "Poblacion insertada supera al maximo posible", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            //Crea un objeto Ciudad con los datos de los campos y lo envia a la base de datos utilizando la connectionSQL
            Ciudad ciudad = new Ciudad(Long.parseLong(textFieldID.getText()), textFieldNombre.getText(), textFieldDistrito.getText(), Long.parseLong(textFieldPoblacion.getText()), codePais);
            SQLConnectionCiudad sqlConnectionCiudad = new SQLConnectionCiudad();
            sqlConnectionCiudad.actualizar(ciudad);
            VentanaTable.situation = 7;
            dispose();
        }
    }

    //Cuando se hace click en cancel, no hace nada, se cierra
    private void onCancel() {
        VentanaTable.situation = 0;
        dispose();
    }

    //La ventana principal es pasada como argumento para pasar los valores
    public static void iniciar(VentanaTable ventana) {
        VentanaActualizar dialog = new VentanaActualizar();
        //Pasa el valor del codigoPais a esta Clase
        dialog.codePais = ventana.codePaisSelect;
        dialog.setBounds(350, 300, 500, 300);
        dialog.rellenarCampos(ventana);
        dialog.setResizable(false);
        dialog.setVisible(true);

        //Actualiza con los metodos el estado actual de la ventana
        ventana.hacerTabla();
        ventana.loadPics();
        ventana.showTalk();
    }
}
