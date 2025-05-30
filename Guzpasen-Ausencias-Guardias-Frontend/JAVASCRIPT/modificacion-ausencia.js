let ausencias = [];

document.addEventListener("DOMContentLoaded", () => {
    cargarAusencias();

    document.getElementById("confirm-btn").addEventListener("click", () => {
        const selectedRadio = document.querySelector('input[name="ausencia"]:checked');

        if (!selectedRadio) {
            alert("Por favor, selecciona una ausencia para continuar.");
            return;
        }

        const ausenciaId = selectedRadio.value;
        const ausencia = ausencias.find(a => a.idAusencia == ausenciaId);
        if (!ausencia) {
            alert("No se ha podido encontrar la ausencia seleccionada.");
            return;
        }

        // Parámetros para enviar en la URL (asegúrate que coinciden con el backend)
        const params = new URLSearchParams({
            id: ausencia.idAusencia,
            fecha: ausencia.fecha,
            motivo: ausencia.motivo,
            horaInicio: ausencia.horaInicio,
            horaFin: ausencia.horaFin,
            comentarioTareas: ausencia.tareaAlumnado ?? ""
        });

        window.location.href = `../HTML/modificar-ausencia.html?${params.toString()}`;
    });

    // Habilitar botón confirmar cuando se selecciona una ausencia
    document.getElementById("absence-list").addEventListener("change", () => {
        document.getElementById("confirm-btn").disabled = false;
    });
});

async function cargarAusencias() {
    const absenceList = document.getElementById("absence-list");
    absenceList.innerHTML = "<li>Cargando ausencias...</li>";

    try {
        const response = await fetch('http://localhost:8080/ausencias/profesor/1');
        if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

        ausencias = await response.json();
        absenceList.innerHTML = "";

        if (ausencias.length === 0) {
            absenceList.innerHTML = "<li>No hay ausencias registradas.</li>";
            return;
        }

        ausencias.forEach((ausencia) => {
            const li = document.createElement("li");
            const label = document.createElement("label");
            const input = document.createElement("input");

            input.type = "radio";
            input.name = "ausencia";
            input.value = ausencia.idAusencia;
            input.classList.add("absence-checkbox");

            const motivo = ausencia.motivo ?? "Motivo no especificado";
            const fecha = ausencia.fecha ?? "Fecha desconocida";
            const texto = `Fecha: ${fecha} | Motivo: ${motivo}`;

            label.appendChild(input);
            label.append(` ${texto}`);
            li.appendChild(label);
            absenceList.appendChild(li);
        });

        // Inicialmente deshabilitar el botón confirmar
        document.getElementById("confirm-btn").disabled = true;

    } catch (error) {
        console.error("Error al cargar ausencias:", error);
        absenceList.innerHTML = "<li>Error al cargar ausencias.</li>";
    }
}