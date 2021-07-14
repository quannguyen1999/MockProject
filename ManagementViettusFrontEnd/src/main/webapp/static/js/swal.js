function excuteSwalDelete(token,url,title){
		Swal.fire({
			icon: 'warning',
			title: 'Xóa mặt hàng',
			text: title,
			showDenyButton: true,
			showCancelButton: true,
			confirmButtonText: `Xác nhận`,
			denyButtonText: `Hủy`
		}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				type : 'DELETE',
				contentType: "application/json; charset=utf-8",
				beforeSend : function(request) {
					request.setRequestHeader("AccessToken", token);
				},
				url : url,
				/*contentType: false, //Cần thiết
				processData: false, //Cần thiết*/
				success : function(e) {
					dialogSuccess(e.message)
					setTimeout(function () { // wait 3 seconds and reload
					    window.location.reload(true);
					 }, 1000);
				},
				error : function(e) {
					var listErrorMessages = e.responseJSON.listErrorMessages;
					dialogError(listErrorMessages[0].message);					
				}
			});
		} else if (result.isDenied) {
		}})
};