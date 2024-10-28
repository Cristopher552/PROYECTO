
import java.sql.SQLException;
import java.util.List;

public interface ClienteRepositorio {
    List<Cliente> getAllClientes() throws SQLException;
}
