// Espera a que el DOM esté completamente cargado antes de ejecutar el código
document.addEventListener('DOMContentLoaded', function () {
    // Referencias a elementos del formulario y otros necesarios
    const form = document.querySelector('.absence-form');
    const successMessage = document.getElementById('success-message');
    const horaInicio = document.getElementById('horaInicio');
    const horaFin = document.getElementById('horaFin');
    const fechaInput = document.getElementById('date');
    const comentarioTareas = document.getElementById('comentarioTareas');
    const motivo = document.getElementById('reason');

    // Array con el orden lógico de las horas para validación y control
    const horas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Establece la fecha mínima permitida en el input de fecha como la fecha actual (hoy)
    const hoy = new Date();
    fechaInput.min = hoy.toISOString().split('T')[0];

    // Escucha el evento submit del formulario para manejar el envío manualmente
    form.addEventListener('submit', async function (e) {
        e.preventDefault(); // Previene el envío normal del formulario

        // Valida que la hora fin no sea anterior a la hora inicio usando el índice del array horas
        if (horas.indexOf(horaInicio.value) > horas.indexOf(horaFin.value)) {
            alert('La hora de fin no puede ser anterior a la de inicio');
            return; // Detiene el envío si la validación falla
        }

        // Obtiene el ID del profesor, por ejemplo, de localStorage (debes implementar según tu sistema)
        const idProfesor = obtenerIdProfesor();

        // Construye el objeto ausencia con los datos que se enviarán al backend
        const ausencia = {
            fecha: fechaInput.value,
            horaInicio: horaInicio.value,
            horaFin: horaFin.value,
            motivo: motivo.value,
            estado: "PENDIENTE_DE_GUARDIA", // Estado por defecto para nuevas ausencias
            tareaAlumnado: comentarioTareas.value,
            idProfesor: {
                idProfesor: idProfesor // Se anida el idProfesor como objeto para el backend
            },
            idZona: {
                idZona: 1 // Zona fija o variable según tu caso
            }
        };

        try {
            // Hace la petición POST para registrar la ausencia en el servidor
            const response = await fetch("http://localhost:8080/ausencias/registrar", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(ausencia) // Convierte el objeto a JSON para enviarlo
            });

            // Si la respuesta es exitosa...
            if (response.ok) {
                // Muestra mensaje de éxito
                successMessage.style.display = 'block';
                // Resetea el formulario para limpiar campos
                form.reset();

                // Después de 3 segundos oculta el mensaje y redirige a la página principal
                setTimeout(() => {
                    successMessage.style.display = 'none';
                    window.location.href = '../HTML/index-ausencia-guardia.html';
                }, 3000);
            } else {
                // Si la respuesta no es exitosa, extrae el mensaje de error y lo muestra
                const errorData = await response.json();
                alert(`Error al registrar la ausencia: ${errorData.message || response.statusText}`);
                console.error('Error detallado:', errorData);
            }
        } catch (error) {
            // Captura errores de conexión o inesperados y los muestra en alerta y consola
            alert("No se pudo conectar con el servidor");
            console.error('Error de conexión:', error);
        }
    });

    // Función para actualizar las opciones del select de horaFin en función de la horaInicio seleccionada
    function actualizarHorasFin() {
        const inicioSeleccionado = horaInicio.value;

        // Primero habilita todas las opciones para resetear posibles deshabilitados previos
        Array.from(horaFin.options).forEach(option => {
            option.disabled = false;
        });

        // Si hay una horaInicio seleccionada, deshabilita opciones de horaFin anteriores a ella
        if (inicioSeleccionado) {
            const indexInicio = horas.indexOf(inicioSeleccionado);

            Array.from(horaFin.options).forEach(option => {
                if (option.value && horas.indexOf(option.value) < indexInicio) {
                    option.disabled = true; // Deshabilita opciones no válidas
                }
            });

            // Si la horaFin seleccionada ya no es válida, la resetea
            if (horaFin.value && horas.indexOf(horaFin.value) < indexInicio) {
                horaFin.value = '';
            }
        }
    }

    // Añade el event listener para actualizar horaFin cuando cambie horaInicio
    horaInicio.addEventListener('change', actualizarHorasFin);

    // Función para obtener el ID del profesor (ejemplo desde localStorage)
    function obtenerIdProfesor() {
        // Intenta obtenerlo de localStorage, sino devuelve 1 por defecto
        return localStorage.getItem('idProfesor') || 1;
    }
});