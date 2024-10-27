import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class usuarioDAOimplem implements usuarioDAO {

    @Override
    public boolean validarCredenciales(String usuario, String contrasena) {
        boolean esValido = false;

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?")) {
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            esValido = rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return esValido;
    }
}
