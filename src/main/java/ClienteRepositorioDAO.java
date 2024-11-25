
import java.sql.SQLException;
import java.util.List;

/**
 * ClienteRepositorioDAO es una interfaz que define el contrato para las operaciones
 * relacionadas con la entidad Cliente en el repositorio de datos.
 */
public interface ClienteRepositorioDAO {

    List<Cliente> getAllClientes() throws SQLException;
}
