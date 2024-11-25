import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class usuarioDAOimplem implements usuarioDAO {

    @Override
    public boolean validarCredenciales(String usuario, String contraseña) {
        boolean esValido = false;

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM administradores WHERE usuario = ? AND contraseña = ?")) {
            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);
            ResultSet rs = stmt.executeQuery();

            esValido = rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return esValido;
    }
}
