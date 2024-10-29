import java.sql.Date;

/**
 * Clase que representa un pedido en el sistema.
 * Contiene información sobre el pedido, incluyendo el ID del pedido,
 * el ID del cliente, el total del pedido, el método de pago y la fecha.
 */
public class Pedido {

    private int id;
    private int clienteId;
    private String total;
    private String metodoPago;
    private Date fecha;

    /**
     * Obtiene el ID del pedido.
     *
     * @return el ID del pedido.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del pedido.
     *
     * @param id el ID del pedido a establecer.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el ID del cliente asociado al pedido.
     *
     * @return el ID del cliente.
     */
    public int getClienteId() {
        return clienteId;
    }

    /**
     * Establece el ID del cliente asociado al pedido.
     *
     * @param clienteId el ID del cliente a establecer.
     */
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    /**
     * Obtiene el total del pedido.
     *
     * @return el total del pedido como cadena.
     */
    public String getTotal() {
        return total;
    }

    /**
     * Establece el total del pedido.
     *
     * @param total el total del pedido a establecer.
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * Obtiene el método de pago utilizado para el pedido.
     *
     * @return el método de pago.
     */
    public String getMetodoPago() {
        return metodoPago;
    }

    /**
     * Establece el método de pago utilizado para el pedido.
     *
     * @param metodoPago el método de pago a establecer.
     */
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    /**
     * Obtiene la fecha en que se realizó el pedido.
     *
     * @return la fecha del pedido.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha en que se realizó el pedido.
     *
     * @param fecha la fecha del pedido a establecer.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
