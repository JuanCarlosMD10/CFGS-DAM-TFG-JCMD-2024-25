document.addEventListener('DOMContentLoaded', function () {
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    const form = document.querySelector('.filter-form');
    const successMessage = document.getElementById('success-message');
    const horaInicio = document.querySelector('.filter-form select[name="horaInicio"]');
    const horaFin = document.querySelector('.filter-form select[name="horaFin"]');
    const fechaInput = document.querySelector('.filter-form input[name="fecha"]');
    const absenceList = document.getElementById("absence-list");

    const hoy = new Date().toISOString().split('T')[0];
    fechaInput.min = hoy;

    function actualizarHorasFin(selectInicio, selectFin) {
        const inicioSeleccionado = selectInicio.value;

        Array.from(selectFin.options).forEach(option => option.disabled = false);

        if (inicioSeleccionado) {
            const indexInicio = horas.indexOf(inicioSeleccionado);
            Array.from(selectFin.options).forEach(option => {
                if (option.value && horas.indexOf(option.value) < indexInicio) {
                    option.disabled = true;
                }
            });

            if (selectFin.value && horas.indexOf(selectFin.value) < indexInicio) {
                selectFin.value = '';
            }
        }
    }

    function deshabilitarHoraFin(selectFin) {
        Array.from(selectFin.options).forEach(option => {
            if (option.value) {
                option.disabled = true;
            }
        });
    }

    async function cargarAusencias(filtros = {}) {
        absenceList.innerHTML = "<li>Cargando ausencias...</li>";

        try {
            const response = await fetch("http://localhost:8080/ausencias/consultar");

            if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

            let ausencias = await response.json();

            ausencias = ausencias.filter(ausencia => {
                const fechaFiltro = filtros.fecha || null;
                const horaInicioFiltro = filtros.horaInicio || null;
                const horaFinFiltro = filtros.horaFin || null;

                if (fechaFiltro && ausencia.fecha !== fechaFiltro) return false;

                const idxAusenciaInicio = horas.indexOf(ausencia.horaInicio);
                const idxAusenciaFin = horas.indexOf(ausencia.horaFin);
                const idxFiltroInicio = horas.indexOf(horaInicioFiltro);
                const idxFiltroFin = horas.indexOf(horaFinFiltro);

                if (horaInicioFiltro && idxAusenciaInicio < idxFiltroInicio) return false;
                if (horaFinFiltro && idxAusenciaFin > idxFiltroFin) return false;

                return true;
            });

            absenceList.innerHTML = "";

            if (!ausencias || ausencias.length === 0) {
                absenceList.innerHTML = "<li>No hay ausencias disponibles con los filtros seleccionados.</li>";
                return;
            }

            ausencias.forEach((ausencia, index) => {
                const li = document.createElement("li");
                const radioId = `ausencia-${index}`;
                li.innerHTML = `
                <label for="${radioId}">
                    <input type="radio" name="ausenciaSeleccionada" value="${index}" id="${radioId}">
                    Nombre: ${ausencia.nombreProfesor} | Fecha: ${ausencia.fecha} | De ${ausencia.horaInicio} a ${ausencia.horaFin} | Motivo: ${ausencia.motivo}
                </label>
            `;
                absenceList.appendChild(li);
            });

            window.ausenciasActuales = ausencias;

        } catch (error) {
            console.error("Error al cargar ausencias:", error);
            absenceList.innerHTML = "<li>Error cargando las ausencias</li>";
        }
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const fecha = fechaInput.value.trim();
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

        cargarAusencias(filtros);
    });

    horaInicio.addEventListener('change', () => actualizarHorasFin(horaInicio, horaFin));
    deshabilitarHoraFin(horaFin);

    // Modal elements
    const openModalBtn = document.getElementById('abrirModalBtn');
    const modal = document.getElementById('confirmation-modal');
    const cancelModalBtn = document.getElementById('cancel-modal');
    const confirmModalBtn = document.getElementById('confirm-modal');
    const modalHoraInicio = modal.querySelector('select[name="horaInicio"]');
    const modalHoraFin = modal.querySelector('select[name="horaFin"]');
    const profesorInput = modal.querySelector('input[name="profesor"]');

    openModalBtn.addEventListener('click', () => {
        const selectedAusencia = document.querySelector('input[name="ausenciaSeleccionada"]:checked');

        if (!selectedAusencia) {
            alert("Debe seleccionar una ausencia antes de continuar.");
            return;
        }

        modal.style.display = 'flex';
        deshabilitarHoraFin(modalHoraFin);
        cargarProfesores();
    });

    cancelModalBtn.addEventListener('click', () => {
        modal.style.display = 'none';
        profesorInput.value = '';
        modalHoraInicio.value = '';
        modalHoraFin.value = '';
        deshabilitarHoraFin(modalHoraFin);
    });

    confirmModalBtn.addEventListener('click', async () => {
        const profesorId = document.getElementById("modalProfesor").value;
        const horaInicioVal = modalHoraInicio.value;
        const horaFinVal = modalHoraFin.value;

        const selectedAusenciaRadio = document.querySelector('input[name="ausenciaSeleccionada"]:checked');

        if (!profesorId) {
            alert("Por favor, seleccione un profesor.");
            return;
        }

        if (!horaInicioVal || !horaFinVal) {
            alert("Debe seleccionar hora de inicio y fin.");
            return;
        }

        if (!selectedAusenciaRadio) {
            alert("Debe seleccionar una ausencia.");
            return;
        }

        const ausenciaSeleccionada = window.ausenciasActuales[parseInt(selectedAusenciaRadio.value)];

        const payload = {
            idProfesor: parseInt(profesorId),
            idAusencia: ausenciaSeleccionada.idAusencia,
            horaInicio: horaInicioVal,
            horaFin: horaFinVal
        };

        try {
            const response = await fetch('http://localhost:8080/guardias/registrar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                throw new Error(`Error HTTP: ${response.status}`);
            }

            modal.style.display = 'none';
            successMessage.style.display = 'block';
            setTimeout(() => {
                successMessage.style.display = 'none';
            }, 3000);

            cargarAusencias();

        } catch (error) {
            console.error("Error al registrar la guardia:", error);
            alert("Ocurrió un error al registrar la guardia.");
        }
    });

    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    modalHoraInicio.addEventListener('change', () => actualizarHorasFin(modalHoraInicio, modalHoraFin));

    cargarAusencias();

    async function cargarProfesores() {
        const selectProfesor = document.getElementById("modalProfesor");
        selectProfesor.innerHTML = '<option value="">Cargando profesores...</option>';

        try {
            const response = await fetch("http://localhost:8080/usuarios/profesores");

            if (!response.ok) {
                throw new Error(`Error HTTP: ${response.status}`);
            }

            const profesores = await response.json();

            const selectedAusenciaRadio = document.querySelector('input[name="ausenciaSeleccionada"]:checked');
            let profesorAusente = null;

            if (selectedAusenciaRadio) {
                const ausenciaSeleccionada = window.ausenciasActuales[parseInt(selectedAusenciaRadio.value)];
                profesorAusente = ausenciaSeleccionada?.idProfesor;
            }

            const profesoresFiltrados = profesores.filter(prof => prof.idUsuario !== profesorAusente);

            if (!profesoresFiltrados || profesoresFiltrados.length === 0) {
                selectProfesor.innerHTML = '<option value="">No hay profesores disponibles</option>';
                return;
            }

            selectProfesor.innerHTML = '<option value="">Seleccione un profesor</option>';
            profesoresFiltrados.forEach(profesor => {
                const option = document.createElement("option");
                option.value = profesor.idUsuario;
                option.textContent = profesor.nombre + ' ' + profesor.apellidos;
                selectProfesor.appendChild(option);
            });

        } catch (error) {
            selectProfesor.innerHTML = '<option value="">Error al cargar</option>';
            console.error("Error cargando profesores:", error);
        }
    }

    document.getElementById('cancel-modal').addEventListener('click', () => {
        document.getElementById('confirmation-modal').style.display = 'none';
    });
});