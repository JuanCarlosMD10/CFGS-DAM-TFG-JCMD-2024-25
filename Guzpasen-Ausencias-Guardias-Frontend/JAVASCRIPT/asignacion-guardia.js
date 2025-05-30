document.addEventListener('DOMContentLoaded', function () {
    // Array con las horas disponibles para seleccionar
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Referencias a elementos del DOM
    const form = document.querySelector('.filter-form');
    const successMessage = document.getElementById('success-message');
    const horaInicio = document.querySelector('.filter-form select[name="horaInicio"]');
    const horaFin = document.querySelector('.filter-form select[name="horaFin"]');
    const fechaInput = document.querySelector('.filter-form input[name="fecha"]');
    const absenceList = document.getElementById("absence-list");

    // Establece la fecha mínima para el input de fecha (hoy)
    const hoy = new Date().toISOString().split('T')[0];
    fechaInput.min = hoy;

    // Función para actualizar las opciones habilitadas en el select de hora fin
    function actualizarHorasFin(selectInicio, selectFin) {
        const inicioSeleccionado = selectInicio.value;

        // Habilita todas las opciones primero
        Array.from(selectFin.options).forEach(option => option.disabled = false);

        if (inicioSeleccionado) {
            const indexInicio = horas.indexOf(inicioSeleccionado);
            // Deshabilita opciones en horaFin que sean anteriores a la horaInicio seleccionada
            Array.from(selectFin.options).forEach(option => {
                if (option.value && horas.indexOf(option.value) < indexInicio) {
                    option.disabled = true;
                }
            });

            // Si la horaFin actual es menor que la horaInicio, la limpia
            if (selectFin.value && horas.indexOf(selectFin.value) < indexInicio) {
                selectFin.value = '';
            }
        }
    }

    // Función para deshabilitar todas las opciones del select de hora fin (usado inicialmente o para reset)
    function deshabilitarHoraFin(selectFin) {
        Array.from(selectFin.options).forEach(option => {
            if (option.value) {
                option.disabled = true;
            }
        });
    }

    // Función asíncrona para cargar las ausencias desde el servidor, aplicando filtros si los hay
    async function cargarAusencias(filtros = {}) {
        absenceList.innerHTML = "<li>Cargando ausencias...</li>"; // Mensaje mientras carga

        try {
            const response = await fetch("http://localhost:8080/ausencias/consultar");

            if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);

            let ausencias = await response.json();

            // Filtra las ausencias según los filtros de fecha y horas
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

            absenceList.innerHTML = ""; // Limpia la lista para rellenarla

            if (!ausencias || ausencias.length === 0) {
                absenceList.innerHTML = "<li>No hay ausencias disponibles con los filtros seleccionados.</li>";
                return;
            }

            // Crea la lista de ausencias con radio buttons para seleccionar una
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

            // Guarda las ausencias cargadas en una variable global para acceder desde otros lugares
            window.ausenciasActuales = ausencias;

        } catch (error) {
            console.error("Error al cargar ausencias:", error);
            absenceList.innerHTML = "<li>Error cargando las ausencias</li>";
        }
    }

    // Evento submit del formulario de filtro para cargar ausencias filtradas
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

        // Construye el objeto de filtros a enviar
        const filtros = {};
        if (fecha) filtros.fecha = fecha;
        if (inicio) filtros.horaInicio = inicio;
        if (fin) filtros.horaFin = fin;

        // Carga las ausencias con filtros aplicados
        cargarAusencias(filtros);
    });

    // Evento para actualizar las horas fin disponibles cuando cambia la hora inicio
    horaInicio.addEventListener('change', () => actualizarHorasFin(horaInicio, horaFin));

    // Inicialmente deshabilita todas las opciones de hora fin
    deshabilitarHoraFin(horaFin);

    // Elementos del modal para registrar una guardia
    const openModalBtn = document.getElementById('abrirModalBtn');
    const modal = document.getElementById('confirmation-modal');
    const cancelModalBtn = document.getElementById('cancel-modal');
    const confirmModalBtn = document.getElementById('confirm-modal');
    const modalHoraInicio = modal.querySelector('select[name="horaInicio"]');
    const modalHoraFin = modal.querySelector('select[name="horaFin"]');
    const profesorInput = modal.querySelector('input[name="profesor"]');

    // Evento para abrir el modal solo si hay una ausencia seleccionada
    openModalBtn.addEventListener('click', () => {
        const selectedAusencia = document.querySelector('input[name="ausenciaSeleccionada"]:checked');

        if (!selectedAusencia) {
            alert("Debe seleccionar una ausencia antes de continuar.");
            return;
        }

        modal.style.display = 'flex'; // Muestra el modal
        deshabilitarHoraFin(modalHoraFin); // Deshabilita opciones de hora fin en modal
        cargarProfesores(); // Carga la lista de profesores para asignar
    });

    // Evento para cerrar el modal y resetear los campos
    cancelModalBtn.addEventListener('click', () => {
        modal.style.display = 'none';
        profesorInput.value = '';
        modalHoraInicio.value = '';
        modalHoraFin.value = '';
        deshabilitarHoraFin(modalHoraFin);
    });

    // Evento para confirmar el registro de la guardia desde el modal
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

        // Obtiene la ausencia seleccionada según el índice guardado
        const ausenciaSeleccionada = window.ausenciasActuales[parseInt(selectedAusenciaRadio.value)];

        // Crea el objeto para enviar al backend con la información de la guardia
        const payload = {
            idProfesor: parseInt(profesorId),
            idAusencia: ausenciaSeleccionada.idAusencia,
            horaInicio: horaInicioVal,
            horaFin: horaFinVal
        };

        try {
            // Envía la solicitud POST para registrar la guardia
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

            // Cierra el modal y muestra mensaje de éxito por 3 segundos
            modal.style.display = 'none';
            successMessage.style.display = 'block';
            setTimeout(() => {
                successMessage.style.display = 'none';
            }, 3000);

            // Recarga la lista de ausencias tras registrar
            cargarAusencias();

        } catch (error) {
            console.error("Error al registrar la guardia:", error);
            alert("Ocurrió un error al registrar la guardia.");
        }
    });

    // Cierra el modal si se hace click fuera del contenido del mismo
    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Actualiza las horas fin en el modal cuando cambia la hora inicio en el modal
    modalHoraInicio.addEventListener('change', () => actualizarHorasFin(modalHoraInicio, modalHoraFin));

    // Carga inicial de ausencias sin filtros
    cargarAusencias();

    // Función para cargar los profesores disponibles para asignar a la guardia en el modal
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

            // Filtra el profesor que está ausente para no incluirlo en la lista de selección
            const profesoresFiltrados = profesores.filter(prof => prof.idUsuario !== profesorAusente);

            if (!profesoresFiltrados || profesoresFiltrados.length === 0) {
                selectProfesor.innerHTML = '<option value="">No hay profesores disponibles</option>';
                return;
            }

            selectProfesor.innerHTML = '<option value="">Seleccione un profesor</option>';
            // Crea las opciones en el select para cada profesor disponible
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

    // Evento para cerrar el modal al hacer click en cancelar (repetido para asegurar funcionamiento)
    document.getElementById('cancel-modal').addEventListener('click', () => {
        document.getElementById('confirmation-modal').style.display = 'none';
    });
});