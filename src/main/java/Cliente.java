/**
 * Clase que representa a un cliente con información personal y de contacto.
 */
public class Cliente {

    private int id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String distrito;
    private String direccion;

    /**
     * Obtiene el identificador único del cliente.
     * 
     * @return el ID del cliente.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del cliente.
     * 
     * @param id el ID del cliente.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del cliente.
     * 
     * @return el nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     * 
     * @param nombre el nombre del cliente.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del cliente.
     * 
     * @return los apellidos del cliente.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del cliente.
     * 
     * @param apellidos los apellidos del cliente.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el correo electrónico del cliente.
     * 
     * @return el email del cliente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del cliente.
     * 
     * @param email el email del cliente.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     * 
     * @return el teléfono del cliente.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del cliente.
     * 
     * @param telefono el teléfono del cliente.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el distrito del cliente.
     * 
     * @return el distrito del cliente.
     */
    public String getDistrito() {
        return distrito;
    }

    /**
     * Establece el distrito del cliente.
     * 
     * @param distrito el distrito del cliente.
     */
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    /**
     * Obtiene la dirección del cliente.
     * 
     * @return la dirección del cliente.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del cliente.
     * 
     * @param direccion la dirección del cliente.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
