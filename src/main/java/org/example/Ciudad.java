package org.example;

public class Ciudad {
    private long id;
    private String nombre;
    private String district;
    private long population;
    private String countryCode;

    //Constructor solo con ID
    public Ciudad(long id) {
        this.id = id;
    }

    //Constructor que no necesita el codigo del pais
    public Ciudad(long id, String nombre, String district, long population) {
        this.id = id;
        this.nombre = nombre;
        this.district = district;
        this.population = population;
    }

    //Constructor que no necesita el id
    public Ciudad(String nombre, String district, long population, String countryCode) {
        this.nombre = nombre;
        this.district = district;
        this.population = population;
        this.countryCode = countryCode;
    }

    //Constructor que tiene todas las columnas
    public Ciudad(long id, String nombre, String district, long population, String countryCode) {
        this.id = id;
        this.nombre = nombre;
        this.district = district;
        this.population = population;
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }
}
