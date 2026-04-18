let stompCliente = null;

const onConnectSocket = () => {
    stompCliente.subscribe('/tema/mensajes', (mensaje) => {
        mostrarMensaje(mensaje.body);
    });
};

const conectarWS = () => {

    stompCliente = new StompJs.Client({
        brokerURL: 'ws://localhost:8080/websocket'
    });

    stompCliente.onConnect = onConnectSocket;

    stompCliente.activate();
};

const enviarMensaje = () => {
    let txtNombre = document.getElementById('txtNombre');
    let txtMensaje = document.getElementById('txtMensaje');

    stompCliente.publish({
        destination: '/app/envio',
        body: JSON.stringify({
            nombre: txtNombre.value,
            mensaje: txtMensaje.value  
        })
    });
};

const mostrarMensaje = (mensaje) => {
    const body = JSON.parse(mensaje);
    const ULMensajes = document.getElementById('ULMensajes');
    const mensajeLI = document.createElement('li');

    mensajeLI.classList.add('list-group-item');
    mensajeLI.innerHTML = `<strong>${body.nombre}</strong>: ${body.mensaje}`;

    ULMensajes.appendChild(mensajeLI);
};

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btnEnviar').addEventListener('click', (e) => {
        e.preventDefault();
        enviarMensaje();
    });

    conectarWS();
});