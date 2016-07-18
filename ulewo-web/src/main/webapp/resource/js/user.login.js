$(function() {

	ulewo.initForm($("form.u-form").eq(0));
	
	ulewo.url={ login:"login.do" };

	$(".btn").click(function() {
		login();
	});

	/**
	 * 点击验证码图片换验证码
	 */
	$(document).on("click", "#refreshCheckCode", function(){
		$(this).find("img").attr("src", "checkCode?" + new Date());
	});

	/**
	 * 登录
	 */
	function login(){
		var formObj = $("form.u-form").eq(0);
		var result = ulewo.checkForm($("form.u-form").eq(0));
		if(result){
			ulewo.ajaxRequest({
				async:true, url:ulewo.url.login, data:formObj.eq(0).serialize(), fun:function(response){
					if(response.responseCode.code==ulewo.resultCode.SUCCESS){
						alert("登陆成功")
					}else if(response.responseCode.code==ulewo.resultCode.MOREMAXLOGINCOUNT){
						ulewo.tipMsg({
							type:"error", content:response.errorMsg, timeout:3000
						});
						
						if($("#checkCode-area").children().length==0){
							var formItem= $('<div class="form-item"></div>').appendTo($("#checkCode-area"));
							$('<div class="item-tit">验证码：</div>').appendTo(formItem);
							$('<div class="item-input"><input type="text" name="checkCode" class="check-code" checkData="{rq:true}" placeholder="请输入验证码"></div>').appendTo(formItem);
							$('<div class="check-code"><a href="javascript:void(0)" id="refreshCheckCode"><img src="checkCode"></a></div>').appendTo(formItem);
							$('<div class="clear"></div>').appendTo(formItem);
						}
					}
				}
			});
		};
	}
	
	/**
	 * 按 enter 键提交
	 */
	$(document).keyup(function(event){
		var code=event.keyCode;
		if(code==13){
			login();
		}
	})
	
	
	
});