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

    if(txtNombre.value.trim() === '' || txtMensaje.value.trim() === '') {
        return; 
    }

    stompCliente.publish({
        destination: '/app/envio',
        body: JSON.stringify({
            nombre: txtNombre.value,
            mensaje: txtMensaje.value  
        })
    });
    txtMensaje.value = '';
};

const mostrarMensaje = (mensaje) => {
    const body = JSON.parse(mensaje);
    const ULMensajes = document.getElementById('ULMensajes');
    
    const mensajeLI = document.createElement('li');
    mensajeLI.classList.add('mensaje-contenedor');

    mensajeLI.innerHTML =`<div class="burbuja"> <div class="nombre-usuario"><strong>${body.nombre}</strong></div>
            <div>${body.mensaje}</div> </div>`;
   
    ULMensajes.appendChild(mensajeLI);

    const chatBox = document.getElementById('mensajes');
    chatBox.scrollTop = chatBox.scrollHeight;
};

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btnEnviar').addEventListener('click', (e) => {
        e.preventDefault();
        enviarMensaje();
    });

    conectarWS();
});