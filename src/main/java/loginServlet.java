import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet para gestionar el inicio de sesión de usuarios.
 * Valida las credenciales ingresadas y redirige al usuario según el resultado.
 */
@WebServlet("/login")
public class loginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(loginServlet.class);
    private usuarioDAO usuarioDAO; 

    /**
     * Inicializa el servlet y configura la instancia de {@link usuarioDAO} para validar credenciales.
     * 
     * @throws ServletException si ocurre un error en la inicialización.
     */
    @Override
    public void init() throws ServletException {
        usuarioDAO = new usuarioDAOimplem(); 
    }

    /**
     * Procesa la solicitud POST para el inicio de sesión.
     * Verifica las credenciales ingresadas y redirige al usuario a la página correspondiente.
     * 
     * @param request el objeto {@link HttpServletRequest} que contiene los datos de la solicitud.
     * @param response el objeto {@link HttpServletResponse} que contiene la respuesta que se enviará al cliente.
     * @throws ServletException si ocurre un error en el procesamiento de la solicitud.
     * @throws IOException si ocurre un error de entrada/salida durante el redireccionamiento.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("password");

        if (usuarioDAO.validarCredenciales(usuario, contrasena)) {
            // Credenciales válidas: redirige a la página de administración
            response.sendRedirect("admin.html");
        } else {
            // Credenciales inválidas: redirige a la página de inicio de sesión con un mensaje de error
            response.sendRedirect("login.html?error=1");
        }
    }
}

