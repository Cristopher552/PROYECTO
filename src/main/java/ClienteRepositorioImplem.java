import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositorioImplem implements ClienteRepositorioDAO {

    @Override
    public List<Cliente> getAllClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT cliente_id, nombre, apellidos, dni, email, telefono, distrito, direccion FROM clientes";
        
        try (Connection connection = conexionBD.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setCliente_id(rs.getInt("cliente_id"));  // Cambiado de setId a setCliente_id
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setDni(rs.getString("dni"));  
                cliente.setEmail(rs.getString("email"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setDistrito(rs.getString("distrito"));
                cliente.setDireccion(rs.getString("direccion"));
                clientes.add(cliente);
            }
        }
        return clientes;
    }
}


