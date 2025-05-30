// Array para almacenar las ausencias que se obtendrán desde el backend
let ausencias = [];

// Espera a que todo el contenido del DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", () => {
    // Carga las ausencias desde el servidor
    cargarAusencias();

    // Configura el botón "Confirmar" para redirigir a la página de modificación
    document.getElementById("confirm-btn").addEventListener("click", () => {
        // Obtiene la ausencia seleccionada (radio button)
        const selectedRadio = document.querySelector('input[name="ausencia"]:checked');

        // Si no hay ninguna ausencia seleccionada, muestra un mensaje de alerta
        if (!selectedRadio) {
            alert("Por favor, selecciona una ausencia para continuar.");
            return;
        }

        // Busca la ausencia correspondiente en el array usando el ID
        const ausenciaId = selectedRadio.value;
        const ausencia = ausencias.find(a => a.idAusencia == ausenciaId);

        // Si no se encuentra la ausencia seleccionada, muestra un error
        if (!ausencia) {
            alert("No se ha podido encontrar la ausencia seleccionada.");
            return;
        }

        // Construye los parámetros de la URL con los datos de la ausencia
        const params = new URLSearchParams({
            id: ausencia.idAusencia,
            fecha: ausencia.fecha,
            motivo: ausencia.motivo,
            horaInicio: ausencia.horaInicio,
            horaFin: ausencia.horaFin,
            comentarioTareas: ausencia.tareaAlumnado ?? "" // Usa valor por defecto si es null/undefined
        });

        // Redirige a la página de modificación con los parámetros en la URL
        window.location.href = `../HTML/modificar-ausencia.html?${params.toString()}`;
    });

    // Habilita el botón de confirmar cuando se selecciona una ausencia
    document.getElementById("absence-list").addEventListener("change", () => {
        document.getElementById("confirm-btn").disabled = false;
    });
});

// Función asíncrona que carga las ausencias del backend
async function cargarAusencias() {
    const absenceList = document.getElementById("absence-list");
    absenceList.innerHTML = "<li>Cargando ausencias...</li>";

    try {
        // Hace una solicitud GET al servidor para obtener las ausencias del profesor con ID 1
        const response = await fetch('http://localhost:8080/ausencias/profesor/1');

        // Lanza un error si la respuesta no es exitosa
        if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

        // Convierte la respuesta JSON en un array y la guarda
        ausencias = await response.json();
        absenceList.innerHTML = "";

        // Si no hay ausencias, muestra un mensaje apropiado
        if (ausencias.length === 0) {
            absenceList.innerHTML = "<li>No hay ausencias registradas.</li>";
            return;
        }

        // Crea dinámicamente elementos HTML para mostrar cada ausencia
        ausencias.forEach((ausencia) => {
            const li = document.createElement("li");
            const label = document.createElement("label");
            const input = document.createElement("input");

            input.type = "radio";                      // Tipo radio para seleccionar una sola opción
            input.name = "ausencia";                   // Grupo de radios con el mismo nombre
            input.value = ausencia.idAusencia;         // Valor del radio: el ID de la ausencia
            input.classList.add("absence-checkbox");   // Clase para aplicar estilos si es necesario

            // Valores por defecto si los campos vienen vacíos o null
            const motivo = ausencia.motivo ?? "Motivo no especificado";
            const fecha = ausencia.fecha ?? "Fecha desconocida";

            // Texto que se mostrará en la opción de ausencia
            const texto = `Fecha: ${fecha} | Motivo: ${motivo}`;

            // Agrega el input y el texto al label, luego al li y finalmente a la lista
            label.appendChild(input);
            label.append(` ${texto}`);
            li.appendChild(label);
            absenceList.appendChild(li);
        });

        // Desactiva el botón de confirmación hasta que se seleccione una ausencia
        document.getElementById("confirm-btn").disabled = true;

    } catch (error) {
        // Muestra un mensaje de error en consola y en la página si falla la carga
        console.error("Error al cargar ausencias:", error);
        absenceList.innerHTML = "<li>Error al cargar ausencias.</li>";
    }
}