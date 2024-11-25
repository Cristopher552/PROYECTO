document.addEventListener("DOMContentLoaded", function () {
    const metodoPagoSelect = document.getElementById("metodoPago");
    const pagoTarjeta = document.getElementById("pagoTarjeta");
    const formPago = document.getElementById("formPago");
    const totalPagarElemento = document.getElementById("totalPagar");

    // Elementos adicionales para métodos de pago
    const inputsTarjeta = pagoTarjeta.querySelectorAll("input");
    const metodoPayPal = document.createElement("div");
    metodoPayPal.id = "pagoPayPal";
    metodoPayPal.style.display = "none";
    metodoPayPal.innerHTML = `
        <div class="mb-3">
            <label for="emailPayPal" class="form-label">Correo de PayPal</label>
            <input type="email" class="form-control" id="emailPayPal" placeholder="Ingrese su correo de PayPal" required>
        </div>`;
    formPago.insertBefore(metodoPayPal, formPago.querySelector("hr.my-4:last-of-type"));

    const metodoTransferencia = document.createElement("div");
    metodoTransferencia.id = "pagoTransferencia";
    metodoTransferencia.style.display = "none";
    metodoTransferencia.innerHTML = `
        <div class="mb-3">
            <label for="numeroCuenta" class="form-label">Número de Cuenta</label>
            <input type="text" class="form-control" id="numeroCuenta" placeholder="Ingrese el número de cuenta" required>
        </div>
        <div class="mb-3">
            <label for="nombreBanco" class="form-label">Banco</label>
            <input type="text" class="form-control" id="nombreBanco" placeholder="Ingrese el nombre del banco" required>
        </div>`;
    formPago.insertBefore(metodoTransferencia, formPago.querySelector("hr.my-4:last-of-type"));

    // Función para manejar el cambio de método de pago
    metodoPagoSelect.addEventListener("change", function () {
        const metodo = metodoPagoSelect.value;
        pagoTarjeta.style.display = metodo === "tarjeta" ? "block" : "none";
        metodoPayPal.style.display = metodo === "paypal" ? "block" : "none";
        metodoTransferencia.style.display = metodo === "transferencia" ? "block" : "none";

        // Manejar los campos obligatorios según el método de pago
        actualizarCamposRequeridos(metodo);
    });

    // Función para actualizar los campos requeridos
    function actualizarCamposRequeridos(metodo) {
        // Limpia los campos requeridos de todos los métodos
        inputsTarjeta.forEach(input => input.required = false);
        metodoPayPal.querySelectorAll("input").forEach(input => input.required = false);
        metodoTransferencia.querySelectorAll("input").forEach(input => input.required = false);

        // Activa los requerimientos según el método de pago seleccionado
        if (metodo === "tarjeta") {
            inputsTarjeta.forEach(input => input.required = true);
        } else if (metodo === "paypal") {
            metodoPayPal.querySelectorAll("input").forEach(input => input.required = true);
        } else if (metodo === "transferencia") {
            metodoTransferencia.querySelectorAll("input").forEach(input => input.required = true);
        }
    }

    // Recuperar el total del carrito desde el localStorage
    const totalCarrito = localStorage.getItem("totalCarrito");

    if (totalCarrito) {
        totalPagarElemento.innerText = `$${totalCarrito}`;
    } else {
        totalPagarElemento.innerText = "$0.00"; // Valor por defecto si no se encuentra el total
    }

    // Recuperar los productos del carrito
    const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
    const productosCarrito = document.getElementById("productosCarrito");
    let total = 0;

    carrito.forEach(producto => {
        const row = document.createElement("tr");
        row.innerHTML = 
            `<td>${producto.nombre}</td>
            <td><span class="cantidad">${producto.cantidad}</span></td>
            <td class="precio">$${(producto.precio * producto.cantidad).toFixed(2)}</td>`;
        productosCarrito.appendChild(row);
        total += producto.precio * producto.cantidad;
    });

    totalPagarElemento.textContent = `$${total.toFixed(2)}`;

    // Función para actualizar el total (aunque la cantidad no es editable)
    function actualizarTotal() {
        total = 0;

        carrito.forEach(producto => {
            total += producto.precio * producto.cantidad;
        });

        totalPagarElemento.textContent = `$${total.toFixed(2)}`;
        document.getElementById("total").value = total.toFixed(2);
    }

    // Inicializa el formulario de tarjeta al cargar la página
    metodoPagoSelect.dispatchEvent(new Event("change"));

    // Lógica para manejar el envío del formulario
    formPago.addEventListener("submit", function (event) {
        event.preventDefault(); 
        
        // Captura los productos del carrito
        const nombresProductos = [];
        const cantidades = [];
        const precios = [];

        carrito.forEach(producto => {
            nombresProductos.push(producto.nombre);
            cantidades.push(producto.cantidad);
            precios.push((producto.precio * producto.cantidad).toFixed(2));
        });

        // Asigna los valores a los campos ocultos
        document.getElementById("nombresProductos").value = JSON.stringify(nombresProductos);
        document.getElementById("cantidades").value = JSON.stringify(cantidades);
        document.getElementById("precios").value = JSON.stringify(precios);

        // Envía el formulario
        formPago.submit();
    });

    // Función para mostrar u ocultar el formulario de pago con tarjeta
    function mostrarFormularioTarjeta() {
        if (metodoPagoSelect.value === "tarjeta") {
            pagoTarjeta.style.display = "block";
        } else {
            pagoTarjeta.style.display = "none";
        }
    }

    // Llama a esta función para inicializar el total cuando se carga la página
    actualizarTotal();
});
