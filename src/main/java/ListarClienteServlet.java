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

    /**
     * Constructor del servlet. Inicializa el repositorio de clientes 
     * utilizando una implementación de ClienteRepositorioDAO.
     */
    public ListarClienteServlet() {
        this.clienteRepositorio = new ClienteRepositorioImplem(); // Opción simple de inyección manual
    }

    /**
     * Maneja las solicitudes GET para listar los clientes.
     *
     * @param request la solicitud HTTP recibida.
     * @param response la respuesta HTTP que se enviará al cliente.
     * @throws ServletException si ocurre un error durante la ejecución del servlet.
     * @throws IOException si ocurre un error de entrada/salida al procesar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtiene la lista de clientes desde el repositorio
            List<Cliente> clientes = clienteRepositorio.getAllClientes();
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String json = gson.toJson(clientes);
            out.print(json);
            out.flush();

        } catch (Exception e) {
            // Manejo de errores y respuesta con estado 500
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar los clientes");
        }
    }
}
