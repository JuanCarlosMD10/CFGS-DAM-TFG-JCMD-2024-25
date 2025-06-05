document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('loginForm');
    const errorMensaje = document.getElementById('errorMensaje');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const email = document.getElementById('email').value.trim();
        const clave = document.getElementById('clave').value;

        const datosLogin = {
            email: email,
            clave: clave
        };

        try {
            const response = await fetch('http://localhost:8080/app/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(datosLogin)
            });

            if (response.ok) {
                const token = await response.text();
                localStorage.setItem('token', token);
                window.location.href = '/index.html';
            } else if (response.status === 401) {
                const mensaje = await response.text();
                mostrarError(mensaje || 'Credenciales incorrectas.');
            } else {
                mostrarError('Error al iniciar sesión. Intente más tarde.');
            }
        } catch (error) {
            mostrarError('No se pudo conectar con el servidor.');
        }
    });

    function mostrarError(mensaje) {
        errorMensaje.style.display = 'block';
        errorMensaje.textContent = mensaje;
    }
});