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

	if (json.action === "add") {
		printDeviceElement(json);
	}
	if (json.action === "remove") {
		document.getElementById(json.id).remove();
		// device.parentNode.removeChild(device);
	}
	if (json.action === "toggle") {
		var node = document.getElementById(json.id);
		var statusText = node.children[2];
		if (json.status === "On") {
			statusText.innerHTML = "Status: " + json.status
					+ " (<a href=\"#\" OnClick=toggleDevice(" + json.id
					+ ")>Turn off</a>)";
		} else if (json.status === "Off") {
			statusText.innerHTML = "Status: " + json.status
					+ " (<a href=\"#\" OnClick=toggleDevice(" + json.id
					+ ")>Turn on</a>)";
		}
	}
}

function addDevice(name, type, description) {
	var DeviceAction = {
		action : "add",
		name : name,
		type : type,
		description : description
	};
	socket.send(JSON.stringify(DeviceAction));
}

function removeDevice(element) {
	var id = element;
	var DeviceAction = {
		action : "remove",
		id : id
	};
	socket.send(JSON.stringify(DeviceAction));
}

function toggleDevice(element) {
	var id = element;
	var DeviceAction = {
		action : "toggle",
		id : id
	};
	socket.send(JSON.stringify(DeviceAction));
}

function printError(message) {
	alert(message);
}

function printDeviceElement(device) {
	var content = document.getElementById("content");

	var deviceDiv = document.createElement("div");
	deviceDiv.setAttribute("id", device.id);
	deviceDiv.setAttribute("class", "device " + device.type);
	content.appendChild(deviceDiv);

	var deviceName = document.createElement("span");
	deviceName.setAttribute("class", "deviceName");
	deviceName.innerHTML = device.name;
	deviceDiv.appendChild(deviceName);

	var deviceType = document.createElement("span");
	deviceType.innerHTML = "<b>Type:</b> " + device.type;
	deviceDiv.appendChild(deviceType);

	var deviceStatus = document.createElement("span");
	if (device.status === "On") {
		deviceStatus.innerHTML = "<b>Status:</b> " + device.status
				+ " (<a href=\"#\" OnClick=toggleDevice(" + device.id
				+ ")>Turn off</a>)";
	} else if (device.status === "Off") {
		deviceStatus.innerHTML = "<b>Status:</b> " + device.status
				+ " (<a href=\"#\" OnClick=toggleDevice(" + device.id
				+ ")>Turn on</a>)";
		// deviceDiv.setAttribute("class", "device off");
	}
	deviceDiv.appendChild(deviceStatus);

	var deviceDescription = document.createElement("span");
	deviceDescription.innerHTML = "<b>Comments:</b> " + device.description;
	deviceDiv.appendChild(deviceDescription);

	var removeDevice = document.createElement("span");
	removeDevice.setAttribute("class", "removeDevice");
	removeDevice.innerHTML = "<a href=\"#\" OnClick=removeDevice(" + device.id
			+ ")>Remove device</a>";
	deviceDiv.appendChild(removeDevice);
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
	document.getElementById('registerStatus').innerHTML = "Successfully registered number "+phoneNumber;
}

function registerPhoneOnServer(phone) {
	var action = {
		action : "registerPhoneNumber",
		phoneNumber : phone
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
	hideForm();
}