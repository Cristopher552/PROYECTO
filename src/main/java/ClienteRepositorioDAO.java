
import java.sql.SQLException;
import java.util.List;

/**
 * ClienteRepositorioDAO es una interfaz que define el contrato para las operaciones
 * relacionadas con la entidad Cliente en el repositorio de datos.
 */
public interface ClienteRepositorioDAO {

    /**
     * Obtiene una lista de todos los clientes en el repositorio.
     *
     * @return una lista de objetos Cliente que representan todos los clientes.
     * @throws SQLException si ocurre algún error al acceder a la base de datos.
     */
    List<Cliente> getAllClientes() throws SQLException;
}
