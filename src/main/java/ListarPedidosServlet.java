import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/listarPedidos")
public class ListarPedidosServlet extends HttpServlet {

    private PedidoRepositorioDAO pedidoRepositorio;

    public ListarPedidosServlet() {
        this.pedidoRepositorio = new PedidoRepositorioImplem(); 
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Pedido> pedidos = pedidoRepositorio.getAllPedidos();
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String json = gson.toJson(pedidos);
            out.print(json);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar los pedidos");
        }
    }
}
