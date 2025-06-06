// Espera a que el DOM esté completamente cargado antes de ejecutar el código
document.addEventListener('DOMContentLoaded', () => {
    // Obtiene el formulario de login por su ID
    const form = document.getElementById('loginForm');
    // Obtiene el elemento donde se mostrarán mensajes de error
    const errorMensaje = document.getElementById('errorMensaje');

    // Añade un evento para cuando se envíe el formulario
    form.addEventListener('submit', async (e) => {
        e.preventDefault(); // Previene el envío tradicional del formulario para manejarlo con JS

        // Obtiene los valores ingresados en los campos email y clave, eliminando espacios extra
        const email = document.getElementById('email').value.trim();
        const clave = document.getElementById('clave').value;

        // Crea un objeto con los datos para enviar al servidor
        const datosLogin = {
            email: email,
            clave: clave
        };

        try {
            // Realiza una petición POST al endpoint de login enviando los datos en formato JSON
            const response = await fetch('http://localhost:8080/app/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'  // Indicamos que enviamos JSON
                },
                body: JSON.stringify(datosLogin) // Convertimos el objeto datosLogin a cadena JSON
            });

            // Si la respuesta es exitosa (status 200-299)
            if (response.ok) {
                // Obtiene el token de autenticación como texto plano
                const token = await response.text();
                // Guarda el token en el almacenamiento local para usarlo luego
                localStorage.setItem('token', token);
                // Redirige al usuario a la página principal
                window.location.href = '/index.html';

                // Si la respuesta fue un error de autenticación (401)
            } else if (response.status === 401) {
                // Obtiene el mensaje de error enviado por el backend
                const mensaje = await response.text();
                // Muestra el mensaje de error personalizado o uno genérico
                mostrarError(mensaje || 'Credenciales incorrectas.');

                // Para cualquier otro tipo de error HTTP
            } else {
                mostrarError('Error al iniciar sesión. Intente más tarde.');
            }

            // Captura errores de conexión o inesperados en la petición fetch
        } catch (error) {
            mostrarError('No se pudo conectar con el servidor.');
        }
    });

    // Función para mostrar mensajes de error en pantalla
    function mostrarError(mensaje) {
        errorMensaje.style.display = 'block';  // Hace visible el mensaje de error
        errorMensaje.textContent = mensaje;     // Establece el texto del mensaje
    }
});