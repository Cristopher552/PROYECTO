import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;

/**
 * Servlet que maneja las solicitudes para listar todos los detalles de productos en pedidos.
 * Este servlet responde a las solicitudes GET y devuelve la lista de detalles de productos en formato JSON.
 */
@WebServlet("/listarDetallesProductos")
public class ListarProductosServlet extends HttpServlet {

    private DetallePedidoRepositorioDAO detallePedidoRepositorio;

    /**
     * Constructor del servlet que inicializa el repositorio de detalles de pedidos.
     * Se utiliza una implementación de DetallePedidoRepositorioDAO.
     */
    public ListarProductosServlet() {
        this.detallePedidoRepositorio = new DetallePedidoRepositorioImplem(); // Opción simple de inyección manual
    }

    /**
     * Maneja las solicitudes GET para obtener la lista de detalles de productos.
     *
     * @param request la solicitud HTTP recibida.
     * @param response la respuesta HTTP que se enviará al cliente.
     * @throws ServletException si ocurre un error durante la ejecución del servlet.
     * @throws IOException si ocurre un error de entrada/salida al procesar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<DetallePedido> detalles = detallePedidoRepositorio.getAllDetallesPedido();
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String json = gson.toJson(detalles);
            out.print(json);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar los detalles de los productos");
        }
    }
}

