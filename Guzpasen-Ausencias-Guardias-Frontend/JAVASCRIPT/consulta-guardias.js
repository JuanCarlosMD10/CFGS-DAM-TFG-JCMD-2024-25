document.addEventListener('DOMContentLoaded', function () {
    // Selecciona el formulario, inputs y lista de ausencias del DOM
    const form = document.querySelector('.filter-form');
    const horaInicio = document.getElementById('horaInicio');
    const horaFin = document.getElementById('horaFin');
    const fechaInput = document.querySelector('input[name="fecha"]');
    const absenceList = document.getElementById('absence-list');

    // Array con las horas disponibles para las guardias
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Función para actualizar las opciones del select de horaFin según la horaInicio seleccionada
    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicio.value;
        // Habilita todas las opciones antes de aplicar los filtros
        Array.from(horaFin.options).forEach(option => option.disabled = false);

        if (inicioSeleccionado) {
            const indexInicio = horas.indexOf(inicioSeleccionado);
            // Deshabilita las opciones de horaFin que sean anteriores a la horaInicio seleccionada
            Array.from(horaFin.options).forEach(option => {
                if (option.value && horas.indexOf(option.value) < indexInicio) {
                    option.disabled = true;
                }
            });

            // Si la horaFin seleccionada es inválida (menor que inicio), la limpia
            if (horaFin.value && horas.indexOf(horaFin.value) < indexInicio) {
                horaFin.value = '';
            }
        }
    }

    // Función para cargar las guardias desde el backend con filtros opcionales
    async function cargarGuardias(filtros = {}) {
        // Muestra mensaje mientras carga los datos
        absenceList.innerHTML = "<li>Cargando guardias...</li>";

        try {
            // Construye la query string si se proporciona una fecha
            const query = filtros.fecha ? `?fecha=${filtros.fecha}` : '';
            // Hace la petición fetch al endpoint correspondiente
            const response = await fetch(`http://localhost:8080/guardias/consultar${query}`);

            // Lanza error si la respuesta no es exitosa
            if (!response.ok) throw new Error(`HTTP error: ${response.status}`);

            // Obtiene el JSON con los datos de guardias
            let guardias = await response.json();

            // Aplica el filtro por horas si se especificaron
            if (filtros.horaInicio || filtros.horaFin) {
                guardias = guardias.filter(guardia => {
                    const idxInicio = horas.indexOf(guardia.horaInicio);
                    const idxFin = horas.indexOf(guardia.horaFin);
                    const idxFiltroInicio = horas.indexOf(filtros.horaInicio);
                    const idxFiltroFin = horas.indexOf(filtros.horaFin);

                    // Filtra guardias que empiezan antes de la horaInicio del filtro
                    if (filtros.horaInicio && idxInicio < idxFiltroInicio) return false;
                    // Filtra guardias que terminan después de la horaFin del filtro
                    if (filtros.horaFin && idxFin > idxFiltroFin) return false;

                    return true;
                });
            }

            // Limpia la lista antes de mostrar resultados
            absenceList.innerHTML = "";

            // Si no hay guardias que mostrar, muestra mensaje
            if (!guardias || guardias.length === 0) {
                absenceList.innerHTML = "<li>No hay guardias con los filtros seleccionados.</li>";
                return;
            }

            // Por cada guardia, crea un <li> con la información y la agrega a la lista
            guardias.forEach(guardia => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <label>
                        Nombre: ${guardia.nombreProfesor} | Fecha: ${guardia.fecha} | De ${guardia.horaInicio} a ${guardia.horaFin}
                    </label>
                `;
                absenceList.appendChild(li);
            });

        } catch (error) {
            // En caso de error en la petición, muestra mensaje y loggea el error
            console.error("Error al cargar guardias:", error);
            absenceList.innerHTML = "<li>Error cargando las guardias.</li>";
        }
    }

    // Evento submit del formulario para filtrar las guardias
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        // Construye objeto de filtros basado en inputs del formulario
        const filtros = {};
        const fecha = fechaInput.value;
        const inicio = horaInicio.value;
        const fin = horaFin.value;

        // Valida que la hora de inicio no sea mayor que la hora de fin
        if (inicio && fin && horas.indexOf(inicio) > horas.indexOf(fin)) {
            alert("La hora de inicio no puede ser posterior a la de fin.");
            return;
        }

        // Agrega filtros al objeto si tienen valor
        if (fecha) filtros.fecha = fecha;
        if (inicio) filtros.horaInicio = inicio;
        if (fin) filtros.horaFin = fin;

        // Carga las guardias con los filtros seleccionados
        cargarGuardias(filtros);
    });

    // Evento change para actualizar las opciones del select horaFin cuando cambia horaInicio
    horaInicio.addEventListener('change', actualizarHorasFin);

    // Carga inicial de guardias sin filtros
    cargarGuardias();
});