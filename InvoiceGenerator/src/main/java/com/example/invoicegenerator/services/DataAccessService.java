package com.example.invoicegenerator.services;

import org.springframework.stereotype.Repository;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataAccessService{

    public final String connStr = "jdbc:postgresql://localhost:5432/amigoscode?user=amigoscode&password=password";

    public Integer[] getStationIds(int customerId){
        try
        {
            Connection conn = DriverManager.getConnection(connStr);
            String query = "Select chargingstation_id from chargingstationdata where customer_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,customerId);
            ResultSet rs = preparedStatement.executeQuery();

            List rowValues = new ArrayList();
            while (rs.next()){
                rowValues.add(rs.getInt(1));
            }
            Integer[] stationIds = (Integer[]) rowValues.toArray(new Integer[rowValues.size()]);
            return stationIds;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResultSet getStationData(int customerId, int stationId){
        try
        {
            Connection conn = DriverManager.getConnection(connStr);
            String query = "Select * from chargingstationdata d inner join kwhprice p " +
                            "on d.kwhprice_id = p.id where customer_id = ? and chargingstation_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,customerId);
            preparedStatement.setInt(2,stationId);
            ResultSet rs = preparedStatement.executeQuery();
            return rs;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
