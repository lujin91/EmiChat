(function (jQuery){
	this.version = '@1.3';
	this.layer = {'width' : 200, 'height':100};
	this.title = '信息提示';
	this.time = 4000;
	this.anims = {'type' : 'slide', 'speed' : 600};
	this.timer1 = null;
	
	this.inits = function(title, text){
		if($("#message").is("div")){
			return;
		}
		
		$(document.body).prepend('<div id="message" style="border:#b9c9ef 1px solid; z-index:100; width:'+ this.layer.width + 
				'px; height:' + this.layer.height + 'px; position:absolute; display:none; background: #cfdef4; bottom:0; right:0; overflow:hidden;">'
				+ '<div style="border:1px solid #fff; border-bottom:none; width:100%; height:25px; font-size:12px; overflow:hidden; color:#1f336b;">'
				+ '<span id="message_close" style="float:right; padding:5px 0 5px 0; width:16px; line-height:auto; color:red; font-size:12px; font-weight:bold; text-align:center; cursor:pointer; overflow:hidden;">x</span>'
				+ '<div style="padding:5px 0 5px 5px; width: 100px; line-height:18px; text-align:left; overflow:hidden;">' + title + '</div><div style="clear:both;"></div></div>'
				+ '<div style="padding-bottom:5px;border:1px solid #fff; border-top:none; width:100%; height:auto; font-size:12px;">'
				+ '<div id="message_content" style="margin:0 5px 0 5px; border:#b9c9ef 1px solid; padding:10px 0 10px 5px; border:#b9c9ef 1px solid; padding:10px 0 10px 5px; font-size:12px; width:' + (this.layer.width - 17) + 'px; height:' + (this.layer.height-50) + 'px; color:#1f336b; text-align:left; overflow:hidden;">' + text 
				+ '</div></div></div>');
		
		$("#message_close").click(function(){
			setTimeout('this.close()',1);
		});
		
		$("#message").hover(function(){
			clearTimeout(timer1);
			timer1 = null;
		}, function(){
			timer1 = setTimeout('this.close()', time);
		});
	};
	
	this.show = function(title, text, time){
		if($("#message").is("div")){
			return;
		}
		
		if(title == 0 || !title){
			title = this.title;
		}
		
		this.inits(title, text);
		
		if(time >= 0){
			this.time = time;
		}
		
		switch(this.anims.type){
			case 'slide': $("#message").slideDown(this.anims.speed);break;
			case 'fade': $("#message").fadeIn(this.anims.speed);break;
			case 'show': $("#message").show(this.anims.speed);break;
			default: $("#message").slideDown(this.anims.speed);break;
		}
		
		if(navigator.userAgent.toUpperCase().indexOf("CHROME")){
			setTimeout(function(){
				$("#message").remove();
				this.inits(title, text);
				$("#message").css("display", "block");
			}, this.anims.speed-(this.anims.speed/5));
		}
		
		this.rmmessage(this.time);
	};
	
	this.lays = function(width, height){
		if($("#message").is("div")){
			return;
		}
		
		if(width != 0 && width){
			this.layer.width = width;
		}
		
		if(height != 0 && height){
			this.layer.height = height;
		}
	}
	
	this.anim = function(type, speed){
		if($("#message").is("div")){
			return;
		}
		
		if(type != 0 && type){
			this.layer.type = type;
		}
		
		if(speed != 0 && speed){
			switch(speed){
				case 'slow': break;
				case 'fast': this.anims.speed = 200; break;
				case 'normal': this.anims.speed = 400; break;
				default: this.anims.speed = speed;
			}
		}
	};
	
	this.rmmessage = function(time){
		if(time > 0){
			timer1 = setTimeout('this.close()', time);
		}
	};
	
	this.close = function(){
		switch(this.anims.type){
			case 'slide': $("#message").slideUp(this.anims.speed);break;
			case 'fade': $("#message").fadeOut(this.anims.speed);break;
			case 'show': $("#message").hide(this.anims.speed);break;
			default: $("#message").slideUp(this.anims.speed);break;
		};
		setTimeout('$("#message").remove()', this.anims.speed);
		this.original();
	}
	
	this.original = function(){
		this.layer = {'width' : 200, 'height':100};
		this.title = '信息提示';
		this.time = 4000;
		this.anims = {'type' : 'slide', 'speed' : 600};
	};
	
	jQuery.messager = this;
	return jQuery;
})(jQuery);