import java.sql.SQLException;
import java.util.List;

public interface DetallePedidoRepositorio {
    List<DetallePedido> getAllDetallesPedido() throws SQLException;
}