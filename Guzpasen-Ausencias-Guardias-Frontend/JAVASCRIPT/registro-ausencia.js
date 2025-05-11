document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.absence-form');
    const successMessage = document.getElementById('success-message');
    const franjaInicio = document.getElementById('franjaInicio');
    const franjaFin = document.getElementById('franjaFin');
    const fechaInput = document.getElementById('date'); // ⬅️ Nuevo
    const franjas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // ⬇️ Evitar fechas pasadas
    const hoy = new Date().toISOString().split('T')[0];
    fechaInput.min = hoy;

    // Mostrar mensaje de éxito al enviar
    form.addEventListener('submit', function (e) {
        e.preventDefault(); // Previene el envío real

        // Muestra el mensaje
        successMessage.style.display = 'block';

        // Oculta el mensaje después de 3 segundos
        setTimeout(() => {
            successMessage.style.display = 'none';
        }, 3000);

        // Reinicia el formulario
        form.reset();

        // Restablece el mínimo de nuevo después del reset
        fechaInput.min = hoy;
    });

    // Control de franjas horarias
    function actualizarFranjasFin() {
        const inicioSeleccionado = franjaInicio.value;

        // Habilita todas las opciones primero
        Array.from(franjaFin.options).forEach(option => {
            option.disabled = false;
        });

        if (inicioSeleccionado) {
            const indexInicio = franjas.indexOf(inicioSeleccionado);

            Array.from(franjaFin.options).forEach(option => {
                if (option.value && franjas.indexOf(option.value) < indexInicio) {
                    option.disabled = true;
                }
            });

            if (franjaFin.value && franjas.indexOf(franjaFin.value) < indexInicio) {
                franjaFin.value = '';
            }
        }
    }

    franjaInicio.addEventListener('change', actualizarFranjasFin);
});