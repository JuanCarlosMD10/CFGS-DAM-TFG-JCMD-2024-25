// Espera a que todo el contenido del DOM se haya cargado antes de ejecutar el código
document.addEventListener('DOMContentLoaded', function () {
    // Selecciona el formulario de filtro
    const form = document.querySelector('.filter-form');
    // Selecciona los elementos input de hora de inicio y hora de fin
    const horaInicio = document.getElementById('horaInicio');
    const horaFin = document.getElementById('horaFin');
    // Selecciona el input de fecha
    const fechaInput = document.querySelector('input[name="fecha"]');
    // Selecciona el contenedor donde se mostrará la lista de ausencias y guardias
    const absenceList = document.getElementById('absence-list');

    // Array con las horas (franjas horarias)
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Función para actualizar las opciones disponibles en el select de horaFin
    // para que no se puedan seleccionar horas anteriores a la horaInicio seleccionada
    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicio.value;

        // Primero habilita todas las opciones de horaFin
        Array.from(horaFin.options).forEach(option => {
            option.disabled = false;
        });

        // Si hay una hora de inicio seleccionada
        if (inicioSeleccionado) {
            const indexInicio = horas.indexOf(inicioSeleccionado);

            // Deshabilita las opciones de horaFin que sean anteriores a la horaInicio
            Array.from(horaFin.options).forEach(option => {
                if (option.value && horas.indexOf(option.value) < indexInicio) {
                    option.disabled = true;
                }
            });

            // Si la horaFin seleccionada es inválida (antes de la horaInicio), la limpia
            if (horaFin.value && horas.indexOf(horaFin.value) < indexInicio) {
                horaFin.value = '';
            }
        }
    }

    // Función asíncrona para cargar las ausencias y guardias desde el servidor
    // y filtrarlas según los filtros pasados como parámetro
    async function cargarDatos(filtros = {}) {
        // Muestra un mensaje de carga mientras se obtienen los datos
        absenceList.innerHTML = "<li>Cargando datos...</li>";

        try {
            // Realiza dos peticiones simultáneas para obtener ausencias y guardias
            const [ausResp, guarResp] = await Promise.all([
                fetch("http://localhost:8080/ausencias/consultar"),
                fetch(`http://localhost:8080/guardias/consultar${filtros.fecha ? `?fecha=${filtros.fecha}` : ''}`)
            ]);

            // Verifica que ambas respuestas sean exitosas
            if (!ausResp.ok || !guarResp.ok) {
                throw new Error("Error al obtener los datos del servidor");
            }

            // Convierte las respuestas en JSON
            let ausencias = await ausResp.json();
            let guardias = await guarResp.json();

            // Obtiene los índices de las horas seleccionadas para filtrar
            const idxFiltroInicio = horas.indexOf(filtros.horaInicio || '');
            const idxFiltroFin = horas.indexOf(filtros.horaFin || '');

            // Función para filtrar elementos según fecha y rango de horas
            const filtrarPorHoras = (item) => {
                const idxInicio = horas.indexOf(item.horaInicio);
                const idxFin = horas.indexOf(item.horaFin);
                if (filtros.fecha && item.fecha !== filtros.fecha) return false;
                if (filtros.horaInicio && idxInicio < idxFiltroInicio) return false;
                if (filtros.horaFin && idxFin > idxFiltroFin) return false;
                return true;
            };

            // Aplica los filtros a las ausencias y guardias
            ausencias = ausencias.filter(filtrarPorHoras);
            guardias = guardias.filter(filtrarPorHoras);

            // Limpia la lista antes de mostrar los resultados filtrados
            absenceList.innerHTML = "";

            // Si no hay datos para mostrar, muestra un mensaje
            if (ausencias.length === 0 && guardias.length === 0) {
                absenceList.innerHTML = "<li>No hay datos disponibles con los filtros seleccionados.</li>";
                return;
            }

            // Muestra la lista de ausencias con información detallada
            ausencias.forEach(a => {
                const li = document.createElement("li");
                li.textContent = `[AUSENCIA] - Nombre: ${a.nombreProfesor} | Fecha: ${a.fecha} | De ${a.horaInicio} a ${a.horaFin} | Motivo: ${a.motivo}`;
                absenceList.appendChild(li);
            });

            // Muestra la lista de guardias
            guardias.forEach(g => {
                const li = document.createElement("li");
                li.textContent = `[GUARDIA] - Nombre: ${g.nombreProfesor} | Fecha: ${g.fecha} | De ${g.horaInicio} a ${g.horaFin}`;
                absenceList.appendChild(li);
            });

        } catch (error) {
            // En caso de error muestra mensaje y loguea en consola
            console.error("Error al cargar datos:", error);
            absenceList.innerHTML = "<li>Error cargando los datos.</li>";
        }
    }

    // Evento submit del formulario de filtros
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const fecha = fechaInput.value;
        const inicio = horaInicio.value;
        const fin = horaFin.value;

        const startIndex = horas.indexOf(inicio);
        const endIndex = horas.indexOf(fin);

        // Valida que la hora de inicio no sea posterior a la hora de fin
        if (inicio && fin && startIndex > endIndex) {
            alert("La hora de inicio no puede ser posterior a la de fin.");
            return;
        }

        // Construye el objeto de filtros con los valores seleccionados
        const filtros = {};
        if (fecha) filtros.fecha = fecha;
        if (inicio) filtros.horaInicio = inicio;
        if (fin) filtros.horaFin = fin;

        // Llama a la función para cargar y mostrar los datos filtrados
        cargarDatos(filtros);
    });

    // Evento para actualizar las opciones de horaFin cuando cambia horaInicio
    horaInicio.addEventListener('change', actualizarHorasFin);

    // Carga inicial de datos sin filtros
    cargarDatos();
});