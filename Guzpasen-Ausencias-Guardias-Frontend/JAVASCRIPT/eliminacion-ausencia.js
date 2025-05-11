// Obtener los elementos
const confirmButton = document.querySelector('.confirm-btn');
const modal = document.getElementById('confirmation-modal');
const cancelButton = document.getElementById('cancel-btn-modal');
const confirmModalButton = document.getElementById('confirm-btn-modal');
const successMessage = document.getElementById('success-message');

// Mostrar el modal al hacer clic en el botón confirmar
confirmButton.addEventListener('click', function () {
    modal.style.display = 'flex';  // Muestra el modal
});

// Cerrar el modal sin hacer cambios al hacer clic en cancelar
cancelButton.addEventListener('click', function () {
    modal.style.display = 'none';  // Oculta el modal
});

// Confirmar la acción al hacer clic en confirmar
confirmModalButton.addEventListener('click', function () {
    modal.style.display = 'none';  // Ocultar el modal

    // Mostrar el mensaje de éxito
    successMessage.style.display = 'block';

    // Opcionalmente, ocultar el mensaje después de unos segundos
    setTimeout(function () {
        successMessage.style.display = 'none';
    }, 3000); // El mensaje se oculta después de 3 segundos
});