$(function() {

	ulewo.initForm($("form.u-form").eq(0));
	
	ulewo.url={ register:"register.do" };

	$(".btn").click(function() {
		register();
	});

	/**
	 * 点击验证码图片换验证码
	 */
	$("#refreshCheckCode").click(function() {
		$(this).find("img").attr("src", "checkCode?" + new Date());
	});

	/**
	 * 注册
	 */
	function register(){
		var formObj = $("form.u-form").eq(0);
		var result = ulewo.checkForm($("form.u-form").eq(0))
		var password = $.trim(formObj.find("input[name='password']").val());
		var rePassword = $.trim(formObj.find("input[name='repassword']").val());
		if(password != rePassword){
			ulewo.setError(formObj.find("input[name='repassword']"),"两次输入的密码不一致");
			return;
		}
		if(result){
			ulewo.ajaxRequest({
				async:true,
				url:ulewo.url.register,
				data:$("form.u-form").eq(0).serialize(),
				fun:function(){
					alert("注册成功");
				}
			})
		}
	}
	
	/**
	 * 按 enter 键提交
	 */
	$(document).keyup(function(event){
		var code=event.keyCode;
		if(code==13){
			register();
		}
	})
	
	
	
});