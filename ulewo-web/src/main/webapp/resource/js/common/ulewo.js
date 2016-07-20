$.extend(ulewo, {
	regs : function() {
		var regs = {
			all : {
				reg : /^[\w\u4e00-\u9fa5]+$/,
				desc : "中文，字母,数字或下划线！"
			},
			common : {
				reg : /^\w+$/,
				desc : "字母,数字或下划线！"
			},
			fangle : {
				reg : /[\uFF00-\uFFFF]/,
				desc : "全角字符!"
			},
			vcode : {
				reg : /^\d{4}$/,
				desc : "4位数字！"
			},
			email : {
				reg : /^\w[\w\.-]*@[\w-]+(\.[\w-]+)+$/,
				desc : "邮箱格式！"
			},
			idcard : {
				reg : /^(\d{15}|\d{17}[\dx])$/,
				desc : "15或18位身份证号码！"
			},
			chinese : {
				reg : /^[\u4E00-\u9FAF]+$/,
				desc : "中文！"
			},
			truename : {
				reg : /^[\u4E00-\u9FAF]{2,4}$/,
				desc : "2-4个中文！"
			},
			english : {
				reg : /^[A-Za-z]+$/,
				desc : "英文！"
			},
			date : {
				reg : /^\d{4}-\d{2}-\d{2}$/i,
				desc : "公历日期(2013-07-06)！"
			},
			url : {
				reg : /^http(s)?:\/\//i,
				desc : "URL！"
			},
			qq : {
				reg : /^[1-9]\d{4,10}$/,
				desc : "5-11位QQ号！"
			},
			phone : {
				reg : /^((((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?)|(\d{11}))$/,
				desc : "电话或手机号码！"
			},
			mobile : {
				reg : /^(\d{1,4}\-)?(13|15|18){1}\d{9}$/,
				desc : "手机号码！"
			},
			symbol : {
				reg : /[`~!@#$%^&*()+=|{}':;',.<>/?~！@#￥%……&*（）——+|{}【】'；：""'。，、？]/,
				desc : "特殊字符！"
			},
			password : {
				reg : /^\w+$/,
				desc : "字母和数字或下划线"
			},
			ip : {
				reg : /^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))$/,
				desc : "IP地址"
			},
			mac : {
				reg : /[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}/i,
				desc : "MAC地址"
			},
			number : {
				reg : /^\d+$/,
				desc : "数字"
			},
			unnumber : {
				reg : /\D/,
				desc : "不为数字"
			},
			integer : {
				reg : /^-?(0|[1-9]\d*)$/,
				desc : "整数！"
			},
			decimal : {
				reg : /^-?[0-9]+\.[0-9]+$/,
				desc : "小数"
			}
		}
		return regs;
	},
	
	initForm : function(formObj) {
		if (!placeholderSupport()) {
			formObj.find('[placeholder]').focus(
				function() {
					var input = $(this);
					if (input.val() == input.attr('placeholder')) {
						input.val('');
						input.removeClass('placeholder-style');
					}
					if (input.attr("realType") == "password") {
						input.prop("type", "password");
					}
				}).blur(function() {
					var input = $(this);
					if (input.val() == '' || input.val() == input.attr('placeholder')) {
						if (input.prop("type") == "password") {
							input.prop("type", "text");
							input.attr("realType", "password")
						}
						input.addClass('placeholder-style');
						input.val(input.attr('placeholder'));
					}
				}).blur();
		}
		function placeholderSupport() {
			return 'placeholder' in document.createElement('input');
		}
	},
	checkForm : function(formObj) {
		var inputs = formObj.find("input[checkData]");
		var resutl = true;
		formObj.find(".error").remove();
		formObj.find("input").removeClass("warn");
		formObj.find("select").removeClass("warn");
		formObj.find("textarea").removeClass("warn");
		var selectes = formObj.find("select[checkData]");
		var textareas = formObj.find("textarea[checkData]");
		for (var i = 0, _len = selectes.length; i < _len; i++) {
			inputs.push(selectes.eq(i));
		}
		for (var i = 0, _len = textareas.length; i < _len; i++) {
			inputs.push(textareas.eq(i));
		}
		for (var i = 0, _len = inputs.length; i < _len; i++) {
			var input = $(inputs[i]);
			var checkDataStr = input.attr("checkData");
			var placeholderData = input.attr("placeholder");
			var value = $.trim(input.val());
			value = value == placeholderData ? "" : value;
			if (null != checkDataStr) {
				var checkData = eval('(' + checkDataStr + ')');
				var msg = checkData.msg;
				msg = msg == null ? "输入不合法" : msg;
				var max = checkData.max;
				if (null != max && value.length > max) {
					this.setError(input, msg);
					resutl = false;
				}
				var min = checkData.min;
				if (null != min && value.length < min) {
					this.setError(input, msg);
					resutl = false;
				}
				var reg = checkData.reg;
				if (null != reg && !this.regs()[reg].reg.test(value)) {
					this.setError(input, msg);
					resutl = false;
				}
				var rq = checkData.rq;
				if (rq != null && value == "") {
					this.setError(input, msg);
					resutl = false;
				}
				var number = checkData.number;
				if (number && !this.regs().number.reg.test(value)) {
					this.setError(input, msg);
					resutl = false;
				}
			}
		}
		return resutl;
	},
	setError : function(obj, msg) {
		obj.addClass("warn");
		if (obj.next().length == 0) {
			obj.after($("<div class='error'>" + msg + "</div>"));
		}
		obj.focus();
	},
	tipMsg : function(config) {
		var type = config.type;
		var content = config.content;
		var timeout = config.timeout;
		var fun = config.fun;
		var icon = "info";
		switch (type) {
		case "loading":
			icon = "loading";
			break;
		case "info":
			icon = "info";
			break;
		case "warn":
			icon = "warn";
			break;
		case "success":
			icon = "success";
			break;
		case "error":
			icon = "error-icon";
			break;
		default:
			icon = "info";
		}
		var content = "<div class='dialog-tip-msg'><div class='dialog-tip-msg-icon " + icon
				+ "'></div><div class='dialog-tip-msg-content'>" + content + "</div></div>";
		var d = dialog({content : content});
		if (type == "loading") {
			d.showModal();
		} else {
			d.show();
		}
		if (null !== timeout && typeof (timeout) != "undefined" && timeout != 0) {
			setTimeout(function() {
				d.remove();
				fun == null ? "" : fun();
			}, timeout)
		}
		return d;
	},
	dialog : function(config) {
		var title = config.title
		var content = config.content;
		var okfun = config.okfun;
		var cancelfun = config.cancelfun;
		var align = config.align;
		var showButton = config.showButton == null ? true : config.showButton;
		var width = config.width == null ? "auto" : config.width;
		var button = [ {
			value : '取消'
		}, {
			value : '确定',
			callback : function() {
				if (okfun != null) {
					okfun();
					return false;
				}
			},
			autofocus : true
		} ];
		if (!showButton) {
			button = [];
		}
		var d = dialog({
			title : title,
			content : content,
			button : button,
			width : width
		});
		d.showModal();
		return d;
	},
	popDialog : function(config) {
		var align = config.align;
		var content = config.content;
		var width = config.width;
		var obj = config.obj;
		var d = dialog({
			align : align,
			content : content,
			width : width,
			quickClose : true
		});
		d.show(obj);
		return d;
	},
	confirm : function(config) {
		var content = config.msg || "你确认要执行操作";
		var fun = config.fun;
		var content = "<div class='dialog-tip-msg'><div class='dialog-tip-msg-icon warn'></div><div class='dialog-confirm-content'>"
				+ content + "</div></div>";
		var d = dialog({
			title : "确认提示",
			content : content,
			okValue : '确定',
			ok : function() {
				fun == null ? "" : fun();
			},
			cancelValue : '取消',
			cancel : function() {
			}
		});
		d.showModal();
		return d;
	},
	ajaxRequest : function(config) {
		var async = config.asycn = null ? true : config.asycn;
		var url = config.url;
		var data = config.data || "";
		var fun = config.fun;
		var showLoad = config.showLoad == null ? true : config.showLoad;
		if (showLoad) {
			var d = this.tipMsg({
				type : "loading", content : "加载中......"
			});
		}
		var that = this;
		$.ajax({async : async, cache : false, type : 'POST', dataType : "json", data : data, url : url, success : function(response) {
				if (showLoad) {
					d.remove();
				}
				if (response.responseCode.code == ulewo.resultCode.LOGINTIMEOUT) {
					var url = ulewo.curUrl;
					that.goLogin();
				} else if (response.responseCode.code == ulewo.resultCode.SUCCESS) {
					fun(response);
				} else if (response.responseCode.code == ulewo.resultCode.MOREMAXLOGINCOUNT) {
					$("#refreshCheckCode").find("img").attr("src","checkCode.do?" + new Date().getTime());
					fun(response);
				} else if (response.responseCode.code == ulewo.resultCode.NOPERMISSION) {
					that.tipMsg({type : "error", content : response.errorMsg, timeout : 3000});
				} else if (response.responseCode.code == ulewo.resultCode.CODEERROR) {
					that.tipMsg({type : "error", content : response.errorMsg, timeout : 3000});
					$("#refreshCheckCode").find("img").attr("src", "checkCode.do?" + new Date().getTime());
				} else {
					that.tipMsg({type : "error", content : response.errorMsg, timeout : 3000});
					$("#refreshCheckCode").find("img").attr("src", "checkCode.do?" + new Date().getTime());
				}
			}
		});
	},
	
	tag : function(config) {
		var id = config.id;
		var contentClass = config.contentClass;
		var event = config.event || "click";
		var fun = config.fun || function() {
		};
		var tag = $("#" + id);
		var lis = tag.find("li");
		$("." + contentClass).css({
			"marginTop" : "10px"
		});
		$("." + contentClass).hide();
		$("." + contentClass).eq(0).show();
		lis.each(function(i, v) {
			$(this).attr("index", i);
		});
		lis.bind(event, function() {
			lis.removeClass("active");
			$(this).addClass("active");
			$("." + config.contentClass).hide();
			$("." + config.contentClass).eq($(this).attr("index")).show();
			fun($(this).attr("index"));
		});
	},
	
	pagination : function(config) {
		var id = config.pagePanelId;
		var page = config.pageObj;
		var fun = config.fun;
		var pageTotal = page.pageTotal;
		var curPageNo = page.page;
		var countTotal = page.countTotal
		var pageNum = 10;
		var ulPanle = $("<ul class='pagination'></ul>");
		$("#" + id).empty();
		ulPanle.appendTo($("#" + id));
		if (pageTotal <= 1) {
			return;
		}
		var beginNum = 0;
		var endNum = 0;
		if (pageNum > pageTotal) {
			beginNum = 1;
			endNum = pageTotal;
		}
		if (curPageNo - pageNum / 2 < 1) {
			beginNum = 1;
			endNum = pageNum;
		} else {
			beginNum = curPageNo - pageNum / 2 + 1;
			endNum = curPageNo + pageNum / 2;
		}
		if (pageTotal - curPageNo < pageNum / 2) {
			beginNum = pageTotal - pageNum + 1;
		}
		if (beginNum < 1) {
			beginNum = 1;
		}
		if (endNum > pageTotal) {
			endNum = pageTotal;
		}
		if (curPageNo > 1) {
			$("<li><a href='javascript:void(0)' class='prePage'>首&nbsp;&nbsp;页</a></li>").appendTo(ulPanle).click(function() {
					fun(1);
				});
			$("<li><a href='javascript:void(0)'><</a></li>").appendTo(ulPanle).click(function() {
					fun((curPageNo - 1));
				});
		} else {
			$("<li><span>首&nbsp;&nbsp;页</span></li>").appendTo(ulPanle);
			$("<li><span><</span></li>").appendTo(ulPanle);
		}
		for (var i = beginNum; i <= endNum; i++) {
			if (pageTotal > 1) {
				if (i == curPageNo) {
					$("<li id='nowPage'>" + curPageNo + "</li>").appendTo(ulPanle);
				} else {
					$("<li  pageNum=" + i + "><a href='javascript:void(0)'>" + i + "</a></li>").appendTo(ulPanle).click(function() {
						fun($(this).attr(
								"pageNum"));
						});
				}
			}
		}
		if (curPageNo < pageTotal) {
			$("<li><a href='javascript:void(0)'>></a></li>").appendTo(ulPanle).click(function() {
					fun((curPageNo + 1));
				});
			$("<li><a href='javascript:void(0)' class='prePage'>尾&nbsp;&nbsp;页</a></li>").appendTo(ulPanle).click(function() {
					fun(pageTotal);
				});
		} else {
			$("<li><span>></span></a></li>").appendTo(ulPanle);
			$("<li><span>尾&nbsp;&nbsp;页</span></li>").appendTo(ulPanle)
		}
		$("<li><span>总共" + countTotal + "条记录，当前" + curPageNo + "/" + pageTotal + "页</span></li>").appendTo(ulPanle)
	},
	
	showEmotion : function(curObj, textarea) {
		var d = this.popDialog({
			width : 300,
			align : 'bottom left',
			obj : curObj
		});
		var emotion_panel = $("<div></div>")
		var emotions = ulewo.emotion_data;
		for (var i = 0, _len = emotions.length; i < _len; i++) {
			var item = $("<div data=" + emotions[i] + " class='emotion' title=" + emotions[i] + "><img src='" + ulewo.absolutePath 
				+ "/resource/images/emotions/" + i + ".gif'></div>").appendTo(emotion_panel).bind("click", function() {
					d.close();
					textarea.val(textarea.val() + $(this).attr("data")).focus();
				});
		}
		d.content(emotion_panel);
	},
	
	showAtUser : function(curObj, textarea) {
		var d = this.popDialog({
			width : 300,
			align : 'bottom left',
			obj : curObj
		});
		var at_panel = $("<div></div>")
		ulewo.ajaxRequest({url : ulewo.absolutePath + "/user/loadFocus.action", showLoad : false, fun : function(res) {
				var content = "";
				var list = res.data;
				var _len = list.length;
				if (_len == 0) {
					content = "没有关注的用户";
				} else {
					content = at_panel;
					for (var i = 0, item; i < _len, item = list[i]; i++) {
						$("<a href='javascript:;' class='at_user'>" + item.friendUserName + "</a>").appendTo(content).click(
							function() {
								d.close();
								textarea.val(textarea.val()).focus().val(textarea.val() + "@" + $(this).text()+ " ");
							});
					}
				}
				d.content(content);
			}
		});
	},
	showImage : function(curIndex, imgArry) {
		var dialogId = "dialog-image-id";
		var d = dialog({
			id : dialogId,
			title : '查看图片'
		});
		d.showModal();
		this.nextImg(dialogId, curIndex, imgArry);
	},
	nextImg : function(dialogId, curIndex, imgArry) {
		var d = dialog.get(dialogId);
		d.content('<div class="dialog-image-load" style="width:100px;text-align:center"><img src="'
						+ ulewo.absolutePath + '/resource/images/loading.gif"><div>');
		var src = imgArry[curIndex];
		var imgReal = new Image();
		var imgSrc = src;
		var targetSrc = "";
		if (imgSrc.indexOf("_") == -1) {
			targetSrc = imgSrc.split(".small")[0];
		} else {
			targetSrc = imgSrc.split("_")[0];
		}
		imgReal.src = targetSrc;
		var browser = this.getBrowserType();
		if (browser == "IE") {
			imgReal.onreadystatechange = function() {
				if (imgReal.readyState == "complete") {
					var html = createPreAndNextBtn(dialogId,
							imgArry, curIndex, imgReal.src);
					d.content(html);
				}
			}
		} else if (browser == "others") {
			imgReal.onload = function() {
				if (imgReal.complete) {
					var html = createPreAndNextBtn(dialogId,
							imgArry, curIndex, imgReal.src);
					d.content(html);
				}
				showBtn();
			}
		}
		function showBtn() {
			var dialog_img_con_height = $("#dialog-image-con")
					.height();
			$("#dialog-image-con a.dialog-image-change").css({
				"top" : (dialog_img_con_height / 2) + "px"
			});
		}
		function createPreAndNextBtn(dialogId, imgArry,
				curIndex, realSrc) {
			var imageCount = imgArry.length;
			var html = "<div id='dialog-image-con'><a class='dialog-img' href='"
					+ realSrc
					+ "' target='_blank'><img src='"
					+ realSrc + "'></a>";
			if (curIndex != 0) {
				html = html
						+ "<a href='javascript:;' type='pre' dialogId=" + dialogId + "  imgArry=" + imgArry
						+ "  curIndex=" + curIndex + "  class='dialog-image-change pre' title='上一张'></a>";
			}
			if (curIndex != imageCount - 1) {
				html = html
						+ "<a href='javascript:;' type='next' dialogId=" + dialogId + "  imgArry="
						+ imgArry + "  curIndex=" + curIndex + "  class='dialog-image-change next' title='下一张'></a>";
			}
			html = html + "</div>";
			return html;
		}
	},
	getBrowserType : function() {
		var browser = navigator.userAgent.indexOf("MSIE") > 0 ? 'IE' : 'others';
		return browser;
	},
	goLogin : function() {
		var url = ulewo.curUrl;
		url = encodeURI(url);
		document.location.href = ulewo.absolutePath + "/login?redirect=" + url;
	},
	goRegister : function() {
		document.location.href = ulewo.absolutePath + "/register";
	}
});

ulewo.emotion_data = [ "[围观]", "[威武]", "[给力]", "[浮云]", "[奥特曼]", "[兔子]", "[熊猫]",
		"[飞机]", "[冰棍]", "[干杯]", "[给跪了]", "[囧]", "[风扇]", "[呵呵]", "[嘻嘻]", "[哈哈]",
		"[爱你]", "[晕]", "[泪]", "[馋嘴]", "[抓狂]", "[哼]", "[抱抱]", "[可爱]", "[怒]",
		"[汗]", "[困]", "[害羞]", "[睡觉]", "[钱]", "[偷笑]", "[酷]", "[衰]", "[吃惊]",
		"[闭嘴]", "[鄙视]", "[挖鼻屎]", "[花心]", "[鼓掌]", "[失望]", "[思考]", "[生病]",
		"[亲亲]", "[怒骂]", "[太开心]", "[懒得理你]", "[右哼哼]", "[左哼哼]", "[嘘]", "[委屈]",
		"[吐]", "[可怜]", "[打哈气]", "[黑线]", "[顶]", "[疑问]", "[握手]", "[耶]", "[好]",
		"[弱]", "[不要]", "[没问题]", "[赞]", "[来]", "[蛋糕]", "[心]", "[伤心]", "[钟]",
		"[猪头]", "[话筒]", "[月亮]", "[下雨]", "[太阳]", "[蜡烛]", "[礼花]", "[玫瑰]" ];