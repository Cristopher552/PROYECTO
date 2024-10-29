import java.math.BigDecimal;

/**
 * La clase DetallePedido representa un detalle de un pedido específico en una tienda en línea.
 * Contiene información sobre el identificador del pedido, el nombre del producto, la cantidad solicitada
 * y el precio del producto.
 */
public class DetallePedido {

    /**
     * Identificador único del pedido al que pertenece el detalle.
     */
    private int pedidoId;

    /**
     * Nombre del producto incluido en el pedido.
     */
    private String nombreProducto;

    /**
     * Cantidad de unidades del producto solicitadas en el pedido.
     */
    private int cantidad;

    /**
     * Precio unitario del producto en el pedido.
     */
    private BigDecimal precio;

    /**
     * Obtiene el identificador del pedido.
     *
     * @return el identificador del pedido.
     */
    public int getPedidoId() {
        return pedidoId;
    }

    /**
     * Establece el identificador del pedido.
     *
     * @param pedidoId el identificador del pedido a establecer.
     */
    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    /**
     * Obtiene el nombre del producto en el detalle del pedido.
     *
     * @return el nombre del producto.
     */
    public String getNombreProducto() {
        return nombreProducto;
    }

    /**
     * Establece el nombre del producto en el detalle del pedido.
     *
     * @param nombreProducto el nombre del producto a establecer.
     */
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    /**
     * Obtiene la cantidad de unidades del producto en el pedido.
     *
     * @return la cantidad de unidades del producto.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de unidades del producto en el pedido.
     *
     * @param cantidad la cantidad de unidades del producto a establecer.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario del producto en el pedido.
     *
     * @return el precio del producto.
     */
    public BigDecimal getPrecio() {
        return precio;
    }

    /**
     * Establece el precio unitario del producto en el pedido.
     *
     * @param precio el precio del producto a establecer.
     */
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
