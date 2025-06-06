document.addEventListener('DOMContentLoaded', function () {
    // Al cargar completamente el DOM, se ejecuta esta función

    // Selecciona el formulario, inputs y lista del DOM
    const form = document.querySelector('.filter-form');
    const horaInicio = document.getElementById('horaInicio');
    const horaFin = document.getElementById('horaFin');
    const fechaInput = document.querySelector('input[name="fecha"]');
    const absenceList = document.getElementById('absence-list');

    // Define las horas posibles en las que puede haber guardias
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Función que actualiza las opciones del select "horaFin" dependiendo de "horaInicio"
    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicio.value;

        // Habilita todas las opciones de "horaFin" antes de aplicar restricciones
        Array.from(horaFin.options).forEach(option => option.disabled = false);

        if (inicioSeleccionado) {
            const indexInicio = horas.indexOf(inicioSeleccionado);

            // Recorre las opciones de "horaFin" y deshabilita las que sean anteriores a "horaInicio"
            Array.from(horaFin.options).forEach(option => {
                if (option.value && horas.indexOf(option.value) < indexInicio) {
                    option.disabled = true;
                }
            });

            // Si la horaFin seleccionada queda inválida tras el cambio, la limpia
            if (horaFin.value && horas.indexOf(horaFin.value) < indexInicio) {
                horaFin.value = '';
            }
        }
    }

    // Función asíncrona que solicita al backend la lista de guardias, filtrada opcionalmente
    async function cargarGuardias(filtros = {}) {
        // Muestra mensaje mientras se cargan los datos
        absenceList.innerHTML = "<li>Cargando guardias...</li>";

        try {
            // Construye la URL de la petición con query param de fecha si se proporciona
            const query = filtros.fecha ? `?fecha=${filtros.fecha}` : '';

            // Realiza la petición al backend
            const response = await fetch(`http://localhost:8080/guardias/consultar${query}`);

            // Si la respuesta no es OK (código 200), lanza un error
            if (!response.ok) throw new Error(`HTTP error: ${response.status}`);

            // Convierte la respuesta a JSON
            let guardias = await response.json();

            // Si se especifican filtros de horaInicio y/o horaFin, se filtra manualmente la lista
            if (filtros.horaInicio || filtros.horaFin) {
                guardias = guardias.filter(guardia => {
                    const idxInicio = horas.indexOf(guardia.horaInicio);
                    const idxFin = horas.indexOf(guardia.horaFin);
                    const idxFiltroInicio = horas.indexOf(filtros.horaInicio);
                    const idxFiltroFin = horas.indexOf(filtros.horaFin);

                    // Si la guardia empieza antes de lo permitido, se descarta
                    if (filtros.horaInicio && idxInicio < idxFiltroInicio) return false;

                    // Si la guardia termina después de lo permitido, se descarta
                    if (filtros.horaFin && idxFin > idxFiltroFin) return false;

                    // Si pasa los filtros, se mantiene
                    return true;
                });
            }

            // Limpia la lista antes de insertar los resultados nuevos
            absenceList.innerHTML = "";

            // Si no hay guardias que cumplan los filtros, se muestra mensaje
            if (!guardias || guardias.length === 0) {
                absenceList.innerHTML = "<li>No hay guardias con los filtros seleccionados.</li>";
                return;
            }

            // Por cada guardia, crea un elemento <li> con los datos de la guardia
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
            // Si ocurre un error, se muestra un mensaje en la lista y se imprime el error en consola
            console.error("Error al cargar guardias:", error);
            absenceList.innerHTML = "<li>Error cargando las guardias.</li>";
        }
    }

    // Evento cuando se envía el formulario
    form.addEventListener('submit', function (e) {
        e.preventDefault(); // Previene el comportamiento por defecto (recargar la página)

        // Crea un objeto con los filtros según los inputs del formulario
        const filtros = {};
        const fecha = fechaInput.value;
        const inicio = horaInicio.value;
        const fin = horaFin.value;

        // Si se seleccionó una hora de inicio posterior a la hora de fin, muestra alerta y cancela
        if (inicio && fin && horas.indexOf(inicio) > horas.indexOf(fin)) {
            alert("La hora de inicio no puede ser posterior a la de fin.");
            return;
        }

        // Agrega al objeto "filtros" sólo los campos que tienen valor
        if (fecha) filtros.fecha = fecha;
        if (inicio) filtros.horaInicio = inicio;
        if (fin) filtros.horaFin = fin;

        // Llama a la función para cargar guardias con los filtros aplicados
        cargarGuardias(filtros);
    });

    // Cada vez que se cambia la horaInicio, se actualizan las opciones disponibles de horaFin
    horaInicio.addEventListener('change', actualizarHorasFin);

    // Al cargar la página por primera vez, se cargan las guardias sin filtros
    cargarGuardias();
});