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

    @Override
    public void init() throws ServletException {
        usuarioDAO = new usuarioDAOimplem(); 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("password");

        if (usuarioDAO.validarCredenciales(usuario, contrasena)) {
            response.sendRedirect("admin.html");
        } else {
            response.sendRedirect("login.html?error=1");
        }
    }
}

