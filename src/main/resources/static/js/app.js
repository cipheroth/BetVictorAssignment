var stompClient = null;

function connect() {
    var sender = document.getElementById('sender').value;
    console.log('SENDER.....', sender);
    var recipient = document.getElementById('recipient').value;
    if(sender==='' || sender===undefined||sender===null){
        alert('Please enter a Nickname');
        return;
    }
    if(recipient === '' || recipient===undefined||recipient===null){
        alert('Please enter a user to chat');
        return;
    }
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({ }, function (frame) {
        console.log('Connected: ' + frame);
        setConnected(true);
        stompClient.subscribe('/topic/message/'+ sender.toLowerCase(), function (message) {
            console.log('MESSAGE ARRIVED: ', message.body);
            showMessageOutput(JSON.parse(message.body));
        });
    });
}

function connectMonitor() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({ }, function (frame) {
        console.log('Connected: ' + frame);
        var info = document.getElementById("information");
        info.innerHTML = "Connection Established !"
        stompClient.subscribe('/topic/message/action-monitor', function (message) {
            console.log('MESSAGE ARRIVED: ', message.body);
            showMonitorOutput(JSON.parse(message.body));
        });
    });
}

function sendMessage() {
    var sender = document.getElementById('sender').value.toLowerCase();
    var recipient = document.getElementById('recipient').value.toLowerCase();
    var text = document.getElementById('text').value;
    stompClient.send("/app/chat/" + recipient, {}, JSON.stringify({'sender':sender, 'recipient': recipient, 'text':text}));
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
        stompClient = null;
    }
    console.log("Disconnected");
    setConnected(false);
}

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('sender').disabled = connected;
    document.getElementById('text').disabled = !connected;
    document.getElementById('sendMessage').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

function showMessageOutput(messageOutput) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(`${messageOutput.sender} says:  ${messageOutput.text}  - (${messageOutput.time})`));
    response.appendChild(p);
}

function showMonitorOutput(monitorOutput) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(`A new message created ===>> [id: ${monitorOutput.id}] [sender: ${monitorOutput.sender}] [recipient: ${monitorOutput.recipient}]  [text: ${monitorOutput.text}]  [time: ${monitorOutput.time}]`));
    response.appendChild(p);
}
