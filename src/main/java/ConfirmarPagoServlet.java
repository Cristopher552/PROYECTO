
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.google.gson.Gson;
import com.google.common.collect.ImmutableList;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import java.time.LocalDate;
import java.util.Arrays;
import javax.swing.text.StyleConstants.FontConstants;

@WebServlet("/confirmarPago")

public class ConfirmarPagoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        PreparedStatement stmtAutoIncrementClientes = null;
        PreparedStatement stmtAutoIncrementPedidos = null;
        PreparedStatement stmtAutoIncrementDetalles = null;
        PreparedStatement stmtCliente = null;
        PreparedStatement stmtPedido = null;
        PreparedStatement stmtDetalle = null;
        ResultSet generatedKeys = null;

        try {
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false);

            // Reiniciar los auto-incrementos
            stmtAutoIncrementClientes = connection.prepareStatement("ALTER TABLE clientes AUTO_INCREMENT = 1");
            stmtAutoIncrementClientes.executeUpdate();
            stmtAutoIncrementPedidos = connection.prepareStatement("ALTER TABLE pedidos AUTO_INCREMENT = 1");
            stmtAutoIncrementPedidos.executeUpdate();
            stmtAutoIncrementDetalles = connection.prepareStatement("ALTER TABLE detalle_pedido AUTO_INCREMENT = 1");
            stmtAutoIncrementDetalles.executeUpdate();

            // Recuperar datos del formulario
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String dni = request.getParameter("dni");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String distrito = request.getParameter("distrito");
            String direccion = request.getParameter("direccion");
            String metodoPago = request.getParameter("metodoPago");
            String totalAPagar = request.getParameter("total");

            // Insertar cliente
            String insertClienteSQL = "INSERT INTO clientes (nombre, apellidos, dni, email, telefono, distrito, direccion) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmtCliente = connection.prepareStatement(insertClienteSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtCliente.setString(1, nombre);
            stmtCliente.setString(2, apellidos);
            stmtCliente.setString(3, dni);
            stmtCliente.setString(4, email);
            stmtCliente.setString(5, telefono);
            stmtCliente.setString(6, distrito);
            stmtCliente.setString(7, direccion);
            stmtCliente.executeUpdate();

            generatedKeys = stmtCliente.getGeneratedKeys();
            int clienteId = -1;
            if (generatedKeys.next()) {
                clienteId = generatedKeys.getInt(1);
            }

            // Insertar pedido
            String insertPedidoSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago, estado) VALUES (?, ?, ?, 'Pendiente')";
            stmtPedido = connection.prepareStatement(insertPedidoSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtPedido.setInt(1, clienteId);
            stmtPedido.setString(2, totalAPagar);
            stmtPedido.setString(3, metodoPago);
            stmtPedido.executeUpdate();

            generatedKeys = stmtPedido.getGeneratedKeys();
            int pedidoId = -1;
            if (generatedKeys.next()) {
                pedidoId = generatedKeys.getInt(1);
            }

            // Insertar detalles del pedido
            Gson gson = new Gson();
            String[] nombresProductos = gson.fromJson(request.getParameter("nombresProductos"), String[].class);
            int[] cantidades = gson.fromJson(request.getParameter("cantidades"), int[].class);
            String[] preciosArray = gson.fromJson(request.getParameter("precios"), String[].class);

            String insertDetalleSQL = "INSERT INTO detalle_pedido (pedido_id, nombre_producto, cantidad, precio) VALUES (?, ?, ?, ?)";
            stmtDetalle = connection.prepareStatement(insertDetalleSQL);
            for (int i = 0; i < nombresProductos.length; i++) {
                stmtDetalle.setInt(1, pedidoId);
                stmtDetalle.setString(2, nombresProductos[i]);
                stmtDetalle.setInt(3, cantidades[i]);
                stmtDetalle.setString(4, preciosArray[i]);
                stmtDetalle.addBatch();
            }
            stmtDetalle.executeBatch();

            connection.commit();

            // Generar factura en formato PDF
            generarFacturaPDF(pedidoId,dni,nombre, email, totalAPagar, response);

            // Confirmar la transacción
            connection.commit();
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la compra.");
        } finally {
            // Cerrar todos los recursos
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (stmtCliente != null) {
                    stmtCliente.close();
                }
                if (stmtPedido != null) {
                    stmtPedido.close();
                }
                if (stmtDetalle != null) {
                    stmtDetalle.close();
                }
                if (stmtAutoIncrementClientes != null) {
                    stmtAutoIncrementClientes.close();
                }
                if (stmtAutoIncrementPedidos != null) {
                    stmtAutoIncrementPedidos.close();
                }
                if (stmtAutoIncrementDetalles != null) {
                    stmtAutoIncrementDetalles.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void generarFacturaPDF(int pedidoId, String dni, String nombre, String email, String totalAPagar, HttpServletResponse response) throws IOException {
    // Configuración de respuesta para indicar que será un PDF
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=factura_" + pedidoId + ".pdf");

    try (PdfWriter writer = new PdfWriter(response.getOutputStream()); PdfDocument pdf = new PdfDocument(writer); Document document = new Document(pdf)) {

        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
       
        document.add(new Paragraph("Factura de Compra")
                .setFont(boldFont).setFontSize(20).setTextAlignment(TextAlignment.CENTER));
        
        document.add(new Paragraph("Tienda Shonos")
                .setFont(boldFont).setFontSize(16).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Dirección: Jiron Hipolito Unanue,La victoria 15018")
                .setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Email: shonos12@gmail.com")
                .setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("------------------------------------------------------------")
                .setFontSize(12).setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Número de Pedido: " + pedidoId)
                .setFontSize(14).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("Fecha de Emisión: " + LocalDate.now())
                .setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("------------------------------------------------------------")
                .setFontSize(12).setTextAlignment(TextAlignment.LEFT));

        // Información del cliente
        document.add(new Paragraph("Cliente:")
                .setFont(boldFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("Nombre: " + nombre)
                .setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("DNI: " + dni)
                .setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("Email: " + email)
                .setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("------------------------------------------------------------")
                .setFontSize(12).setTextAlignment(TextAlignment.LEFT));

        document.add(new Paragraph("Pedido:")
                .setFont(boldFont).setFontSize(14).setTextAlignment(TextAlignment.LEFT));
        Table table = new Table(4);
        table.addHeaderCell("Producto");
        table.addHeaderCell("Cantidad");
        table.addHeaderCell("Precio Unitario");
        table.addHeaderCell("Total");

        // Consultar los detalles del pedido desde la base de datos
        try (Connection connection = conexionBD.getConnection(); PreparedStatement stmtDetalle = connection.prepareStatement("SELECT nombre_producto, cantidad, precio FROM detalle_pedido WHERE pedido_id = ?")) {
            stmtDetalle.setInt(1, pedidoId);
            try (ResultSet rs = stmtDetalle.executeQuery()) {
                boolean hayDetalles = false;
                double totalPedido = 0.0;
                while (rs.next()) {
                    hayDetalles = true;
                    String producto = rs.getString("nombre_producto");
                    int cantidad = rs.getInt("cantidad");
                    double precioTotal = rs.getDouble("precio");

                    // Calcular el precio unitario
                    double precioUnitario = precioTotal / cantidad;

                    // Calcular el total del producto (precio unitario * cantidad)
                    double totalProducto = precioUnitario * cantidad;

                    // Añadir una fila por cada producto
                    table.addCell(producto);
                    table.addCell(String.valueOf(cantidad));
                    table.addCell("$" + String.format("%.2f", precioUnitario));  // Precio unitario
                    table.addCell("$" + String.format("%.2f", totalProducto));  // Total (precio unitario * cantidad)

                    totalPedido += totalProducto;
                }

                if (!hayDetalles) {
                    table.addCell("No hay productos");
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Agregar la tabla al documento PDF
        document.add(table);

        // Resumen del pago
        document.add(new Paragraph("------------------------------------------------------------")
                .setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("Subtotal: $" + String.format("%.2f", Double.parseDouble(totalAPagar)))
                .setFontSize(12).setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("IVA (18%): $" + String.format("%.2f", Double.parseDouble(totalAPagar) * 0.18))
                .setFontSize(12).setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("Total a Pagar: $" + totalAPagar)
                .setFont(boldFont).setFontSize(14).setTextAlignment(TextAlignment.RIGHT));

        // Pie de página con detalles de la tienda
        document.add(new Paragraph("Gracias por tu compra. Esperamos verte pronto.")
                .setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("------------------------------------------------------------")
                .setFontSize(12).setTextAlignment(TextAlignment.CENTER));
    }
}


}
