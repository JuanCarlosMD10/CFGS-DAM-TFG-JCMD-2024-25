document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.filter-form');
    const franjaInicio = document.getElementById('franjaInicio');
    const franjaFin = document.getElementById('franjaFin');
    const fechaInput = document.querySelector('input[name="fecha"]');

    const franjas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // Evitar fechas pasadas
    const hoy = new Date().toISOString().split('T')[0];
    fechaInput.min = hoy;

    // Actualiza franjaFin al cambiar franjaInicio
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

            // Si la selección actual de fin es inválida, se limpia
            if (franjaFin.value && franjas.indexOf(franjaFin.value) < indexInicio) {
                franjaFin.value = '';
            }
        }
    }

    // Validación final al enviar
    form.addEventListener('submit', function (e) {
        const startIndex = franjas.indexOf(franjaInicio.value);
        const endIndex = franjas.indexOf(franjaFin.value);

        if (startIndex === -1 || endIndex === -1) {
            alert("Por favor selecciona tanto la franja de inicio como la de fin.");
            e.preventDefault();
            return;
        }

        if (startIndex > endIndex) {
            alert("La franja de inicio no puede ser posterior a la de fin.");
            e.preventDefault();
        }
    });

    franjaInicio.addEventListener('change', actualizarFranjasFin);
});