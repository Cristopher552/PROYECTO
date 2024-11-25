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
    
    public ListarProductosServlet() {
        this.detallePedidoRepositorio = new DetallePedidoRepositorioImplem(); 
    }


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

