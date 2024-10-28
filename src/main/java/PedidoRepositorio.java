import java.sql.SQLException;
import java.util.List;

public interface PedidoRepositorio {
    List<Pedido> getAllPedidos() throws SQLException;
}
