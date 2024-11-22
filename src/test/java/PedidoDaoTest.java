import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PedidoDaoTest {

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

    private int crearCliente() throws SQLException {
        String insertSQL = "INSERT INTO clientes (nombre, apellidos, email, telefono, distrito, direccion,dni) VALUES (?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Juan");
            stmt.setString(2, "Perez");
            stmt.setString(3, "juan.perez@email.com");
            stmt.setString(4, "123456789");
            stmt.setString(5, "Lima");
            stmt.setString(6, "Av. Siempre Viva 123");
            stmt.setString(7, "12345678");
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); 
                } else {
                    throw new SQLException("No se pudo obtener el ID del cliente.");
                }
            }
        }
    }

    @Test
    public void testCrearPedido() throws Exception {
        try {
            int clienteId = crearCliente();
            
            double total = 150.00;
            String metodoPago = "tarjeta";
            String estado = "Espera";
            LocalDateTime fechaPedido = LocalDateTime.now(); 

            String insertSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago, estado, fecha_pedido) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
                stmt.setInt(1, clienteId);
                stmt.setDouble(2, total);
                stmt.setString(3, metodoPago);
                stmt.setString(4, estado);
                stmt.setObject(5, fechaPedido); 

                int rowsInserted = stmt.executeUpdate();
                assertEquals(1, rowsInserted);

                String querySQL = "SELECT * FROM pedidos WHERE cliente_id = ?";
                try (PreparedStatement queryStmt = connection.prepareStatement(querySQL)) {
                    queryStmt.setInt(1, clienteId);
                    try (ResultSet rs = queryStmt.executeQuery()) {
                        assertTrue(rs.next());
                        assertEquals(clienteId, rs.getInt("cliente_id"));
                        assertEquals(total, rs.getDouble("total"));
                        assertEquals(metodoPago, rs.getString("metodo_pago"));
                        assertEquals(estado, rs.getString("estado"));
                        assertNotNull(rs.getTimestamp("fecha_pedido")); 
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error al crear el pedido: " + e.getMessage());
        }
    }

    @Test
    public void testModificarEstadoPedido() throws Exception {
        try {
            int clienteId = crearCliente();
            
            int pedidoId;
            String estadoInicial = "Espera";
            String estadoNuevo = "Aprobada";

            String insertSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago, estado, fecha_pedido) VALUES (?, ?, 'tarjeta', ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, clienteId);
                stmt.setDouble(2, 100.00);
                stmt.setString(3, estadoInicial);
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    assertTrue(rs.next());
                    pedidoId = rs.getInt(1);
                }
            }

            String updateSQL = "UPDATE pedidos SET estado = ? WHERE pedido_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
                stmt.setString(1, estadoNuevo);
                stmt.setInt(2, pedidoId);

                int rowsUpdated = stmt.executeUpdate();
                assertEquals(1, rowsUpdated);

                String querySQL = "SELECT estado FROM pedidos WHERE pedido_id = ?";
                try (PreparedStatement queryStmt = connection.prepareStatement(querySQL)) {
                    queryStmt.setInt(1, pedidoId);
                    try (ResultSet rs = queryStmt.executeQuery()) {
                        assertTrue(rs.next());
                        assertEquals(estadoNuevo, rs.getString("estado"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error al modificar el estado del pedido: " + e.getMessage());
        }
    }

    @Test
    public void testCalcularTotal() throws Exception {
        try {
            int clienteId = crearCliente();

            double totalEsperado = 200.00;

            String insertSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago, estado, fecha_pedido) VALUES (?, ?, 'tarjeta', 'Espera', CURRENT_TIMESTAMP)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
                stmt.setInt(1, clienteId);
                stmt.setDouble(2, totalEsperado);
                stmt.executeUpdate();
            }

            String querySQL = "SELECT SUM(total) AS total_calculado FROM pedidos WHERE cliente_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(querySQL)) {
                stmt.setInt(1, clienteId);

                try (ResultSet rs = stmt.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals(totalEsperado, rs.getDouble("total_calculado"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error al calcular el total del pedido: " + e.getMessage());
        }
    }

    @Test
    public void testEliminarPedido() throws Exception {
        try {
           
            int clienteId = crearCliente();

            int pedidoId;

            String insertSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago, estado, fecha_pedido) VALUES (?, ?, 'tarjeta', 'Espera', CURRENT_TIMESTAMP)";
            try (PreparedStatement stmt = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, clienteId);
                stmt.setDouble(2, 50.00);
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    assertTrue(rs.next());
                    pedidoId = rs.getInt(1);
                }
            }

            String deleteSQL = "DELETE FROM pedidos WHERE pedido_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteSQL)) {
                stmt.setInt(1, pedidoId);

                int rowsDeleted = stmt.executeUpdate();
                assertEquals(1, rowsDeleted);

                String querySQL = "SELECT * FROM pedidos WHERE pedido_id = ?";
                try (PreparedStatement queryStmt = connection.prepareStatement(querySQL)) {
                    queryStmt.setInt(1, pedidoId);

                    try (ResultSet rs = queryStmt.executeQuery()) {
                        assertFalse(rs.next());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error al eliminar el pedido: " + e.getMessage());
        }
    }
}
