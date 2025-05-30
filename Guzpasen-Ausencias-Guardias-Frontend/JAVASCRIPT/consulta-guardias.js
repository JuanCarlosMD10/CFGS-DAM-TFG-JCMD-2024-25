document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.filter-form');
    const horaInicio = document.getElementById('horaInicio');
    const horaFin = document.getElementById('horaFin');
    const fechaInput = document.querySelector('input[name="fecha"]');
    const absenceList = document.getElementById('absence-list');

    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicio.value;
        Array.from(horaFin.options).forEach(option => option.disabled = false);

        if (inicioSeleccionado) {
            const indexInicio = horas.indexOf(inicioSeleccionado);
            Array.from(horaFin.options).forEach(option => {
                if (option.value && horas.indexOf(option.value) < indexInicio) {
                    option.disabled = true;
                }
            });

            if (horaFin.value && horas.indexOf(horaFin.value) < indexInicio) {
                horaFin.value = '';
            }
        }
    }

    async function cargarGuardias(filtros = {}) {
        absenceList.innerHTML = "<li>Cargando guardias...</li>";

        try {
            const query = filtros.fecha ? `?fecha=${filtros.fecha}` : '';
            const response = await fetch(`http://localhost:8080/guardias/consultar${query}`);

            if (!response.ok) throw new Error(`HTTP error: ${response.status}`);

            let guardias = await response.json();

            // Aplicar filtro de horas (si existen)
            if (filtros.horaInicio || filtros.horaFin) {
                guardias = guardias.filter(guardia => {
                    const idxInicio = horas.indexOf(guardia.horaInicio);
                    const idxFin = horas.indexOf(guardia.horaFin);
                    const idxFiltroInicio = horas.indexOf(filtros.horaInicio);
                    const idxFiltroFin = horas.indexOf(filtros.horaFin);

                    if (filtros.horaInicio && idxInicio < idxFiltroInicio) return false;
                    if (filtros.horaFin && idxFin > idxFiltroFin) return false;

                    return true;
                });
            }

            absenceList.innerHTML = "";

            if (!guardias || guardias.length === 0) {
                absenceList.innerHTML = "<li>No hay guardias con los filtros seleccionados.</li>";
                return;
            }

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
            console.error("Error al cargar guardias:", error);
            absenceList.innerHTML = "<li>Error cargando las guardias.</li>";
        }
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const filtros = {};
        const fecha = fechaInput.value;
        const inicio = horaInicio.value;
        const fin = horaFin.value;

        if (inicio && fin && horas.indexOf(inicio) > horas.indexOf(fin)) {
            alert("La hora de inicio no puede ser posterior a la de fin.");
            return;
        }

        if (fecha) filtros.fecha = fecha;
        if (inicio) filtros.horaInicio = inicio;
        if (fin) filtros.horaFin = fin;

        cargarGuardias(filtros);
    });

    horaInicio.addEventListener('change', actualizarHorasFin);

    // Carga inicial
    cargarGuardias();
});