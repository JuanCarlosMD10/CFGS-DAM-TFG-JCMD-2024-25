document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);

    const ausenciaId = params.get("id");
    const fecha = params.get("fecha");
    const motivo = params.get("motivo");
    const horaInicio = params.get("horaInicio");
    const horaFin = params.get("horaFin");
    const comentarioTareas = params.get("comentarioTareas");

    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    const fechaInput = document.getElementById("date");
    const motivoInput = document.getElementById("reason");
    const horaInicioSelect = document.getElementById("horaInicio");
    const horaFinSelect = document.getElementById("horaFin");
    const comentarioTareasInput = document.getElementById("comentarioTareas");
    const form = document.querySelector(".absence-form");
    const successMessage = document.getElementById("success-message");

    // Establecer la fecha mínima como hoy
    const hoy = new Date();
    fechaInput.min = hoy.toISOString().split('T')[0];

    // Rellenar campos con los datos de la URL
    if (fecha) fechaInput.value = fecha;
    if (motivo) motivoInput.value = motivo;
    if (horaInicio) horaInicioSelect.value = horaInicio.toUpperCase();
    if (horaFin) horaFinSelect.value = horaFin.toUpperCase();
    if (comentarioTareas) comentarioTareasInput.value = comentarioTareas;

    // Deshabilita horas fin anteriores a la de inicio
    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicioSelect.value;
        const indexInicio = horas.indexOf(inicioSeleccionado);

        Array.from(horaFinSelect.options).forEach(option => {
            const indexOption = horas.indexOf(option.value);
            option.disabled = indexOption < indexInicio;
        });

        // Resetear hora fin si ya no es válida
        if (horaFinSelect.value && horas.indexOf(horaFinSelect.value) < indexInicio) {
            horaFinSelect.value = '';
        }
    }

    horaInicioSelect.addEventListener("change", actualizarHorasFin);
    actualizarHorasFin(); // Llamar al cargar para aplicar lógica si ya hay valor

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const indexInicio = horas.indexOf(horaInicioSelect.value);
        const indexFin = horas.indexOf(horaFinSelect.value);

        if (indexInicio > indexFin) {
            alert("La hora final no puede ser anterior a la de inicio.");
            return;
        }

        const payload = {
            fecha: fechaInput.value,
            motivo: motivoInput.value,
            horaInicio: horaInicioSelect.value,
            horaFin: horaFinSelect.value,
            tareaAlumnado: comentarioTareasInput.value
        };

        try {
            const response = await fetch(`http://localhost:8080/ausencias/${ausenciaId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Error al actualizar la ausencia");
            }

            successMessage.style.display = "block";

            setTimeout(() => {
                successMessage.style.display = "none";
                window.location.href = "index-ausencia-guardia.html";
            }, 2000);

        } catch (error) {
            alert(error.message);
            console.error("Detalle:", error);
        }
    });
});