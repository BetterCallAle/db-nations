package org.lessons.java;

import java.sql.*;
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
        scan.close();


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

                        System.out.println(regionName + " " + countryId + " " + continentName + " " + countryName);
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
