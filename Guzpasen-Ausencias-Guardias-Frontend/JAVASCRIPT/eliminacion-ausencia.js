// Espera a que el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", () => {
    cargarAusencias(); // Carga la lista de ausencias al cargar la página

    // Obtiene referencias a los elementos necesarios del DOM
    const eliminarBtn = document.querySelector(".confirm-btn"); // Botón principal "Eliminar"
    const modal = document.getElementById("confirmation-modal"); // Modal de confirmación
    const modalConfirmBtn = document.getElementById("confirm-btn-modal"); // Botón "Confirmar" en el modal
    const modalCancelBtn = document.getElementById("cancel-btn-modal"); // Botón "Cancelar" en el modal

    // Muestra el modal al hacer clic en el botón "Eliminar"
    eliminarBtn.addEventListener("click", () => {
        const selectedRadio = document.querySelector('input[name="ausencia"]:checked');
        if (!selectedRadio) {
            alert("Por favor, selecciona una ausencia para eliminar.");
            return;
        }
        modal.style.display = "flex"; // Muestra el modal de confirmación
    });

    // Cierra el modal al hacer clic en "Cancelar"
    modalCancelBtn.addEventListener("click", () => {
        modal.style.display = "none";
    });

    // Maneja la lógica al confirmar la eliminación en el modal
    modalConfirmBtn.addEventListener("click", async () => {
        const selectedRadio = document.querySelector('input[name="ausencia"]:checked');
        if (!selectedRadio) {
            alert("Por favor, selecciona una ausencia para eliminar.");
            modal.style.display = "none";
            return;
        }

        const ausenciaId = selectedRadio.value; // Obtiene el ID de la ausencia seleccionada

        try {
            // Realiza la solicitud DELETE al backend
            const response = await fetch(`http://localhost:8080/ausencias/${ausenciaId}`, {
                method: 'DELETE'
            });

            if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

            // Oculta el modal y muestra mensaje de éxito
            modal.style.display = "none";
            mostrarMensajeExito();

            // Refresca la lista después de 1.5 segundos
            setTimeout(() => {
                ocultarMensajeExito();
                cargarAusencias();
            }, 1500);

        } catch (error) {
            console.error("Error al eliminar la ausencia:", error);
            alert("No se pudo eliminar la ausencia. Inténtalo de nuevo.");
            modal.style.display = "none";
        }
    });

    // Habilita o deshabilita el botón "Eliminar" según si hay una ausencia seleccionada
    document.getElementById("absence-list").addEventListener("change", () => {
        const selected = document.querySelector('input[name="ausencia"]:checked');
        eliminarBtn.disabled = !selected;
    });

    // Desactiva el botón "Eliminar" por defecto
    eliminarBtn.disabled = true;
});

// Muestra el mensaje de éxito
function mostrarMensajeExito() {
    const mensaje = document.getElementById("success-message");
    mensaje.style.display = "block";
}

// Oculta el mensaje de éxito
function ocultarMensajeExito() {
    const mensaje = document.getElementById("success-message");
    mensaje.style.display = "none";
}

// Carga dinámicamente la lista de ausencias desde el backend
async function cargarAusencias() {
    const absenceList = document.getElementById("absence-list");
    absenceList.innerHTML = "<li>Cargando ausencias...</li>"; // Muestra mensaje temporal

    try {
        // Solicita la lista de ausencias del profesor con ID 1
        const response = await fetch('http://localhost:8080/ausencias/profesor/1');
        if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

        const ausencias = await response.json(); // Convierte la respuesta a JSON
        absenceList.innerHTML = ""; // Limpia la lista anterior

        if (ausencias.length === 0) {
            // Si no hay ausencias, muestra mensaje
            absenceList.innerHTML = "<li>No hay ausencias registradas.</li>";
            return;
        }

        // Recorre las ausencias y las agrega como elementos de lista
        ausencias.forEach(ausencia => {
            const li = document.createElement("li");

            // Crea un radio button para seleccionar la ausencia
            const radio = document.createElement("input");
            radio.type = "radio";
            radio.name = "ausencia";
            radio.value = ausencia.idAusencia || ausencia.id || "";

            // Construye el texto de la ausencia
            const texto = ` Fecha: ${ausencia.fecha || "sin fecha"} | Motivo: ${ausencia.motivo || "sin motivo"}`;

            // Crea y organiza el contenido dentro del <li>
            const label = document.createElement("label");
            label.appendChild(radio);
            label.append(texto);

            li.appendChild(label);
            absenceList.appendChild(li);
        });

    } catch (error) {
        console.error("Error al cargar ausencias:", error);
        absenceList.innerHTML = "<li>Error cargando las ausencias</li>"; // En caso de error
    }
}