// Espera a que el contenido del DOM esté completamente cargado antes de ejecutar el código
document.addEventListener("DOMContentLoaded", () => {
    // Extrae los parámetros de la URL
    const params = new URLSearchParams(window.location.search);

    // Obtiene los valores de los parámetros
    const ausenciaId = params.get("id");
    const fecha = params.get("fecha");
    const motivo = params.get("motivo");
    const horaInicio = params.get("horaInicio");
    const horaFin = params.get("horaFin");
    const comentarioTareas = params.get("comentarioTareas");

    // Define el orden de las horas para validaciones posteriores
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Referencias a los elementos del formulario
    const fechaInput = document.getElementById("date");
    const motivoInput = document.getElementById("reason");
    const horaInicioSelect = document.getElementById("horaInicio");
    const horaFinSelect = document.getElementById("horaFin");
    const comentarioTareasInput = document.getElementById("comentarioTareas");
    const form = document.querySelector(".absence-form");
    const successMessage = document.getElementById("success-message");

    // Establece la fecha mínima permitida como la fecha actual
    const hoy = new Date();
    fechaInput.min = hoy.toISOString().split('T')[0];

    // Rellena los campos del formulario con los valores extraídos de la URL
    if (fecha) fechaInput.value = fecha;
    if (motivo) motivoInput.value = motivo;
    if (horaInicio) horaInicioSelect.value = horaInicio.toUpperCase();
    if (horaFin) horaFinSelect.value = horaFin.toUpperCase();
    if (comentarioTareas) comentarioTareasInput.value = comentarioTareas;

    // Función para actualizar las opciones del selector de hora de fin según la hora de inicio
    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicioSelect.value;
        const indexInicio = horas.indexOf(inicioSeleccionado);

        // Deshabilita las opciones de hora de fin que sean anteriores a la hora de inicio
        Array.from(horaFinSelect.options).forEach(option => {
            const indexOption = horas.indexOf(option.value);
            option.disabled = indexOption < indexInicio;
        });

        // Si la hora de fin ya no es válida, se resetea su valor
        if (horaFinSelect.value && horas.indexOf(horaFinSelect.value) < indexInicio) {
            horaFinSelect.value = '';
        }
    }

    // Ejecuta la validación cuando se cambia la hora de inicio
    horaInicioSelect.addEventListener("change", actualizarHorasFin);
    actualizarHorasFin(); // Llama al cargar por si ya había valores preestablecidos

    // Maneja el evento de envío del formulario
    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // Previene el envío tradicional del formulario

        // Valida que la hora de fin no sea anterior a la de inicio
        const indexInicio = horas.indexOf(horaInicioSelect.value);
        const indexFin = horas.indexOf(horaFinSelect.value);

        if (indexInicio > indexFin) {
            alert("La hora final no puede ser anterior a la de inicio.");
            return;
        }

        // Construye el objeto a enviar al servidor
        const payload = {
            fecha: fechaInput.value,
            motivo: motivoInput.value,
            horaInicio: horaInicioSelect.value,
            horaFin: horaFinSelect.value,
            tareaAlumnado: comentarioTareasInput.value
        };

        try {
            // Realiza una solicitud PUT al servidor para actualizar la ausencia
            const response = await fetch(`http://localhost:8080/ausencias/${ausenciaId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            // Si hay un error en la respuesta, lanza una excepción con el mensaje del servidor
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Error al actualizar la ausencia");
            }

            // Muestra un mensaje de éxito temporalmente
            successMessage.style.display = "block";

            // Después de 2 segundos, oculta el mensaje y redirige a la página principal
            setTimeout(() => {
                successMessage.style.display = "none";
                window.location.href = "index-ausencia-guardia.html";
            }, 2000);

        } catch (error) {
            // Muestra el error al usuario y en la consola
            alert(error.message);
            console.error("Detalle:", error);
        }
    });
});