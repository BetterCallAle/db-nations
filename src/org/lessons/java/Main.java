package org.lessons.java;

import java.sql.*;

public class Main {
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USERNAME = System.getenv("DB_USERNAME");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    public static void main(String[] args) {
        try(Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)){
            String sql = """
                    SELECT regions.name, countries.country_id, continents.name, countries.name  FROM regions
                    JOIN continents on regions.continent_id = continents.continent_id
                    JOIN countries on regions.region_id = countries.region_id;
                    """;

            try(PreparedStatement ps = con.prepareStatement(sql)) {
                try(ResultSet rs = ps.executeQuery()){
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
            e.getStackTrace();
        }
    }
}
