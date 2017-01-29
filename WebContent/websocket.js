window.onload = init;
var socket = new WebSocket("ws://localhost:8080/SafeChatApp/actions");
socket.onmessage = onMessage;

function onMessage(event) {
	var json = JSON.parse(event.data);
	if (json.action === "error") {
		printError(json.message);
	}
	if (json.action === "registeredPhoneNumber") {
		updateStatus(json.phoneNumber);
	}
}

function printError(message) {
	alert(message);
}

function registerPhone() {
	document.getElementById('registerPhoneForm').style.visibility = 'visible';
	var phone = document.getElementById('phoneNumberText').value;
	if (phone == "") {
		document.getElementById('registerStatus').innerHTML = "No Number entered";
	} else {
		document.getElementById('registerStatus').innerHTML = "Processing ...";
		registerPhoneOnServer(phone);
	}
}

function updateStatus(phoneNumber) {
	document.getElementById('registerPhoneForm').style.visibility = 'visible';
	var phone = document.getElementById('phoneNumberText').value;
	document.getElementById('registerStatus').innerHTML = "Successfully registered number "
			+ phoneNumber;
	document.getElementById('chatboxForm').style.visibility = 'visible';
}

function registerPhoneOnServer(phone) {
	var action = {
		action : "registerPhoneNumber",
		phoneNumber : phone
	};
	socket.send(JSON.stringify(action));
}

function sendMessageToNumber() {
	var senderNumber = document.getElementById('phoneNumberText').value;
	var receiverNumber = document.getElementById('numberToSend').value;
	var message = document.getElementById('message').value;
	document.getElementById('sendStatus').style.visibility = 'visible';
	if (receiverNumber == "" || message == "" || senderNumber =="") {
		document.getElementById('sendStatus').innerHTML = "No sender, receiver or message";
	} else {
		document.getElementById('sendStatus').innerHTML = "Sending message...";
		sendMessageToSocket(senderNumber,receiverNumber, message);
	}
}

function sendMessageToSocket(senderNumber,receiverNumber, messageToSend) {
	var action = {
		action : "sendMessageToNumber",
		receiver: receiverNumber,
		sender : senderNumber,
		message : messageToSend
	};
	socket.send(JSON.stringify(action));
}

function formSubmit() {
	var form = document.getElementById("addDeviceForm");
	var name = form.elements["device_name"].value;
	var type = form.elements["device_type"].value;
	var description = form.elements["device_description"].value;
	hideForm();
	document.getElementById("addDeviceForm").reset();
	addDevice(name, type, description);
}

function init() {
}