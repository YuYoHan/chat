const stompClient = Stomp.over(new SockJS('/ws/chat'));

stompClient.connect({}, () => {
    stompClient.subscribe('/topic/chat/' + roomId, (message) => {
        const chatMessage = JSON.parse(message.body);
        displayMessage(chatMessage);
    });
});

function sendMessage() {
    const message = { type: 'TALK', roomId, sender: 'username', message: document.getElementById("msg").value };
    stompClient.send("/app/chat/sendMessage", {}, JSON.stringify(message));
}
