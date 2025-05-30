document.addEventListener("DOMContentLoaded", () => {
    cargarAusencias();

    const eliminarBtn = document.querySelector(".confirm-btn"); // botón ELIMINAR (fuera modal)
    const modal = document.getElementById("confirmation-modal");
    const modalConfirmBtn = document.getElementById("confirm-btn-modal");
    const modalCancelBtn = document.getElementById("cancel-btn-modal");

    eliminarBtn.addEventListener("click", () => {
        const selectedRadio = document.querySelector('input[name="ausencia"]:checked');
        if (!selectedRadio) {
            alert("Por favor, selecciona una ausencia para eliminar.");
            return;
        }
        // Mostrar modal
        modal.style.display = "flex";
    });

    modalCancelBtn.addEventListener("click", () => {
        // Ocultar modal sin hacer nada
        modal.style.display = "none";
    });

    modalConfirmBtn.addEventListener("click", async () => {
        const selectedRadio = document.querySelector('input[name="ausencia"]:checked');
        if (!selectedRadio) {
            alert("Por favor, selecciona una ausencia para eliminar.");
            modal.style.display = "none";
            return;
        }

        const ausenciaId = selectedRadio.value;

        try {
            const response = await fetch(`http://localhost:8080/ausencias/${ausenciaId}`, {
                method: 'DELETE'
            });

            if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

            // Ocultar modal y mostrar mensaje éxito
            modal.style.display = "none";
            mostrarMensajeExito();

            // Refrescar lista después de un pequeño delay para ver mensaje
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

    document.getElementById("absence-list").addEventListener("change", () => {
        const selected = document.querySelector('input[name="ausencia"]:checked');
        eliminarBtn.disabled = !selected;
    });

    // Inicialmente botón ELIMINAR deshabilitado
    eliminarBtn.disabled = true;
});

function mostrarMensajeExito() {
    const mensaje = document.getElementById("success-message");
    mensaje.style.display = "block";
}

function ocultarMensajeExito() {
    const mensaje = document.getElementById("success-message");
    mensaje.style.display = "none";
}

async function cargarAusencias() {
    const absenceList = document.getElementById("absence-list");
    absenceList.innerHTML = "<li>Cargando ausencias...</li>";

    try {
        const response = await fetch('http://localhost:8080/ausencias/profesor/1');
        if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

        const ausencias = await response.json();

        absenceList.innerHTML = "";

        if (ausencias.length === 0) {
            absenceList.innerHTML = "<li>No hay ausencias registradas.</li>";
            return;
        }

        ausencias.forEach(ausencia => {
            const li = document.createElement("li");

            const radio = document.createElement("input");
            radio.type = "radio";
            radio.name = "ausencia";
            radio.value = ausencia.idAusencia || ausencia.id || "";

            const texto = ` Fecha: ${ausencia.fecha || "sin fecha"} | Motivo: ${ausencia.motivo || "sin motivo"}`;

            const label = document.createElement("label");
            label.appendChild(radio);
            label.append(texto);

            li.appendChild(label);
            absenceList.appendChild(li);
        });

    } catch (error) {
        console.error("Error al cargar ausencias:", error);
        absenceList.innerHTML = "<li>Error cargando las ausencias</li>";
    }
}