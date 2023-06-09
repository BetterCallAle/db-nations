package org.lessons.java;

import java.time.LocalDate;
import java.util.List;

public class Country {
    private String name;
    private List<String> languages;
    private long year;
    private long population;
    private long gdp;

    //CONSTRUCTOR
    public Country(String name,List<String> languages, long year, long population, long gdp) {
        this.name = name;
        this.languages = languages;
        this.year = year;
        this.population = population;
        this.gdp = gdp;
    }

    //GETTERS
    public String getName() {
        return name;
    }

    public String getLanguages(){
        String languages = String.join(", ", this.languages);

        return languages;
    }

    public long getYear() {
        return year;
    }

    public long getPopulation() {
        return population;
    }

    public long getGdp() {
        return gdp;
    }

    //METHODS
    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", population=" + population +
                ", gdp=" + gdp +
                '}';
    }
}
