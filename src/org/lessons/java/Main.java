package org.lessons.java;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USERNAME = System.getenv("DB_USERNAME");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.print("Effettua una ricerca per nazione: ");
        String userChoice = "%";
        userChoice += scan.nextLine();
        userChoice += "%";

        try(Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)){
            String sql = """
                    SELECT countries.name, countries.country_id, regions.name, continents.name  FROM regions
                    JOIN continents on regions.continent_id = continents.continent_id
                    JOIN countries on regions.region_id = countries.region_id
                    WHERE countries.name LIKE ?
                    ORDER BY countries.name;
                    """;

            try(PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

                ps.setString(1, userChoice);

                try(ResultSet rs = ps.executeQuery()){

                    if(!rs.next()){
                        System.out.println("Non ci sono risultati per " + userChoice);
                    } else {
                        rs.beforeFirst();
                    }

                    while (rs.next()){
                        String regionName = rs.getString(1);
                        int countryId = rs.getInt(2);
                        String continentName = rs.getString(3);
                        String countryName = rs.getString(4);

                        System.out.println(regionName + " - " + countryId + " - " + continentName + " - " + countryName);
                    }

                }

            }

            System.out.print("Digita l'id del paese per avere più informazioni: ");
            int userId = Integer.parseInt(scan.nextLine());
            List<String> languages = new ArrayList<>();
            String countryName = null;


            sql = """
                      SELECT countries.name, languages.language FROM countries
                      JOIN country_languages on countries.country_id = country_languages.country_id
                      JOIN languages on languages.language_id = country_languages.language_id
                      WHERE countries.country_id = ?;
                    """;

            try(PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
                ps.setInt(1, userId);

                try(ResultSet rs = ps.executeQuery()){

                    if(!rs.next()){
                        System.out.println("Non ci sono id per " + userId);
                    } else {
                        rs.beforeFirst();
                    }

                    while(rs.next()){
                       countryName = rs.getString(1);
                       languages.add(rs.getString(2));
                    }

                }
            }

            sql = """
                      SELECT country_stats.year, country_stats.population, country_stats.gdp FROM countries
                        JOIN country_stats on countries.country_id = country_stats.country_id
                        WHERE countries.country_id = ?
                        ORDER BY country_stats.year DESC;
                    """;

            long year = 0;
            long population = 0;
            long gdp = 0;

            try(PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
                ps.setInt(1, userId);

                try(ResultSet rs = ps.executeQuery()){

                    if(!rs.next()){
                        System.out.println("Non ci sono id per " + userId);
                    } else {
                        rs.beforeFirst();
                        rs.next();
                        year = rs.getLong(1);
                        population = rs.getLong(2);
                        gdp = rs.getLong(3);
                    }

                }
            }

            Country country = new Country(countryName, languages, year, population, gdp);

            System.out.println("Nome del paese: " + country.getName());
            System.out.println("Lingue parlate: " + country.getLanguages());
            System.out.println("Anno: " + country.getYear());
            System.out.println("Popolazione: " + country.getPopulation());
            System.out.println("GDP: " + country.getGdp());

        } catch (SQLException e){
            e.printStackTrace();
        }


        scan.close();
    }
}
