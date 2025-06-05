document.addEventListener('DOMContentLoaded', function () {
    // Seleccionamos el formulario de filtro
    const form = document.querySelector('.filter-form');

    // Selectores para las horas de inicio y fin
    const horaInicio = document.getElementById('horaInicio'); // select inicio
    const horaFin = document.getElementById('horaFin'); // select fin

    // Inputs para las fechas de inicio y fin, con un fallback para fechaInicio
    let fechaInicioInput = document.getElementById('fechaInicio') || document.querySelector('input[type="date"][name="fecha"]');
    let fechaFinInput = document.getElementById('fechaFin') || null;

    // Array con las posibles horas disponibles para seleccionar
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Función que actualiza las opciones disponibles en "horaFin" según el valor seleccionado en "horaInicio"
    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicio.value;
        // Primero habilitamos todas las opciones
        Array.from(horaFin.options).forEach(option => option.disabled = false);

        if (inicioSeleccionado) {
            const indexInicio = horas.indexOf(inicioSeleccionado);

            // Deshabilitamos las horas anteriores a la hora de inicio seleccionada
            Array.from(horaFin.options).forEach(option => {
                if (option.value && horas.indexOf(option.value) < indexInicio) {
                    option.disabled = true;
                }
            });

            // Si la hora fin seleccionada es menor que la hora inicio, la limpiamos
            if (horaFin.value && horas.indexOf(horaFin.value) < indexInicio) {
                horaFin.value = '';
            }
        }
    }

    // Función para cargar las ausencias desde el backend y filtrarlas en frontend según filtros dados
    async function cargarAusencias(filtros = {}) {
        const absenceList = document.getElementById("absence-list");
        absenceList.innerHTML = "<li>Cargando ausencias...</li>"; // Mensaje mientras carga

        try {
            // Petición fetch para obtener las ausencias
            const response = await fetch("http://localhost:8080/ausencias/consultar");

            if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

            let ausencias = await response.json();

            // Filtrado frontend: solo mostrar ausencias que coincidan exactamente con la fecha seleccionada
            ausencias = ausencias.filter(ausencia => {
                const fechaFiltro = filtros.fechaInicio || null;
                const horaInicioFiltro = filtros.horaInicio || null;
                const horaFinFiltro = filtros.horaFin || null;

                // Filtrar por fecha exacta
                if (fechaFiltro && ausencia.fecha !== fechaFiltro) return false;

                // Filtrar por rango de horas si se seleccionaron
                const idxAusenciaInicio = horas.indexOf(ausencia.horaInicio);
                const idxAusenciaFin = horas.indexOf(ausencia.horaFin);
                const idxFiltroInicio = horas.indexOf(horaInicioFiltro);
                const idxFiltroFin = horas.indexOf(horaFinFiltro);

                if (horaInicioFiltro && idxAusenciaInicio < idxFiltroInicio) return false;
                if (horaFinFiltro && idxAusenciaFin > idxFiltroFin) return false;

                return true;
            });

            absenceList.innerHTML = ""; // Limpiar la lista antes de mostrar resultados

            // Mostrar mensaje si no hay ausencias con esos filtros
            if (!ausencias.length) {
                absenceList.innerHTML = "<li>No hay ausencias disponibles con los filtros seleccionados.</li>";
                return;
            }

            // Crear y agregar cada ausencia a la lista
            ausencias.forEach(ausencia => {
                const li = document.createElement("li");
                li.textContent = `Nombre: ${ausencia.nombreProfesor} | Fecha: ${ausencia.fecha} | De ${ausencia.horaInicio} a ${ausencia.horaFin} | Motivo: ${ausencia.motivo}`;
                absenceList.appendChild(li);
            });

        } catch (error) {
            // En caso de error mostrar mensaje en consola y en la UI
            console.error("Error al cargar ausencias:", error);
            absenceList.innerHTML = "<li>Error cargando las ausencias</li>";
        }
    }

    // Evento submit del formulario para aplicar los filtros
    form.addEventListener('submit', function (e) {
        e.preventDefault(); // Prevenir envío tradicional del formulario

        // Obtener los valores ingresados y recortados de espacios
        const fechaInicio = fechaInicioInput?.value.trim();
        const fechaFin = fechaFinInput?.value.trim();

        const startIndex = horas.indexOf(horaInicio.value);
        const endIndex = horas.indexOf(horaFin.value);

        // Validación: fecha fin no puede ser anterior a fecha inicio
        if (fechaInicio && fechaFin && fechaFin < fechaInicio) {
            alert("La fecha fin no puede ser anterior a la fecha de inicio.");
            return;
        }

        // Validación: hora inicio no puede ser posterior a hora fin
        if (horaInicio.value && horaFin.value && startIndex > endIndex) {
            alert("La hora de inicio no puede ser posterior a la de fin.");
            return;
        }

        // Construir objeto filtros solo con los valores seleccionados
        const filtros = {};
        if (fechaInicio) filtros.fechaInicio = fechaInicio;
        if (fechaFin) filtros.fechaFin = fechaFin;
        if (horaInicio.value) filtros.horaInicio = horaInicio.value;
        if (horaFin.value) filtros.horaFin = horaFin.value;

        // Cargar las ausencias aplicando los filtros
        cargarAusencias(filtros);
    });

    // Actualizar opciones de horaFin cuando cambia horaInicio
    horaInicio.addEventListener('change', actualizarHorasFin);

    // Cargar las ausencias sin filtros al cargar la página
    cargarAusencias();
});