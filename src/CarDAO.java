import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level; // Logger için gerekli
import java.util.logging.Logger; // Logger için gerekli

public class CarDAO {

    // Uyarıyı gidermek için Logger tanımladık
    private static final Logger LOGGER = Logger.getLogger(CarDAO.class.getName());

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=smartdrive_db;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "GucluBirSifre123!";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void addCar(Car car) {
        String sql = "INSERT INTO cars (brand, model, year, price_per_day) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setDouble(4, car.getPricePerDay());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            // printStackTrace yerine Logger kullandık
            LOGGER.log(Level.SEVERE, "Add up error", e);
        }
    }

    public List<String> getAllCars() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM cars";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String row = "ID: " + rs.getInt("id") + " | " +
                             rs.getString("brand") + " " + 
                             rs.getString("model") + " (" + 
                             rs.getInt("year") + ") - " + 
                             rs.getDouble("price_per_day") + " $";
                list.add(row);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Listing Error", e);
        }
        return list;
    }

    public List<String> searchCars(String keyword) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM cars WHERE brand LIKE ?"; 
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String row = "ID: " + rs.getInt("id") + " | " +
                             rs.getString("brand") + " " + 
                             rs.getString("model") + " (" + 
                             rs.getInt("year") + ") - " + 
                             rs.getDouble("price_per_day") + " $";
                list.add(row);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Searching Error", e);
        }
        return list;
    }

    public void deleteCar(int id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Deleting Error", e);
        }
    }

    public void updateCar(Car car, int id) {
        String sql = "UPDATE cars SET brand=?, model=?, year=?, price_per_day=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setDouble(4, car.getPricePerDay());
            stmt.setInt(5, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Update Error", e);
        }
    }
}