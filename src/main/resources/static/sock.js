const socket = new SockJS("http://localhost:8080/ws/chat");
const websocket = Stomp.over(socket);

websocket.connect({}, function(frame) {
    console.log("Connected: " + frame);
    // Subscribe to chat topic
    websocket.subscribe("/topic/chat/" + roomId, function(message) {
        onMessage(message);
    });
});
