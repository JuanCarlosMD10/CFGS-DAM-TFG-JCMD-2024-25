// Espera a que el contenido del DOM esté completamente cargado antes de ejecutar el código
document.addEventListener("DOMContentLoaded", () => {
    // Extrae los parámetros de la URL (query string)
    const params = new URLSearchParams(window.location.search);

    // Obtiene los valores de los parámetros individuales
    const ausenciaId = params.get("id");
    const fecha = params.get("fecha");
    const motivo = params.get("motivo");
    const horaInicio = params.get("horaInicio");
    const horaFin = params.get("horaFin");
    const comentarioTareas = params.get("comentarioTareas");

    // Define el orden lógico de las horas para las validaciones (de primera a sexta hora)
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Referencias a los elementos del formulario para interactuar con ellos
    const fechaInput = document.getElementById("date");
    const motivoInput = document.getElementById("reason");
    const horaInicioSelect = document.getElementById("horaInicio");
    const horaFinSelect = document.getElementById("horaFin");
    const comentarioTareasInput = document.getElementById("comentarioTareas");
    const form = document.querySelector(".absence-form");
    const successMessage = document.getElementById("success-message");

    // Establece la fecha mínima permitida en el selector de fecha como la fecha actual
    const hoy = new Date();
    fechaInput.min = hoy.toISOString().split('T')[0];

    // Rellena los campos del formulario con los valores extraídos de la URL (si existen)
    if (fecha) fechaInput.value = fecha;
    if (motivo) motivoInput.value = motivo;
    if (horaInicio) horaInicioSelect.value = horaInicio.toUpperCase();
    if (horaFin) horaFinSelect.value = horaFin.toUpperCase();
    if (comentarioTareas) comentarioTareasInput.value = comentarioTareas;

    // Función para actualizar las opciones del selector de horaFin según la horaInicio seleccionada
    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicioSelect.value;
        const indexInicio = horas.indexOf(inicioSeleccionado);

        // Recorre todas las opciones de horaFin y deshabilita las que sean anteriores a la horaInicio
        Array.from(horaFinSelect.options).forEach(option => {
            const indexOption = horas.indexOf(option.value);
            option.disabled = indexOption < indexInicio;
        });

        // Si la horaFin actualmente seleccionada ya no es válida, la resetea (vacía)
        if (horaFinSelect.value && horas.indexOf(horaFinSelect.value) < indexInicio) {
            horaFinSelect.value = '';
        }
    }

    // Ejecuta la función para actualizar las horas fin cuando cambia la hora inicio
    horaInicioSelect.addEventListener("change", actualizarHorasFin);
    // Llama a la función al cargar para asegurar que las opciones estén correctamente configuradas
    actualizarHorasFin();

    // Maneja el evento de envío del formulario
    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // Previene el envío estándar para manejarlo con JavaScript

        // Valida que la hora fin no sea anterior a la hora inicio usando los índices en el array 'horas'
        const indexInicio = horas.indexOf(horaInicioSelect.value);
        const indexFin = horas.indexOf(horaFinSelect.value);

        if (indexInicio > indexFin) {
            alert("La hora final no puede ser anterior a la de inicio.");
            return; // Detiene el envío si la validación falla
        }

        // Construye el objeto con los datos que se enviarán al backend para actualizar la ausencia
        const payload = {
            fecha: fechaInput.value,
            motivo: motivoInput.value,
            horaInicio: horaInicioSelect.value,
            horaFin: horaFinSelect.value,
            tareaAlumnado: comentarioTareasInput.value
        };

        try {
            // Hace la petición PUT para actualizar la ausencia con el ID correspondiente
            const response = await fetch(`http://localhost:8080/ausencias/${ausenciaId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            // Si la respuesta no es exitosa, extrae el mensaje de error y lanza excepción
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Error al actualizar la ausencia");
            }

            // Muestra un mensaje de éxito en la interfaz
            successMessage.style.display = "block";

            // Después de 2 segundos oculta el mensaje y redirige a la página principal de ausencias
            setTimeout(() => {
                successMessage.style.display = "none";
                window.location.href = "index-ausencia-guardia.html";
            }, 2000);

        } catch (error) {
            // Muestra un alert con el error y también lo muestra en consola para debugging
            alert(error.message);
            console.error("Detalle:", error);
        }
    });
});