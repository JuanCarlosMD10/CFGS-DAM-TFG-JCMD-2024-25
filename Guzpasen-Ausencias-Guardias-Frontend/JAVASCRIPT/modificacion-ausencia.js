// Array para almacenar las ausencias obtenidas desde el backend
let ausencias = [];

// Espera a que todo el contenido del DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", () => {
    // Carga las ausencias desde el servidor al cargar la página
    cargarAusencias();

    // Configura el botón "Confirmar" para redirigir a la página de modificación
    document.getElementById("confirm-btn").addEventListener("click", () => {
        // Obtiene el radio button seleccionado que representa una ausencia
        const selectedRadio = document.querySelector('input[name="ausencia"]:checked');

        // Si no se seleccionó ninguna ausencia, muestra una alerta y detiene la ejecución
        if (!selectedRadio) {
            alert("Por favor, selecciona una ausencia para continuar.");
            return;
        }

        // Obtiene el ID de la ausencia seleccionada
        const ausenciaId = selectedRadio.value;
        // Busca la ausencia en el array 'ausencias' usando el ID
        const ausencia = ausencias.find(a => a.idAusencia == ausenciaId);

        // Si no se encontró la ausencia seleccionada, muestra un error
        if (!ausencia) {
            alert("No se ha podido encontrar la ausencia seleccionada.");
            return;
        }

        // Crea los parámetros que se pasarán por URL con los datos de la ausencia
        const params = new URLSearchParams({
            id: ausencia.idAusencia,
            fecha: ausencia.fecha,
            motivo: ausencia.motivo,
            horaInicio: ausencia.horaInicio,
            horaFin: ausencia.horaFin,
            comentarioTareas: ausencia.tareaAlumnado ?? "" // Si no existe, pone cadena vacía
        });

        // Redirige a la página de modificar ausencia con los parámetros en la URL
        window.location.href = `../HTML/modificar-ausencia.html?${params.toString()}`;
    });

    // Habilita el botón "Confirmar" cuando se selecciona alguna ausencia en la lista
    document.getElementById("absence-list").addEventListener("change", () => {
        document.getElementById("confirm-btn").disabled = false;
    });
});

// Función asíncrona que carga las ausencias desde el backend
async function cargarAusencias() {
    // Obtiene el contenedor donde se listarán las ausencias
    const absenceList = document.getElementById("absence-list");
    // Muestra mensaje de carga mientras se obtienen los datos
    absenceList.innerHTML = "<li>Cargando ausencias...</li>";

    try {
        // Solicita al backend las ausencias del profesor con ID 1 (puedes parametrizar esto)
        const response = await fetch('http://localhost:8080/ausencias/profesor/1');

        // Si la respuesta no es exitosa, lanza un error
        if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

        // Convierte la respuesta JSON en un array y lo guarda en la variable global
        ausencias = await response.json();
        // Limpia el contenedor para mostrar las ausencias
        absenceList.innerHTML = "";

        // Si no hay ausencias, muestra un mensaje indicando que no hay datos
        if (ausencias.length === 0) {
            absenceList.innerHTML = "<li>No hay ausencias registradas.</li>";
            return;
        }

        // Recorre cada ausencia y crea elementos HTML para mostrarlas
        ausencias.forEach((ausencia) => {
            const li = document.createElement("li");
            const label = document.createElement("label");
            const input = document.createElement("input");

            // Crea un radio button para seleccionar la ausencia
            input.type = "radio";                      // Radio para selección única
            input.name = "ausencia";                   // Mismo nombre para agrupar radios
            input.value = ausencia.idAusencia;         // Valor: el ID único de la ausencia
            input.classList.add("absence-checkbox");   // Clase para estilos (opcional)

            // Valores por defecto para evitar mostrar undefined o null
            const motivo = ausencia.motivo ?? "Motivo no especificado";
            const fecha = ausencia.fecha ?? "Fecha desconocida";

            // Texto que se muestra al lado del radio button
            const texto = `Fecha: ${fecha} | Motivo: ${motivo}`;

            // Agrega el input y el texto al label, y este al elemento de la lista
            label.appendChild(input);
            label.append(` ${texto}`);
            li.appendChild(label);
            absenceList.appendChild(li);
        });

        // Desactiva el botón de confirmación hasta que el usuario seleccione una ausencia
        document.getElementById("confirm-btn").disabled = true;

    } catch (error) {
        // Si ocurre un error en la petición o procesamiento, lo muestra por consola
        console.error("Error al cargar ausencias:", error);
        // Y muestra un mensaje de error en el contenedor de la lista
        absenceList.innerHTML = "<li>Error al cargar ausencias.</li>";
    }
}