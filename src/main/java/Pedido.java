import java.math.BigDecimal;
import java.sql.Date;

public class Pedido {

    private int pedido_id;  // Cambio de 'id' a 'pedido_id'
    private int cliente_id;  // Cambio de 'clienteId' a 'cliente_id'
    private BigDecimal total;
    private String metodoPago;
    private String estado;
    private Date fecha;

    // Getter y Setter para 'pedido_id'
    public int getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(int pedido_id) {
        this.pedido_id = pedido_id;
    }

    // Getter y Setter para 'cliente_id'
    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}