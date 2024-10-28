import java.sql.SQLException;
import java.util.List;

public interface PedidoRepositorioDAO {
    List<Pedido> getAllPedidos() throws SQLException;
}
