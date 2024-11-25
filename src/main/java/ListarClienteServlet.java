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
 * El servlet ListarClienteServlet maneja las solicitudes para listar todos los clientes.
 * Este servlet responde a solicitudes GET y devuelve la lista de clientes en formato JSON.
 */
@WebServlet("/listarClientes")
public class ListarClienteServlet extends HttpServlet {

    private ClienteRepositorioDAO clienteRepositorio;

    public ListarClienteServlet() {
        this.clienteRepositorio = new ClienteRepositorioImplem(); 
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Cliente> clientes = clienteRepositorio.getAllClientes();
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String json = gson.toJson(clientes);
            out.print(json);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar los clientes");
        }
    }
}
