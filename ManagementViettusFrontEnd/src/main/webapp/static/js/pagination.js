function excuteListPagination(totalPage, pageChoose, url){
	var limitPage = 5;
	var valueHtml = '<ul  class="pagination">';
		if(limitPage < totalPage){
			var startIndex = 0;
			if(pageChoose != 0){
				valueHtml+='<li class="active"><a href="'+url+0+'">\<<</a></li>';
				startIndex = pageChoose - 1;
			}
			if(pageChoose == (totalPage-1)){
				for(var i = totalPage-limitPage;i<totalPage;i++){
					if(pageChoose == i){
						valueHtml+='<li class="active"><a href='+url+i+'">'+i+'</a></li>';
					}else{
						valueHtml+='<li ><a href="'+url+i+'">'+i+'</a></li>';
					}
				}
			}else{
				for(var i = startIndex;i<limitPage+startIndex;i++){
					if(pageChoose == i){
						valueHtml+='<li class="active"><a href="'+url+i+'">'+i+'</a></li>';
					}else{
						valueHtml+='<li ><a href="'+url+i+'">'+i+'</a></li>';
					}
				}
				totalPage--;
				valueHtml+='<li class="active"><a href="'+url+totalPage+'">\>></a></li>';
			}
		}else{
			for(var i = 0;i<totalPage;i++){
				if(pageChoose == i){
					valueHtml+='<li class="active"><a href="'+url+i+'">'+i+'</a></li>';
				}else{
					valueHtml+='<li ><a href="'+url+i+'">'+i+'</a></li>';
				}
			}
		}
		valueHtml+='</ul>';
		$("#page").append(valueHtml);
}
function excuteListPaginationWithRequest(totalPage, pageChoose, url, requestUrl){
	var limitPage = 5;
	var valueHtml = '<ul  class="pagination">';
		if(limitPage < totalPage){
			var startIndex = 0;
			if(pageChoose != 0){
				valueHtml+='<li class="active"><a href="'+url+0+requestUrl+'">\<<</a></li>';
				startIndex = pageChoose - 1;
			}
			if(pageChoose == (totalPage-1)){
				for(var i = totalPage-limitPage;i<totalPage;i++){
					if(pageChoose == i){
						valueHtml+='<li class="active"><a href='+url+i+requestUrl+'">'+i+'</a></li>';
					}else{
						valueHtml+='<li ><a href="'+url+i+requestUrl+'">'+i+'</a></li>';
					}
				}
			}else{
				for(var i = startIndex;i<limitPage+startIndex;i++){
					if(pageChoose == i){
						valueHtml+='<li class="active"><a href="'+url+i+requestUrl+'">'+i+'</a></li>';
					}else{
						valueHtml+='<li ><a href="'+url+i+requestUrl+'">'+i+'</a></li>';
					}
				}
				totalPage--;
				valueHtml+='<li class="active"><a href="'+url+totalPage+requestUrl+'">\>></a></li>';
			}
		}else{
			for(var i = 0;i<totalPage;i++){
				if(pageChoose == i){
					valueHtml+='<li class="active"><a href="'+url+i+requestUrl+'">'+i+'</a></li>';
				}else{
					valueHtml+='<li ><a href="'+url+i+requestUrl+'">'+i+'</a></li>';
				}
			}
		}
		valueHtml+='</ul>';
		$("#page").append(valueHtml);
}
		
		