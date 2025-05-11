document.addEventListener('DOMContentLoaded', function () {
    const franjas = ["PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA"];

    // ------------------------- FORMULARIO PRINCIPAL -------------------------
    const form = document.querySelector('.filter-form');
    const successMessage = document.getElementById('success-message');
    const franjaInicio = document.querySelector('.filter-form select[name="franjaInicio"]');
    const franjaFin = document.querySelector('.filter-form select[name="franjaFin"]');
    const fechaInput = document.querySelector('.filter-form input[name="fecha"]');

    const hoy = new Date().toISOString().split('T')[0];
    fechaInput.min = hoy;

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        successMessage.style.display = 'block';
        setTimeout(() => {
            successMessage.style.display = 'none';
        }, 3000);
        form.reset();
        fechaInput.min = hoy;
        deshabilitarFranjaFin(franjaFin);
    });

    franjaInicio.addEventListener('change', function () {
        actualizarFranjasFin(franjaInicio, franjaFin);
    });

    deshabilitarFranjaFin(franjaFin); // Inicia deshabilitado

    // ------------------------- MODAL -------------------------
    const openModalBtn = document.getElementById('open-confirm-modal');
    const modal = document.getElementById('confirmation-modal');
    const cancelModalBtn = document.getElementById('cancel-modal');
    const confirmModalBtn = document.getElementById('confirm-modal');

    const modalFranjaInicio = modal.querySelector('select[name="franjaInicio"]');
    const modalFranjaFin = modal.querySelector('select[name="franjaFin"]');
    const profesorInput = modal.querySelector('input[name="profesor"]');

    openModalBtn.addEventListener('click', () => {
        modal.style.display = 'flex';
        deshabilitarFranjaFin(modalFranjaFin);
    });

    cancelModalBtn.addEventListener('click', () => {
        modal.style.display = 'none';
        modal.querySelector('form')?.reset?.();
    });

    confirmModalBtn.addEventListener('click', () => {
        if (!profesorInput.value.trim()) {
            alert("Por favor, indique el nombre del profesor.");
            return;
        }

        if (!modalFranjaInicio.value || !modalFranjaFin.value) {
            alert("Debe seleccionar hora de inicio y fin.");
            return;
        }

        modal.style.display = 'none';
        successMessage.style.display = 'block';
        setTimeout(() => {
            successMessage.style.display = 'none';
        }, 3000);
    });

    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    modalFranjaInicio.addEventListener('change', function () {
        actualizarFranjasFin(modalFranjaInicio, modalFranjaFin);
    });

    // ------------------------- FUNCIONES AUXILIARES -------------------------
    function actualizarFranjasFin(selectInicio, selectFin) {
        const inicioSeleccionado = selectInicio.value;

        Array.from(selectFin.options).forEach(option => {
            option.disabled = false;
        });

        if (inicioSeleccionado) {
            const indexInicio = franjas.indexOf(inicioSeleccionado);

            Array.from(selectFin.options).forEach(option => {
                if (option.value && franjas.indexOf(option.value) < indexInicio) {
                    option.disabled = true;
                }
            });

            if (selectFin.value && franjas.indexOf(selectFin.value) < indexInicio) {
                selectFin.value = '';
            }
        }
    }

    function deshabilitarFranjaFin(selectFin) {
        Array.from(selectFin.options).forEach(option => {
            if (option.value) {
                option.disabled = true;
            }
        });
    }
});