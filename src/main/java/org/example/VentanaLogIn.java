package org.example;

import javax.swing.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class VentanaLogIn extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel panelButton;
    private JTextField textFieldUser;
    private JPasswordField passwordField;
    private JLabel labelUser;
    private JLabel labelPassword;
    private JPanel panelBottom;
    private JPanel panelField;
    private JPanel panelPic;
    private JLabel labelPic;
    private JLabel labelDialog;
    private JPanel panelDialog;
    private JTextField textFieldTable;
    private JLabel labelTable;
    private static String url = "jdbc:mysql://localhost:3306/geografia";
    private static String login = "root";
    private static boolean repeated = false;

    public VentanaLogIn() {
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
        saveText();
        sendText();
        testConnection();
        dispose();
    }

    private void onCancel() {
        System.exit(0);
    }


    private void getText() {
        //Obtiene los valores de las variables guardadas
        textFieldTable.setText(url);
        textFieldUser.setText(login);
        //Password no se guarda
    }

    private void saveText() {
        //Guarda los datos introducidos en variables del objeto
        url = textFieldTable.getText();
        login = textFieldUser.getText();
        //Password no se guarda
    }

    //Prueba si los datos introducidos funcionan como connection con la base de datos
    private void testConnection() {
        SQLConnectionCiudad sqlConnectionCiudad = new SQLConnectionCiudad();
        try {
            sqlConnectionCiudad.dataSource.getConnection();
            dispose();
            VentanaTable.iniciar();
            //Si no se obtiene la connection entonces hay un error
        } catch (SQLException e) {
            //Se repite esta ventana modificando el mensaje con un booleano
            repeated = true;
            dispose();
            main();
        }
    }

    private void sendText() {

        //Modificara los valores del archivo Properties
        Properties properties = new Properties();

        //Try with resources que obtiene los recursos del archivo db_config-properties

        // try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("db_config.properties")) {

        // properties.load(inputStream);

        //Pondra los valores de los textField en el archivo properties
        properties.setProperty("db.url", textFieldTable.getText());
        properties.setProperty("db.login", textFieldUser.getText());
        //Password necesita volver String, si devuelve pasword devuelve un array que no funciona como password
        properties.setProperty("db.pass", passwordField.getText());

        // } catch (IOException e) {
        //    e.printStackTrace();
        // }

        //Output, para enviar los datos al archivo y guardarlos, hay que especificar la ruta concretamente
        //Parece que no se puede modificar un archivo dentro de resources miesntras que la aplicacion esta siendo ejecutada

        //src/main/java/db_config.properties
        //db_config.properties

        try (FileOutputStream fileOutputStream = new FileOutputStream("db_config.properties")) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPics() {
        //Si se ha repetido esta ventana cambia la imagen
        if (repeated) labelPic.setIcon(new ImageIcon(getClass().getResource("/img/kokoStickerFail.png")));
        else labelPic.setIcon(new ImageIcon(getClass().getResource("/img/kokoStickerRegular.png")));
    }

    private void showTalk() {
        //JLabel no entiende el salto de linea \n, es necesario pasarle el String como <html> y hacer el salto de lina con <br>
        //Si el label es pequeño se realizan saltos de linea automaticamente tambien
        //Mensaje cambia cuando esta pantalla se ha repetido, lo cual significa que hubo un error
        String kokoTalk = "";
        if (repeated)
            //Para que este en rojo
            kokoTalk = "<html><p><span style=\"color: red;\">Parece que ha habido un error al intentar conectarse a la base de datos con estos datos, introduce otros datos.<p><span><html>";
        else
            kokoTalk = "<html>Introduce la url de la tabla con la que quieres trabajar, el usuario y la contraseña de la cuenta que deseas utilizar.<html>";
        //Utilizado para mostrar el texto un caracter detras de otro, debido a que se utiliza un Stream, es necesario que las variables sean finales
        final int[] index = {0};
        String finalKokoTalk = kokoTalk;
        //Timer que es utilizado para mostrar el texto un caracter detras de otro, utiliza a un listener
        //este listener consiste de un if que comprueba que la variable de un contador indice sea menor que el valor del timer
        Timer timer = new Timer(13, e -> {
            //Mientras que el indice sea menor que la longitud del texto
            if (index[0] < finalKokoTalk.length()) {
                //Pondra como texto el texto que ya esta mas el caracter en la posicion del indice que corresponde al Timer
                labelDialog.setText(labelDialog.getText() + finalKokoTalk.charAt(index[0]));
                index[0]++;
            }
        });
        //Timer tiene que repetirse, sino solo se haria una vez
        timer.setRepeats(true);
        timer.start();
    }

    public static void main() {
        VentanaLogIn dialog = new VentanaLogIn();
        dialog.getText();
        dialog.setTitle("Gestion Geografia");
        dialog.loadPics();
        dialog.showTalk();
        dialog.setResizable(false);
        dialog.setBounds(600, 250, 750, 500);
        dialog.setVisible(true);
    }

}