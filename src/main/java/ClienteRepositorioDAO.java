
import java.sql.SQLException;
import java.util.List;

public interface ClienteRepositorioDAO {
    List<Cliente> getAllClientes() throws SQLException;
}
