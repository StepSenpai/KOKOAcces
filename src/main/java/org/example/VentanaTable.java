package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class VentanaTable extends JFrame {
    private JPanel panelMain;
    private JComboBox comboBoxPais;
    private JTable tableCiudades;
    JTextField textFieldID;
    JTextField textFieldNombre;
    JTextField textFieldDistrito;
    JTextField textFieldPoblacion;
    private JButton buttonActualizar;
    private JButton buttonBorrar;
    private JButton buttonInsertar;
    private JLabel labelID;
    private JLabel labelNombre;
    private JLabel labelDistrito;
    private JLabel labelPoblacion;
    private JPanel panelTop;
    private JPanel panelInsert;
    private JScrollPane scrollPanelTabla;
    private JPanel panelLeft;
    private JPanel panelDialog;
    private JPanel panelPic;
    private JLabel labelDialog;
    //static private JLabel labelDialog;
    private JLabel labelPic;
    private JTextField textFieldCodePais;
    private JLabel labelCodePais;
    private JButton buttonInsertarPais;
    private JButton buttonActualizarPais;
    private JButton buttonBorrarPais;
    private JTextField textFieldNombrePais;
    private JLabel labelNombrePais;
    private JPanel panelPais;

    String codePaisSelect;
    static String nombrePaisSelect;
    //static String nombrePaisSelect;
    static DefaultTableModel defaultTableModel;
    //Variable que tendra en cuenta la situacion actual
    static int situation = 0;

    //Lista que contiene todos los paises de la base de datos, necesario para otras ventanas
    static List<Pais> listaPaises;

    static String allCodePais;

    //Metodo llamado para poder crear y mostrar la ventana
    public static void iniciar() {

        //Crea un objeto de esta clase
        VentanaTable ventana = new VentanaTable();

        //ventana.setResizable(false);
        ventana.loadPics();
        ventana.showTalk();

        //Titulo de la ventana
        ventana.setTitle("Gestion Geografia");

        //Al cerrar la ventana se cierra el programa
        ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Abre la ventana en pantalla completa
        ventana.setExtendedState(MAXIMIZED_BOTH);

        //Se añade el panel principal a la ventana
        ventana.setContentPane(ventana.panelMain);

        //Cierra el programa la cerrar la ventana
        ventana.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Utiliza los metodos para los componentes
        ventana.ponerPaisesBox();

        //Listeners
        //Cuando se selecciona algo en el comboBox
        ventana.comboBoxPais.addActionListener(e -> {
            //Cambia la tabla al seleccionar otro pais, tambien asigna una variable del codigo del pais seleccionado
            //para ser utilizado en las otras ventanas
            Pais paisSelect = (Pais) ventana.comboBoxPais.getSelectedItem();
            if (paisSelect == null) {

            } else {
                ventana.codePaisSelect = paisSelect.getCode();
                ventana.nombrePaisSelect = paisSelect.getNombre();
                ventana.textFieldCodePais.setText(paisSelect.getCode());
                ventana.textFieldNombrePais.setText(paisSelect.getNombre());
                ventana.hacerTabla();
            }

        });
        //Cuando se selecciona algo en la tabla
        ventana.tableCiudades.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //Coloca los datos en los campos (Devuelve un objeto, por eso cast a String)
                ventana.textFieldID.setText((String) ventana.tableCiudades.getValueAt(ventana.tableCiudades.getSelectedRow(), 0));
                ventana.textFieldNombre.setText((String) ventana.tableCiudades.getValueAt(ventana.tableCiudades.getSelectedRow(), 1));
                ventana.textFieldDistrito.setText((String) ventana.tableCiudades.getValueAt(ventana.tableCiudades.getSelectedRow(), 2));
                ventana.textFieldPoblacion.setText((String) ventana.tableCiudades.getValueAt(ventana.tableCiudades.getSelectedRow(), 3));
            }
        });
        //Listeners de los botones de ciudad
        ventana.buttonActualizar.addActionListener(e -> {
            //Si no hay un registro seleccionado en la tabla, cambia el estado a uno de error, repitiendo los metodos
            //Si hay uno seleccionado entonces cambia el estado a uno de informacion
            if (ventana.tableCiudades.getSelectionModel().isSelectionEmpty()) {
                situation = 1;
                ventana.loadPics();
                ventana.showTalk();
            } else {
                situation = 4;
                ventana.loadPics();
                ventana.showTalk();
                VentanaActualizar.iniciar(ventana);
            }
        });
        ventana.buttonInsertar.addActionListener(e -> {
            //Si no hay un pais seleccionado en el comboBox cambia el estado a uno de error y repite los metodos
            //Si lo hay cambia el estado a otro que permite insertar un registro
            if (ventana.comboBoxPais.getSelectedItem() == null) {
                situation = 2;
                ventana.loadPics();
                ventana.showTalk();

            } else {
                situation = 5;
                ventana.loadPics();
                ventana.showTalk();
                VentanaInsertar.iniciar(ventana);
            }
        });
        ventana.buttonBorrar.addActionListener(e -> {
            if (ventana.tableCiudades.getSelectionModel().isSelectionEmpty()) {
                situation = 3;
                ventana.loadPics();
                ventana.showTalk();
            } else {
                situation = 6;
                ventana.loadPics();
                ventana.showTalk();
                VentanaBorrar.iniciar(ventana);
            }
        });
        //listeners de Pais buttons
        ventana.buttonActualizarPais.addActionListener(e -> {
            //Si no hay un pais seleccionado en el comboBox cambia el estado a uno de error y repite los metodos
            //Si lo hay cambia el estado a otro que permite actualizar el pais
            if (ventana.comboBoxPais.getSelectedItem() == null) {
                situation = 10;
                ventana.loadPics();
                ventana.showTalk();

            } else {
                situation = 12;
                ventana.loadPics();
                ventana.showTalk();
                VentanaActualizarPais.iniciar(ventana);
            }
        });
        ventana.buttonInsertarPais.addActionListener(e -> {
            //Insertar ub pais siempre es posible
            situation = 13;
            ventana.loadPics();
            ventana.showTalk();
            VentanaInsertarPais.iniciar(ventana);
        });
        ventana.buttonBorrarPais.addActionListener(e -> {
            //Si no hay un pais seleccionado en el comboBox cambia el estado a uno de error y repite los metodos
            //Si lo hay cambia el estado a otro que permite actualizar el pais
            if (ventana.comboBoxPais.getSelectedItem() == null) {
                situation = 11;
                ventana.loadPics();
                ventana.showTalk();

            } else {
                situation = 14;
                ventana.loadPics();
                ventana.showTalk();
                VentanaBorrarPais.iniciar(ventana);
            }
        });

        //Hace que la ventana sea visible
        ventana.setVisible(true);
    }

    void loadPics() {
        //Dependiendo de la situacion, cambia de imagen
        switch (situation) {
            case 0, 4, 5, 6, 12, 13, 14:
                labelPic.setIcon(new ImageIcon(getClass().getResource("/img/kokoStickerRegular.png")));
                break;
            case 1, 2, 3, 10, 11:
                labelPic.setIcon(new ImageIcon(getClass().getResource("/img/kokoStickerFail.png")));
                break;
            case 7, 8, 9, 15, 16, 17:
                labelPic.setIcon(new ImageIcon(getClass().getResource("/img/kokoStickerSuccess.png")));
                break;
        }
    }

    //TODO
    // Aqui el timer static para toda la clase, hay que asegurar que se cierre cada vez que se necesite un reset en un
    // cambio de situation, necesitara variables locales dentro de la clase anonima, debido al static sera necesario que
    // ciertos elementos de la ventana sean static tambien

    void showTalk() {
        //JLabel no entiende el salto de linea \n, es necesario pasarle el String como <html> y hacer el salto de lina con <br>
        //Si el label es pequeño se realizan saltos de linea automaticamente tambien
        //Dependiendo de la situacion actual (error, modificacion, informacion) cambia el mensaje mostrado
        String kokoTalk = "";
        switch (situation) {
            case 0:
                kokoTalk = "<html>Selecciona un pais de la lista desplegable. Puedes modificar, añadir o borrar paises. " +
                        "Una vez seleccionado se mostraran las ciudades de ese pais y su informacion, puedes modificar, " +
                        "borrar o insertar ciudades de ese pais seleccionado.<html>";
                break;
            case 1:
                //Para que salga en rojo
                kokoTalk = "<html><p><span style=\"color: red;\">Para poder actualizar un registro, primero necesitas seleccionarlo en la tabla.<p><span><html>";
                break;
            case 2:
                kokoTalk = "<html><p><span style=\"color: red;\">Para poder insertar un registro, primero necesitas seleccionar un pais de la lista desplegable.<p><span><html>";
                break;
            case 3:
                kokoTalk = "<html><p><span style=\"color: red;\">Para poder borrar un registro, primero necesitas seleccionarlo en la tabla.<p><span><html>";
                break;
            case 4:
                kokoTalk = "<html>Vas a actualizar un registro en <html>" + nombrePaisSelect + ".";
                break;
            case 5:
                kokoTalk = "<html>Vas a insertar un registro en <html>" + nombrePaisSelect + ".";
                break;
            case 6:
                kokoTalk = "<html>¿Deseas borrar este registro en <html>" + nombrePaisSelect + "?";
                break;
            case 7:
                kokoTalk = "<html><p><span style=\"color: green;\">El registro ha sido actualizado correctamente.<p><span><html>";
                break;
            case 8:
                kokoTalk = "<html><p><span style=\"color: green;\">El registro ha sido insertado correctamente.<span><p><html>";
                break;
            case 9:
                kokoTalk = "<html><p><span style=\"color: green;\">El registro ha sido eliminado.<span><p><html>";
                break;
            case 10:
                kokoTalk = "<html><p><span style=\"color: red;\">Para poder actualizar un pais, primero necesitas seleccionarlo en la lista.<span><p><html>";
                break;
            case 11:
                kokoTalk = "<html><p><span style=\"color: red;\">Para poder borrar un pais, primero necesitas seleccionarlo en la lista.<span><p><html>";
                break;
            case 12:
                kokoTalk = "<html>Vas a actualizar el siguiente pais.<html>";
                break;
            case 13:
                kokoTalk = "<html>Vas a insertar el siguiente pais.<html>";
                break;
            case 14:
                kokoTalk = "<html>¿Deseas borrar este pais?<html>";
                break;
            case 15:
                kokoTalk = "<html><p><span style=\"color: green;\">El pais ha sido actualizado correctamente.<p><span><html>";
                break;
            case 16:
                kokoTalk = "<html><p><span style=\"color: green;\">El pais ha sido insertado correctamente.<span><p><html>";
                break;
            case 17:
                kokoTalk = "<html><p><span style=\"color: green;\">El pais ha sido eliminado.<span><p><html>";
                break;
        }

        //labelDialog.setText(kokoTalk);

        //TODO
        // No he conseguido hacer que el texto que se va mostrando no se sume al texto anterior que ya ha sido escrito sin tener que
        // abrir un nuevo objeto de esta clase, causando que se abra otra vez una ventana, esto provoca que el texto se rompe si se
        // repite varias veces por lo que se tendra que quedar sin el efecto de dialogo

         /*

        //TODO
        // Indice utilizado para mostrar el texto un caracter detras de otro
        // Debido a que se trata de una clase anonima/lamda es necesario que las variables sean FINAL (aunque no lo ponga lo son),
        // un contador final no puede cambiar de valor y se quedaria igual haciendolo inutil, por eso se utiliza un Array que
        // va aumentando de posicion y utiliza la posicion en la que encuentra como indice para obtener el char del String
        //final int index = 0;
        int[] index = {0};
        String finalKokoTalk = kokoTalk;

        //TODO
        // Test (hace reset del label aun asi no sirve, ya que el nuevo char del String correspondiente en la posicion del
        // Timer es añadido al FinalString y mostrara cosas incoherentes)

        labelDialog.setText("");

        //Timer que es utilizado para mostrar el texto un caracter detras de otro, utiliza a un listener
        //este listener consiste de un if que comprueba que la variable de un contador indice sea menor que el valor del timer
        Timer timer = new Timer(0, e -> {

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

          */

        //TODO
        // Otro metodo sin la clase anonima del ActionListener, esto causa que no funcione el Timer correctamente (solo realiza 1 tick)

        /*

        //Timer que es utilizado para mostrar el texto un caracter detras de otro, utiliza a un listener
        //este listener consiste de un if que comprueba que la variable de un contador indice sea menor que el valor del timer
        Timer timer = new Timer(18, null) {

           {

                //Mientras que el indice sea menor que la longitud del texto
                if (index[0] < finalKokoTalk.length()) {
                    //Pondra como texto el texto que ya esta mas el caracter en la posicion del indice que corresponde al Timer
                    labelDialog.setText(labelDialog.getText() + finalKokoTalk.charAt(index[0]));
                    index[0]++;
                }
            }
        };
        //Timer tiene que repetirse, sino solo se haria una vez
        timer.setRepeats(true);
        timer.start();

         */

        //TODO
        // Haciendo un Array de char que va poniendo cada char en posicion para el dialogo

        /*
        labelDialog.setText("");
        int[] index = {0};
        char[] kokoTalkChar = kokoTalk.toCharArray();
        Timer timer = new Timer(0, e -> {
            if (index[0] < kokoTalkChar.length) {
                labelDialog.setText(labelDialog.getText() + kokoTalkChar[index[0]]);
                index[0]++;
            }
        });
        timer.setRepeats(true);
        timer.start();

         */

        //TODO
        // Dialogo de Kokomi funcional

        labelDialog.setText("");
        int[] index = {0};
        char[] kokoTalkChar = kokoTalk.toCharArray();
        Timer timer = new Timer(0, e -> {
            if (index[0] < kokoTalkChar.length) {
                labelDialog.setText(labelDialog.getText() + kokoTalkChar[index[0]]);
                index[0]++;
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    void ponerPaisesBox() {
        //Crea un objeto de la Clase PaisSQL y utiliza su metodo para obtener los paises de la base de datos
        SQLConnectionPais sqlConnectionPais = new SQLConnectionPais();

        //Crea una lista con todos los paises
        listaPaises = sqlConnectionPais.obtenerTodos();

        //Utiliza un lambda (clase anonima) para ordenar esta lista con un comparator
        listaPaises.sort(Comparator.comparing(Pais::getNombre));

        //Cuando se modifica un pais, hay que quitar todos los objetos del comboBox para que los inserte correctamente de nuevo
        comboBoxPais.removeAllItems();

        //Necesario para poder introducir paises sin errores
        //Esta cadena estatica es utilizada para tener todos los codigos de todos los paises de la base de datos
        // los code se separaran con "," y se reinician cada vez se modifique la lista de paises
        allCodePais = "";

        //Inserta todos los paises en la ComboBox
        for (Pais pais : listaPaises) {
            allCodePais += pais.getCode() + ",";
            comboBoxPais.addItem(pais);
        }

        //Selecciona al vacio
        comboBoxPais.setSelectedIndex(-1);
    }

    void hacerTabla() {
        //Crea un objeto de la Clase CiudadSQL y utiliza su metodo para obtener todas las ciudades de la base de datos
        SQLConnectionCiudad sqlConnectionCiudad = new SQLConnectionCiudad();
        List<Ciudad> listaCiudades = sqlConnectionCiudad.obtenerTodos();

        //Array para encabezado de las columnas
        String[] columnas = {"ID", "Nombre", "Distrito", "Población"};

        //ArayList que contendra las ciudades de un pais
        ArrayList<Ciudad> listaCiudadPais = new ArrayList<>();

        //Crea un objeto Pais del objeto sleccionado del combo box
        Pais pais = (Pais) comboBoxPais.getSelectedItem();

        //Cuando el Codigo del Pais seleccionado es el mismo que el de una ciudad lo añade al ArrayList listaCiudadPais
        //Tambien se le añade que no sea null debido a una exception cuando se modifica los paises
        for (Ciudad ciudad : listaCiudades) {
            if (pais != null && pais.getCode().equals(ciudad.getCountryCode())) {
                listaCiudadPais.add(ciudad);
            }
        }

        //Array para rellenar la tabla
        String[][] data = new String[listaCiudadPais.size()][4];

        //Rellena el Array de datos para la tabla con los datos del ArrayList
        for (int i = 0; i < listaCiudadPais.size(); i++) {
            data[i][0] = String.valueOf(listaCiudadPais.get(i).getId());
            data[i][1] = String.valueOf(listaCiudadPais.get(i).getNombre());
            data[i][2] = String.valueOf(listaCiudadPais.get(i).getDistrict());
            data[i][3] = String.valueOf(listaCiudadPais.get(i).getPopulation());
        }

        //Hace un modelo para crear la tabla a partir de los dos Array
        //DefaultTableModel model = new DefaultTableModel(data, columnas);
        //Hace una clase anonima a partir de DefaultTableModel que sobrescribe el metodo isCellEditable para que siempre sea falso
        defaultTableModel = new DefaultTableModel(data, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        //Selecciona el model y lo añade a la tabla
        tableCiudades.setModel(defaultTableModel);

        //Obtiene la cabecera de la tabla y no permitre que se muevan
        tableCiudades.getTableHeader().setReorderingAllowed(false);
    }
}