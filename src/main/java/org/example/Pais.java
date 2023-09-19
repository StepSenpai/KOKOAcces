package org.example;

public class Pais {
    private String code;
    private String nombre;

    //Constructor
    public Pais(String code, String nombre) {
        this.code = code;
        this.nombre = nombre;
    }
    public Pais(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //ToString, solo necesitaremos los nombres
    @Override
    public String toString() {
        return nombre;
    }
}
