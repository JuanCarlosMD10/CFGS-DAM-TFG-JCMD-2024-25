// Espera a que el contenido del DOM esté completamente cargado antes de ejecutar el script
document.addEventListener('DOMContentLoaded', function () {
    // Lista de horas disponibles
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Elementos del formulario y del DOM
    const form = document.querySelector('.filter-form');
    const successMessage = document.getElementById('success-message');
    const horaInicio = document.querySelector('.filter-form select[name="horaInicio"]');
    const horaFin = document.querySelector('.filter-form select[name="horaFin"]');
    const fechaInput = document.querySelector('.filter-form input[name="fecha"]');
    const absenceList = document.getElementById("absence-list");

    // Restringe la fecha mínima a hoy
    const hoy = new Date().toISOString().split('T')[0];
    fechaInput.min = hoy;

    // Función que actualiza las opciones de horaFin según la horaInicio seleccionada
    function actualizarHorasFin(selectInicio, selectFin, idxFinMaximo = horas.length - 1) {
        const inicioSeleccionado = selectInicio.value;
        Array.from(selectFin.options).forEach(option => option.disabled = false);

        if (inicioSeleccionado) {
            const indexInicio = horas.indexOf(inicioSeleccionado);

            // Desactiva las opciones anteriores a la hora de inicio y posteriores al índice máximo permitido
            Array.from(selectFin.options).forEach(option => {
                const idx = horas.indexOf(option.value);
                if (option.value && (idx < indexInicio || idx > idxFinMaximo)) {
                    option.disabled = true;
                }
            });

            // Limpia la selección si ya no es válida
            if (selectFin.value) {
                const idxFinActual = horas.indexOf(selectFin.value);
                if (idxFinActual < indexInicio || idxFinActual > idxFinMaximo) {
                    selectFin.value = '';
                }
            }
        } else {
            // Si no se selecciona hora de inicio, se deshabilitan todas las opciones de fin
            Array.from(selectFin.options).forEach(option => {
                if (option.value) {
                    option.disabled = true;
                }
            });
        }
    }

    // Deshabilita todas las opciones de horaFin
    function deshabilitarHoraFin(selectFin) {
        Array.from(selectFin.options).forEach(option => {
            if (option.value) {
                option.disabled = true;
            }
        });
    }

    // Función para cargar ausencias desde el backend y aplicar filtros si es necesario
    async function cargarAusencias(filtros = {}) {
        absenceList.innerHTML = "<li>Cargando ausencias...</li>";

        try {
            const response = await fetch("http://localhost:8080/ausencias/consultar");
            if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

            let ausencias = await response.json();

            // Filtra solo las ausencias pendientes de asignar guardia
            ausencias = ausencias.filter(ausencia => ausencia.estado === "PENDIENTE_DE_GUARDIA");

            // Aplica los filtros de fecha y horas si están definidos
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

            // Muestra la lista de ausencias con opción a seleccionar
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

            // Guarda las ausencias en una variable global para usarlas en el modal
            window.ausenciasActuales = ausencias;

        } catch (error) {
            console.error("Error al cargar ausencias:", error);
            absenceList.innerHTML = "<li>Error cargando las ausencias</li>";
        }
    }

    // Manejador del evento submit del formulario de filtros
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const fecha = fechaInput.value.trim();
        const inicio = horaInicio.value;
        const fin = horaFin.value;

        const startIndex = horas.indexOf(inicio);
        const endIndex = horas.indexOf(fin);

        // Valida que la hora de inicio no sea posterior a la de fin
        if (inicio && fin && startIndex > endIndex) {
            alert("La hora de inicio no puede ser posterior a la de fin.");
            return;
        }

        // Construye objeto de filtros y carga las ausencias con ellos
        const filtros = {};
        if (fecha) filtros.fecha = fecha;
        if (inicio) filtros.horaInicio = inicio;
        if (fin) filtros.horaFin = fin;

        cargarAusencias(filtros);
    });

    // Cuando cambia la hora de inicio, actualiza las opciones de fin
    horaInicio.addEventListener('change', () => actualizarHorasFin(horaInicio, horaFin));

    // Deshabilita inicialmente todas las horas de fin
    deshabilitarHoraFin(horaFin);

    // Referencias a elementos del modal
    const openModalBtn = document.getElementById('abrirModalBtn');
    const modal = document.getElementById('confirmation-modal');
    const cancelModalBtn = document.getElementById('cancel-modal');
    const confirmModalBtn = document.getElementById('confirm-modal');
    const modalHoraInicio = modal.querySelector('select[name="horaInicio"]');
    const modalHoraFin = modal.querySelector('select[name="horaFin"]');
    const profesorInput = modal.querySelector('input[name="profesor"]');

    // Evento para abrir el modal de confirmación
    openModalBtn.addEventListener('click', () => {
        const selectedAusencia = document.querySelector('input[name="ausenciaSeleccionada"]:checked');

        if (!selectedAusencia) {
            alert("Debe seleccionar una ausencia antes de continuar.");
            return;
        }

        const selectedIndex = parseInt(selectedAusencia.value);
        const ausencia = window.ausenciasActuales[selectedIndex];
        const idxInicioAusencia = horas.indexOf(ausencia.horaInicio);
        const idxFinAusencia = horas.indexOf(ausencia.horaFin);

        // Restringe las opciones de horaInicio en el modal al rango de la ausencia seleccionada
        Array.from(modalHoraInicio.options).forEach(option => {
            const idx = horas.indexOf(option.value);
            option.disabled = option.value && (idx < idxInicioAusencia || idx > idxFinAusencia);
        });

        // Desactiva inicialmente las opciones de horaFin
        Array.from(modalHoraFin.options).forEach(option => {
            option.disabled = true;
        });

        // Actualiza opciones de horaFin cuando cambia horaInicio en el modal
        modalHoraInicio.addEventListener('change', () => {
            const idxInicioSeleccionado = horas.indexOf(modalHoraInicio.value);

            Array.from(modalHoraFin.options).forEach(option => {
                const idx = horas.indexOf(option.value);
                option.disabled = option.value && (idx < idxInicioSeleccionado || idx > idxFinAusencia);
            });

            if (modalHoraFin.value) {
                const idxFinActual = horas.indexOf(modalHoraFin.value);
                if (idxFinActual < idxInicioSeleccionado || idxFinActual > idxFinAusencia) {
                    modalHoraFin.value = '';
                }
            }
        });

        modal.style.display = 'flex';
        cargarProfesores();
    });

    // Cierra el modal y limpia sus campos al pulsar "Cancelar"
    cancelModalBtn.addEventListener('click', () => {
        modal.style.display = 'none';
        profesorInput.value = '';
        modalHoraInicio.value = '';
        modalHoraFin.value = '';
        deshabilitarHoraFin(modalHoraFin);
    });

    // Evento de confirmación del modal para registrar una guardia
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

        // Construye el payload para registrar la guardia
        const payload = {
            idProfesor: parseInt(profesorId),
            idAusencia: ausenciaSeleccionada.idAusencia,
            horaInicio: horaInicioVal,
            horaFin: horaFinVal
        };

        // Realiza la petición al backend para registrar la guardia
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

            // Oculta el modal y muestra un mensaje de éxito
            modal.style.display = 'none';
            successMessage.style.display = 'block';
            setTimeout(() => {
                successMessage.style.display = 'none';
            }, 3000);

            // Recarga las ausencias después de registrar la guardia
            cargarAusencias();

        } catch (error) {
            console.error("Error al registrar la guardia:", error);
            alert("Ocurrió un error al registrar la guardia.");
        }
    });

    // Cierra el modal si se hace clic fuera del contenido del mismo
    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Vuelve a aplicar la lógica de actualización de horas en el modal
    modalHoraInicio.addEventListener('change', () => actualizarHorasFin(modalHoraInicio, modalHoraFin));

    // Carga inicial de ausencias
    cargarAusencias();

    // Función que carga los profesores desde el backend y excluye al profesor ausente
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

            // Filtra al profesor que está ausente para no asignarle su propia guardia
            const profesoresFiltrados = profesores.filter(prof => prof.idUsuario !== profesorAusente);

            if (!profesoresFiltrados || profesoresFiltrados.length === 0) {
                selectProfesor.innerHTML = '<option value="">No hay profesores disponibles</option>';
                return;
            }

            // Muestra la lista de profesores disponibles
            selectProfesor.innerHTML = '<option value="">Seleccione un profesor</option>';
            profesoresFiltrados.forEach(profesor => {
                const option = document.createElement("option");
                option.value = profesor.idUsuario;
                option.textContent = profesor.nombre + ' ' + profesor.apellidos;
                selectProfesor.appendChild(option);
            });

        } catch (error) {
            selectProfesor.innerHTML = '<option value="">Error al cargar los profesores</option>';
            console.error("Error cargando profesores:", error);
        }
    }

    // Añade un listener duplicado para cancelar el modal (opcional, ya está arriba también)
    document.getElementById('cancel-modal').addEventListener('click', () => {
        document.getElementById('confirmation-modal').style.display = 'none';
    });
});