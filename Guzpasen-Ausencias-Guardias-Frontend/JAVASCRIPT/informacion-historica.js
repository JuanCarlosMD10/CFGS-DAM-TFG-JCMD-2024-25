document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.filter-form');
    const horaInicio = document.getElementById('horaInicio');
    const horaFin = document.getElementById('horaFin');
    const fechaInput = document.querySelector('input[name="fecha"]');
    const absenceList = document.getElementById('absence-list');

    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicio.value;

        Array.from(horaFin.options).forEach(option => {
            option.disabled = false;
        });

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

    async function cargarDatos(filtros = {}) {
        absenceList.innerHTML = "<li>Cargando datos...</li>";

        try {
            const [ausResp, guarResp] = await Promise.all([
                fetch("http://localhost:8080/ausencias/consultar"),
                fetch(`http://localhost:8080/guardias/consultar${filtros.fecha ? `?fecha=${filtros.fecha}` : ''}`)
            ]);

            if (!ausResp.ok || !guarResp.ok) {
                throw new Error("Error al obtener los datos del servidor");
            }

            let ausencias = await ausResp.json();
            let guardias = await guarResp.json();

            // Filtros
            const idxFiltroInicio = horas.indexOf(filtros.horaInicio || '');
            const idxFiltroFin = horas.indexOf(filtros.horaFin || '');

            const filtrarPorHoras = (item) => {
                const idxInicio = horas.indexOf(item.horaInicio);
                const idxFin = horas.indexOf(item.horaFin);
                if (filtros.fecha && item.fecha !== filtros.fecha) return false;
                if (filtros.horaInicio && idxInicio < idxFiltroInicio) return false;
                if (filtros.horaFin && idxFin > idxFiltroFin) return false;
                return true;
            };

            ausencias = ausencias.filter(filtrarPorHoras);
            guardias = guardias.filter(filtrarPorHoras);

            absenceList.innerHTML = "";

            if (ausencias.length === 0 && guardias.length === 0) {
                absenceList.innerHTML = "<li>No hay datos disponibles con los filtros seleccionados.</li>";
                return;
            }

            // Mostrar ausencias
            ausencias.forEach(a => {
                const li = document.createElement("li");
                li.textContent = `[AUSENCIA] - Nombre: ${a.nombreProfesor} | Fecha: ${a.fecha} | De ${a.horaInicio} a ${a.horaFin} | Motivo: ${a.motivo}`;
                absenceList.appendChild(li);
            });

            // Mostrar guardias
            guardias.forEach(g => {
                const li = document.createElement("li");
                li.textContent = `[GUARDIA] - Nombre: ${g.nombreProfesor} | Fecha: ${g.fecha} | De ${g.horaInicio} a ${g.horaFin}`;
                absenceList.appendChild(li);
            });

        } catch (error) {
            console.error("Error al cargar datos:", error);
            absenceList.innerHTML = "<li>Error cargando los datos.</li>";
        }
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const fecha = fechaInput.value;
        const inicio = horaInicio.value;
        const fin = horaFin.value;

        const startIndex = horas.indexOf(inicio);
        const endIndex = horas.indexOf(fin);

        if (inicio && fin && startIndex > endIndex) {
            alert("La hora de inicio no puede ser posterior a la de fin.");
            return;
        }

        const filtros = {};
        if (fecha) filtros.fecha = fecha;
        if (inicio) filtros.horaInicio = inicio;
        if (fin) filtros.horaFin = fin;

        cargarDatos(filtros);
    });

    horaInicio.addEventListener('change', actualizarHorasFin);

    // Carga inicial
    cargarDatos();
});