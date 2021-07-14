function toggleClick(id,checked, token, url){
	var item = {};
	if(checked == true){
				item['status'] = false;
			}else{
				item['status'] = true;
			}
			$(id).toggleClass('active').siblings().removeClass('active');
			//cal ajax
			$.ajax({
				type : 'PUT',
				contentType: "application/json; charset=utf-8",
				beforeSend : function(request) {
					request.setRequestHeader("AccessToken", token);
				},
				url : url,
				data : JSON.stringify(item),
				/*contentType: false, //Cần thiết
				processData: false, //Cần thiết*/
				success : function(e) {
					dialogSuccess(e.message);
				},
				error : function(e) {
					var listErrorMessages = e.responseJSON.listErrorMessages;
					dialogError(listErrorMessages[0].message);
				}
	});
}