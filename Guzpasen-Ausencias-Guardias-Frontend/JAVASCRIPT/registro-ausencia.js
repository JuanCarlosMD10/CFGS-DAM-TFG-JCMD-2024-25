document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.absence-form');
    const successMessage = document.getElementById('success-message');
    const horaInicio = document.getElementById('horaInicio');
    const horaFin = document.getElementById('horaFin');
    const fechaInput = document.getElementById('date');
    const comentarioTareas = document.getElementById('comentarioTareas');
    const motivo = document.getElementById('reason');
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Establecer la fecha mínima como hoy
    const hoy = new Date();
    fechaInput.min = hoy.toISOString().split('T')[0];

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        // Validar que la hora fin no sea anterior a la hora inicio
        if (horas.indexOf(horaInicio.value) > horas.indexOf(horaFin.value)) {
            alert('La hora de fin no puede ser anterior a la de inicio');
            return;
        }

        const idProfesor = obtenerIdProfesor(); // Debes implementar según tu sistema

        const ausencia = {
            fecha: fechaInput.value,
            horaInicio: horaInicio.value,
            horaFin: horaFin.value,
            motivo: motivo.value,
            estado: "PENDIENTE_DE_GUARDIA",
            tareaAlumnado: comentarioTareas.value,
            idProfesor: {
                idProfesor: idProfesor
            },
            idZona: {
                idZona: 1 // Ajusta según corresponda
            }
        };

        try {
            const response = await fetch("http://localhost:8080/ausencias", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(ausencia)
            });

            if (response.ok) {
                successMessage.style.display = 'block';
                form.reset();

                setTimeout(() => {
                    successMessage.style.display = 'none';
                    window.location.href = '../HTML/index-ausencia-guardia.html';
                }, 3000);
            } else {
                const errorData = await response.json();
                alert(`Error al registrar la ausencia: ${errorData.message || response.statusText}`);
                console.error('Error detallado:', errorData);
            }
        } catch (error) {
            alert("No se pudo conectar con el servidor");
            console.error('Error de conexión:', error);
        }
    });

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

    horaInicio.addEventListener('change', actualizarHorasFin);

    // Obtener ID profesor (ejemplo)
    function obtenerIdProfesor() {
        return localStorage.getItem('idProfesor') || 1;
    }
});