import java.sql.SQLException;
import java.util.List;

public interface DetallePedidoRepositorioDAO {
    List<DetallePedido> getAllDetallesPedido() throws SQLException;
}