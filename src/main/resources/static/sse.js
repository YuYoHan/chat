const eventSource = new EventSource("/sse/notifications?userId=" + userId);

eventSource.onmessage = function(event) {
    alert("알림: " + event.data);  // 새 메시지 알림 표시
};
