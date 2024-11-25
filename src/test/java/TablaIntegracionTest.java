
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TablaIntegracionTest {

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
    public void testEliminarClienteConPedidos() throws Exception {
        int clienteId;
        try {
            String insertClienteSQL = "INSERT INTO clientes (nombre, apellidos, email, telefono, distrito, direccion, dni) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertClienteSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, "Luis");
                stmt.setString(2, "Ramírez");
                stmt.setString(3, "luis.ramirez@example.com");
                stmt.setString(4, "987654321");
                stmt.setString(5, "Surco");
                stmt.setString(6, "Av. Siempre Viva 456");
                stmt.setString(7, "12345678");
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    assertTrue(rs.next());
                    clienteId = rs.getInt(1);
                }
            }

            String insertPedidoSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertPedidoSQL)) {
                stmt.setInt(1, clienteId);
                stmt.setDouble(2, 200.00);
                stmt.setString(3, "tarjeta");
                stmt.executeUpdate();
            }

            String deleteClienteSQL = "DELETE FROM clientes WHERE cliente_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteClienteSQL)) {
                stmt.setInt(1, clienteId);
                int rowsDeleted = stmt.executeUpdate();
                assertEquals(1, rowsDeleted);
            }

            String queryPedidosSQL = "SELECT * FROM pedidos WHERE cliente_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(queryPedidosSQL)) {
                stmt.setInt(1, clienteId);
                try (ResultSet rs = stmt.executeQuery()) {
                    assertFalse(rs.next(), "No se eliminaron los pedidos asociados al cliente.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error en la prueba de eliminación en cascada: " + e.getMessage());
        }
    }

    @Test
    public void testEvitarInsertarDetalleParaPedidoInexistente() throws Exception {
        String insertDetalleSQL = "INSERT INTO detalle_pedido (pedido_id, nombre_producto, cantidad, precio) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertDetalleSQL)) {
            stmt.setInt(1, 9999);  
            stmt.setString(2, "Producto Inválido");
            stmt.setInt(3, 1);
            stmt.setDouble(4, 100.00);
            stmt.executeUpdate();
            fail("Se permitió insertar un detalle de pedido para un pedido inexistente");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("foreign key"), "La restricción de clave foránea no se aplicó correctamente.");
        }
    }

    @Test
    public void testEliminarPedidoConDetalles() throws Exception {
        int clienteId, pedidoId;
        try {
            String insertClienteSQL = "INSERT INTO clientes (nombre, apellidos, email, telefono, distrito, direccion, dni) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertClienteSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, "Carlos");
                stmt.setString(2, "Gómez");
                stmt.setString(3, "carlos.gomez@example.com");
                stmt.setString(4, "12345678");
                stmt.setString(5, "Callao");
                stmt.setString(6, "Jr. Libertad 321");
                stmt.setString(7, "87654321");
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    assertTrue(rs.next());
                    clienteId = rs.getInt(1);
                }
            }

            String insertPedidoSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago) VALUES (?, 100.00, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertPedidoSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, clienteId);
                stmt.setString(2, "transferencia");
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    assertTrue(rs.next());
                    pedidoId = rs.getInt(1);
                }
            }

            String insertDetalleSQL = "INSERT INTO detalle_pedido (pedido_id, nombre_producto, cantidad, precio) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertDetalleSQL)) {
                stmt.setInt(1, pedidoId);
                stmt.setString(2, "Producto A");
                stmt.setInt(3, 2);
                stmt.setDouble(4, 50.00);
                stmt.executeUpdate();
            }

            String deletePedidoSQL = "DELETE FROM pedidos WHERE pedido_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deletePedidoSQL)) {
                stmt.setInt(1, pedidoId);
                stmt.executeUpdate();
            }

            String queryDetalleSQL = "SELECT * FROM detalle_pedido WHERE pedido_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(queryDetalleSQL)) {
                stmt.setInt(1, pedidoId);
                try (ResultSet rs = stmt.executeQuery()) {
                    assertFalse(rs.next(), "No se eliminaron los detalles del pedido.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error en la prueba de eliminación de pedido con detalles: " + e.getMessage());
        }
    }

}
