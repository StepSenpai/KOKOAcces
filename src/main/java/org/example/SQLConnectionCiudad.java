package org.example;

import com.sun.tools.javac.Main;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SQLConnectionCiudad implements Dao<Ciudad> {
    // Un DataSource es un intermediario para obtener las conexiones con la base de datos
    DataSource dataSource;

    //Contructor de la clase, genera el DataSource
    public SQLConnectionCiudad() {
        // Configurar el BasicDataSource con los datos de la base de datos
        Properties properties = new Properties();
        //Try with resources que obtiene los recursos del archivo db_config-properties
//        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("/db_config.properties")) {
//            properties.load(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //No funciona con el metodo anterior debido a que no se puede modificar un archivo dentro de la carpeta resources
        //mientras que la aplicacion esta en ejecucion, debido a esto el archivo properties se encuentra fuera

        //src/main/java/db_config.properties
        //db_config.propertiesz

        try (InputStream fileInputStream = new FileInputStream("db_config.properties")) {
            properties.load(fileInputStream);
            //Inserta lso datos obtenidos del config.properties al DataSource
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(properties.getProperty("db.url"));
            basicDataSource.setUsername(properties.getProperty("db.login"));
            basicDataSource.setPassword(properties.getProperty("db.pass"));
            this.dataSource = basicDataSource;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Obtiene todas las ciudades a partir de un codigo Pais
    @Override
    public List<Ciudad> obtenerTodos() {
        //Crea un ArrayList con las ciudades
        List<Ciudad> ciudades = new ArrayList<>();
        //Por si existe algun dato de una consulta anterior se reinicia la lista
        ciudades.clear();
        //Try with resources
        try (Connection conn = dataSource.getConnection();
             //Realiza una consulta
             Statement stmt = conn.createStatement();
             //Obtiene un conjunto de los datos de la consulta
             ResultSet rs = stmt.executeQuery("SELECT id,name,district,population, countrycode FROM city")) {

            //Mientras haya otro resultado, obtiene los datos y los guarda como variables
            while (rs.next()) {
                long id = rs.getLong("id");
                String nombre = rs.getString("name");
                String distrito = rs.getString("district");
                long poblacion = rs.getLong("population");
                String countryCode = rs.getString("countrycode");

                //Añade una ciudad creada a partir de los datos obtenidos al arrayList
                Ciudad ciudad = new Ciudad(id, nombre, distrito, poblacion,countryCode);
                ciudades.add(ciudad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ciudades;
    }

    @Override
    public void insertar(Ciudad ciudad) {
        try (Connection conn = dataSource.getConnection();
             //Prepara una sentencia que inserta los datos pasados a la base de datos
             PreparedStatement pst = conn.prepareStatement("INSERT INTO city(name,countrycode,district,population) VALUES (?,?,?,?)")) {
            pst.setString(1, ciudad.getNombre());
            pst.setString(2, ciudad.getCountryCode());
            pst.setString(3, ciudad.getDistrict());
            pst.setLong(4, ciudad.getPopulation());

            //Ejecuta
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Ciudad ciudad) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pst = conn.prepareStatement("UPDATE city SET name=?,district=?,population=? WHERE id=?")) {

            pst.setString(1, ciudad.getNombre());
            pst.setString(2, ciudad.getDistrict());
            pst.setLong(3, ciudad.getPopulation());
            pst.setString(4, String.valueOf(ciudad.getId()));

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void borrar(Ciudad ciudad) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pst = conn.prepareStatement("DELETE FROM city WHERE id=?")) {

            pst.setLong(1, ciudad.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
