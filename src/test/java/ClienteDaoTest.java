import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDaoTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = conexionBD.getConnection();
        connection.setAutoCommit(false);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.rollback();
            connection.close();
        }
    }

    @Test
    public void testInsertarCliente() throws Exception {
        try {
            String dni = "12345678";
            String nombre = "Juan";
            String apellidos = "Perez";
            String email = "juan.perez@example.com";
            String telefono = "123456789";
            String distrito = "Centro";
            String direccion = "Calle 123";

            String insertSQL = "INSERT INTO clientes (dni, nombre, apellidos, email, telefono, distrito, direccion) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
                stmt.setString(1, dni);
                stmt.setString(2, nombre);
                stmt.setString(3, apellidos);
                stmt.setString(4, email);
                stmt.setString(5, telefono);
                stmt.setString(6, distrito);
                stmt.setString(7, direccion);

                int rowsInserted = stmt.executeUpdate();
                assertEquals(1, rowsInserted);

                String querySQL = "SELECT * FROM clientes WHERE dni = ?";
                try (PreparedStatement queryStmt = connection.prepareStatement(querySQL)) {
                    queryStmt.setString(1, dni);
                    try (ResultSet rs = queryStmt.executeQuery()) {
                        assertTrue(rs.next());
                        assertEquals(nombre, rs.getString("nombre"));
                        assertEquals(apellidos, rs.getString("apellidos"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error en la base de datos: " + e.getMessage());
        }
    }

    @Test
    public void testValidarDNI() throws Exception {
        try {
            String dni = "87654321";

            assertTrue(dni.matches("\\d{8}"), "El DNI debe ser un número de 8 dígitos");

            String insertSQL = "INSERT INTO clientes (dni, nombre, apellidos, email, telefono, distrito, direccion) VALUES (?, 'Prueba', 'Prueba', 'test@example.com', '123456789', 'Distrito', 'Dirección')";
            try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
                stmt.setString(1, dni);
                stmt.executeUpdate();

                String querySQL = "SELECT * FROM clientes WHERE dni = ?";
                try (PreparedStatement queryStmt = connection.prepareStatement(querySQL)) {
                    queryStmt.setString(1, dni);
                    try (ResultSet rs = queryStmt.executeQuery()) {
                        assertTrue(rs.next());
                        assertEquals(dni, rs.getString("dni"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error en la validación del DNI: " + e.getMessage());
        }
    }

    @Test
    public void testValidarTelefono() throws Exception {
        try {
            String dni = "56789012";
            String telefono = "987654321";
            String insertSQL = "INSERT INTO clientes (dni, telefono, nombre, apellidos, email, distrito, direccion) VALUES (?, ?, 'Prueba', 'Prueba', 'test@example.com', 'Distrito', 'Dirección')";
            try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
                stmt.setString(1, dni);
                stmt.setString(2, telefono);
                stmt.executeUpdate();

                String querySQL = "SELECT * FROM clientes WHERE dni = ?";
                try (PreparedStatement queryStmt = connection.prepareStatement(querySQL)) {
                    queryStmt.setString(1, dni);
                    try (ResultSet rs = queryStmt.executeQuery()) {
                        assertTrue(rs.next());
                        assertEquals(telefono, rs.getString("telefono"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error en la validación del teléfono: " + e.getMessage());
        }
    }

    @Test
    public void testValidarEmail() throws Exception {
        try {
            String dni = "23456789";
            String email = "correo.valido@example.com";
            String insertSQL = "INSERT INTO clientes (dni, email, nombre, apellidos, telefono, distrito, direccion) VALUES (?, ?, 'Prueba', 'Prueba', '123456789', 'Distrito', 'Dirección')";
            try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
                stmt.setString(1, dni);
                stmt.setString(2, email);
                stmt.executeUpdate();

                String querySQL = "SELECT * FROM clientes WHERE dni = ?";
                try (PreparedStatement queryStmt = connection.prepareStatement(querySQL)) {
                    queryStmt.setString(1, dni);
                    try (ResultSet rs = queryStmt.executeQuery()) {
                        assertTrue(rs.next());
                        assertEquals(email, rs.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error en la validación del email: " + e.getMessage());
        }
    }

    @Test
    public void testEliminarCliente() throws Exception {
        try {
            String dni = "34567890";
            String email = "eliminar.cliente@example.com";
            String insertSQL = "INSERT INTO clientes (dni, email, nombre, apellidos, telefono, distrito, direccion) VALUES (?, ?, 'Eliminar', 'Cliente', '123456789', 'Distrito', 'Dirección')";
            try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
                stmt.setString(1, dni);
                stmt.setString(2, email);
                stmt.executeUpdate();

                String deleteSQL = "DELETE FROM clientes WHERE dni = ?";
                try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
                    deleteStmt.setString(1, dni);
                    int rowsDeleted = deleteStmt.executeUpdate();
                    assertEquals(1, rowsDeleted);

                    String querySQL = "SELECT * FROM clientes WHERE dni = ?";
                    try (PreparedStatement queryStmt = connection.prepareStatement(querySQL)) {
                        queryStmt.setString(1, dni);
                        try (ResultSet rs = queryStmt.executeQuery()) {
                            assertFalse(rs.next());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error al eliminar el cliente: " + e.getMessage());
        }
    }
}
