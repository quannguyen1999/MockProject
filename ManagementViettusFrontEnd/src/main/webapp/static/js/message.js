function receiveMessageFromControllerAndShowDialog(messageSuccess, messageError) {
	if (messageSuccess != 'null' && messageSuccess.length >= 1) {
		dialogSuccess(messageSuccess);
	}
	if (messageError != 'null' && messageError.length >= 1) {
		dialogError(messageError);
	}
}
setTimeout(receiveMessageFromController, 100);