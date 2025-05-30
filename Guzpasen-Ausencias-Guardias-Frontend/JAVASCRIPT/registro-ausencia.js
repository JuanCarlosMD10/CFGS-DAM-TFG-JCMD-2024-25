// Espera a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function () {
    // Referencias a elementos del formulario
    const form = document.querySelector('.absence-form');
    const successMessage = document.getElementById('success-message');
    const horaInicio = document.getElementById('horaInicio');
    const horaFin = document.getElementById('horaFin');
    const fechaInput = document.getElementById('date');
    const comentarioTareas = document.getElementById('comentarioTareas');
    const motivo = document.getElementById('reason');

    // Horas disponibles en el sistema
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Establece la fecha mínima del campo de fecha como la fecha actual
    const hoy = new Date();
    fechaInput.min = hoy.toISOString().split('T')[0];

    // Maneja el envío del formulario
    form.addEventListener('submit', async function (e) {
        e.preventDefault(); // Previene recarga de página

        // Validación: hora fin no puede ser anterior a hora inicio
        if (horas.indexOf(horaInicio.value) > horas.indexOf(horaFin.value)) {
            alert('La hora de fin no puede ser anterior a la de inicio');
            return;
        }

        // Obtiene el ID del profesor (desde almacenamiento local o valor por defecto)
        // En este caso coge el id por defecto 1 ya que aún no tengo login y habrá que coger del
        // inicio de sesión el id del profesor que se loguea
        const idProfesor = obtenerIdProfesor();

        // Crea el objeto de ausencia a enviar
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
                idZona: 1 // Puede ajustarse según el contexto
            }
        };

        // Intenta enviar los datos al servidor
        try {
            const response = await fetch("http://localhost:8080/ausencias", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(ausencia)
            });

            // Verifica si la respuesta fue exitosa
            if (response.ok) {
                successMessage.style.display = 'block';
                form.reset();

                // Redirige tras 3 segundos
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

    // Actualiza las opciones válidas de hora fin según la hora inicio seleccionada
    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicio.value;

        // Habilita todas las opciones primero
        Array.from(horaFin.options).forEach(option => {
            option.disabled = false;
        });

        // Deshabilita opciones anteriores a la hora de inicio
        if (inicioSeleccionado) {
            const indexInicio = horas.indexOf(inicioSeleccionado);

            Array.from(horaFin.options).forEach(option => {
                if (option.value && horas.indexOf(option.value) < indexInicio) {
                    option.disabled = true;
                }
            });

            // Borra la selección si ya no es válida
            if (horaFin.value && horas.indexOf(horaFin.value) < indexInicio) {
                horaFin.value = '';
            }
        }
    }

    // Actualiza horas fin al cambiar hora inicio
    horaInicio.addEventListener('change', actualizarHorasFin);

    // Recupera el ID del profesor desde el almacenamiento local o devuelve 1 por defecto
    function obtenerIdProfesor() {
        return localStorage.getItem('idProfesor') || 1;
    }
});