document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.filter-form');
    const horaInicio = document.getElementById('horaInicio'); // select inicio
    const horaFin = document.getElementById('horaFin'); // select fin

    let fechaInicioInput = document.getElementById('fechaInicio') || document.querySelector('input[type="date"][name="fecha"]');
    let fechaFinInput = document.getElementById('fechaFin') || null;

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

    async function cargarAusencias(filtros = {}) {
        const absenceList = document.getElementById("absence-list");
        absenceList.innerHTML = "<li>Cargando ausencias...</li>";

        try {
            const response = await fetch("http://localhost:8080/ausencias/consultar");

            if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

            let ausencias = await response.json();

            // Filtrar en frontend según filtros recibidos
            ausencias = ausencias.filter(ausencia => {
                const fechaInicioFiltro = filtros.fechaInicio || null;
                const fechaFinFiltro = filtros.fechaFin || null;
                const horaInicioFiltro = filtros.horaInicio || null;
                const horaFinFiltro = filtros.horaFin || null;

                if (fechaInicioFiltro && ausencia.fecha < fechaInicioFiltro) return false;
                if (fechaFinFiltro && ausencia.fecha > fechaFinFiltro) return false;

                const idxAusenciaInicio = horas.indexOf(ausencia.horaInicio);
                const idxAusenciaFin = horas.indexOf(ausencia.horaFin);
                const idxFiltroInicio = horas.indexOf(horaInicioFiltro);
                const idxFiltroFin = horas.indexOf(horaFinFiltro);

                if (horaInicioFiltro && idxAusenciaInicio < idxFiltroInicio) return false;
                if (horaFinFiltro && idxAusenciaFin > idxFiltroFin) return false;

                return true;
            });

            absenceList.innerHTML = "";

            if (!ausencias.length) {
                absenceList.innerHTML = "<li>No hay ausencias disponibles con los filtros seleccionados.</li>";
                return;
            }

            ausencias.forEach(ausencia => {
                const li = document.createElement("li");
                li.textContent = `Nombre: ${ausencia.nombreProfesor} | Fecha: ${ausencia.fecha} | De ${ausencia.horaInicio} a ${ausencia.horaFin} | Motivo: ${ausencia.motivo}`;
                absenceList.appendChild(li);
            });

        } catch (error) {
            console.error("Error al cargar ausencias:", error);
            absenceList.innerHTML = "<li>Error cargando las ausencias</li>";
        }
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const fechaInicio = fechaInicioInput?.value.trim();
        const fechaFin = fechaFinInput?.value.trim();

        const startIndex = horas.indexOf(horaInicio.value);
        const endIndex = horas.indexOf(horaFin.value);

        if (fechaInicio && fechaFin && fechaFin < fechaInicio) {
            alert("La fecha fin no puede ser anterior a la fecha de inicio.");
            return;
        }

        if (horaInicio.value && horaFin.value && startIndex > endIndex) {
            alert("La hora de inicio no puede ser posterior a la de fin.");
            return;
        }

        const filtros = {};
        if (fechaInicio) filtros.fechaInicio = fechaInicio;
        if (fechaFin) filtros.fechaFin = fechaFin;
        if (horaInicio.value) filtros.horaInicio = horaInicio.value;
        if (horaFin.value) filtros.horaFin = horaFin.value;

        cargarAusencias(filtros);
    });

    horaInicio.addEventListener('change', actualizarHorasFin);

    cargarAusencias();
});