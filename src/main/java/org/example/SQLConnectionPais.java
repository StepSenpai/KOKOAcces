package org.example;


import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SQLConnectionPais implements Dao<Pais> {

    // Un DataSource es un intermediario para obtener las conexiones con la base de datos
    DataSource dataSource;

    public SQLConnectionPais() {
        // Configurar el BasicDataSource con los datos de la base de datos
        Properties datos = new Properties();

        //src/main/java/db_config.properties
        //db_config.properties

        try (FileInputStream fileInputStream = new FileInputStream("db_config.properties")) {
            datos.load(fileInputStream);
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(datos.getProperty("db.url"));
            basicDataSource.setUsername(datos.getProperty("db.login"));
            basicDataSource.setPassword(datos.getProperty("db.pass"));
            this.dataSource = basicDataSource;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pais> obtenerTodos() {
        //Crea una lista que contendra todos los paises de la base
        List<Pais> paises = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             //Una sentencia SQL, selecciona todos los codigos y nombres de la tabla country
             Statement stmt = conn.createStatement();
             //Un conjunto dedl resultado de la sentencia
             ResultSet rs = stmt.executeQuery("SELECT code,name FROM country")) {

            //Miesntras que haya un siguiente objeto en el conjunto
            while (rs.next()) {
                //Obtiene los datos de la base
                String code = rs.getString("code");
                String nombre = rs.getString("name");
                //Añade un pais creado con los datos al ArrayList
                paises.add(new Pais(code, nombre));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paises;
    }

    @Override
    public void insertar(Pais pais) {
        try (Connection conn = dataSource.getConnection();
             //Prepara una sentencia que inserta los datos pasados a la base de datos
             PreparedStatement pst = conn.prepareStatement("INSERT INTO country(code,name) VALUES (?,?)")) {
            pst.setString(1, pais.getCode());
            pst.setString(2, pais.getNombre());

            //Ejecuta
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Pais pais) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pst = conn.prepareStatement("UPDATE country SET name=? WHERE code=?")) {

            pst.setString(1, pais.getNombre());
            pst.setString(2, pais.getCode());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void borrar(Pais pais) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pst = conn.prepareStatement("DELETE FROM country WHERE code=?")) {

            pst.setString(1, pais.getCode());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
