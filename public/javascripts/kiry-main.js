(function(t,e){if(typeof exports=="object")module.exports=e();else if(typeof define=="function"&&define.amd)define(e);else t.Spinner=e()})(this,function(){"use strict";var t=["webkit","Moz","ms","O"],e={},i;function o(t,e){var i=document.createElement(t||"div"),o;for(o in e)i[o]=e[o];return i}function n(t){for(var e=1,i=arguments.length;e<i;e++)t.appendChild(arguments[e]);return t}var r=function(){var t=o("style",{type:"text/css"});n(document.getElementsByTagName("head")[0],t);return t.sheet||t.styleSheet}();function s(t,o,n,s){var a=["opacity",o,~~(t*100),n,s].join("-"),f=.01+n/s*100,l=Math.max(1-(1-t)/o*(100-f),t),d=i.substring(0,i.indexOf("Animation")).toLowerCase(),u=d&&"-"+d+"-"||"";if(!e[a]){r.insertRule("@"+u+"keyframes "+a+"{"+"0%{opacity:"+l+"}"+f+"%{opacity:"+t+"}"+(f+.01)+"%{opacity:1}"+(f+o)%100+"%{opacity:"+t+"}"+"100%{opacity:"+l+"}"+"}",r.cssRules.length);e[a]=1}return a}function a(e,i){var o=e.style,n,r;if(o[i]!==undefined)return i;i=i.charAt(0).toUpperCase()+i.slice(1);for(r=0;r<t.length;r++){n=t[r]+i;if(o[n]!==undefined)return n}}function f(t,e){for(var i in e)t.style[a(t,i)||i]=e[i];return t}function l(t){for(var e=1;e<arguments.length;e++){var i=arguments[e];for(var o in i)if(t[o]===undefined)t[o]=i[o]}return t}function d(t){var e={x:t.offsetLeft,y:t.offsetTop};while(t=t.offsetParent)e.x+=t.offsetLeft,e.y+=t.offsetTop;return e}var u={lines:12,length:7,width:5,radius:10,rotate:0,corners:1,color:"#000",direction:1,speed:1,trail:100,opacity:1/4,fps:20,zIndex:2e9,className:"spinner",top:"auto",left:"auto",position:"relative"};function p(t){if(typeof this=="undefined")return new p(t);this.opts=l(t||{},p.defaults,u)}p.defaults={};l(p.prototype,{spin:function(t){this.stop();var e=this,n=e.opts,r=e.el=f(o(0,{className:n.className}),{position:n.position,width:0,zIndex:n.zIndex}),s=n.radius+n.length+n.width,a,l;if(t){t.insertBefore(r,t.firstChild||null);l=d(t);a=d(r);f(r,{left:(n.left=="auto"?l.x-a.x+(t.offsetWidth>>1):parseInt(n.left,10)+s)+"px",top:(n.top=="auto"?l.y-a.y+(t.offsetHeight>>1):parseInt(n.top,10)+s)+"px"})}r.setAttribute("role","progressbar");e.lines(r,e.opts);if(!i){var u=0,p=(n.lines-1)*(1-n.direction)/2,c,h=n.fps,m=h/n.speed,y=(1-n.opacity)/(m*n.trail/100),g=m/n.lines;(function v(){u++;for(var t=0;t<n.lines;t++){c=Math.max(1-(u+(n.lines-t)*g)%m*y,n.opacity);e.opacity(r,t*n.direction+p,c,n)}e.timeout=e.el&&setTimeout(v,~~(1e3/h))})()}return e},stop:function(){var t=this.el;if(t){clearTimeout(this.timeout);if(t.parentNode)t.parentNode.removeChild(t);this.el=undefined}return this},lines:function(t,e){var r=0,a=(e.lines-1)*(1-e.direction)/2,l;function d(t,i){return f(o(),{position:"absolute",width:e.length+e.width+"px",height:e.width+"px",background:t,boxShadow:i,transformOrigin:"left",transform:"rotate("+~~(360/e.lines*r+e.rotate)+"deg) translate("+e.radius+"px"+",0)",borderRadius:(e.corners*e.width>>1)+"px"})}for(;r<e.lines;r++){l=f(o(),{position:"absolute",top:1+~(e.width/2)+"px",transform:e.hwaccel?"translate3d(0,0,0)":"",opacity:e.opacity,animation:i&&s(e.opacity,e.trail,a+r*e.direction,e.lines)+" "+1/e.speed+"s linear infinite"});if(e.shadow)n(l,f(d("#000","0 0 4px "+"#000"),{top:2+"px"}));n(t,n(l,d(e.color,"0 0 1px rgba(0,0,0,.1)")))}return t},opacity:function(t,e,i){if(e<t.childNodes.length)t.childNodes[e].style.opacity=i}});function c(){function t(t,e){return o("<"+t+' xmlns="urn:schemas-microsoft.com:vml" class="spin-vml">',e)}r.addRule(".spin-vml","behavior:url(#default#VML)");p.prototype.lines=function(e,i){var o=i.length+i.width,r=2*o;function s(){return f(t("group",{coordsize:r+" "+r,coordorigin:-o+" "+-o}),{width:r,height:r})}var a=-(i.width+i.length)*2+"px",l=f(s(),{position:"absolute",top:a,left:a}),d;function u(e,r,a){n(l,n(f(s(),{rotation:360/i.lines*e+"deg",left:~~r}),n(f(t("roundrect",{arcsize:i.corners}),{width:o,height:i.width,left:i.radius,top:-i.width>>1,filter:a}),t("fill",{color:i.color,opacity:i.opacity}),t("stroke",{opacity:0}))))}if(i.shadow)for(d=1;d<=i.lines;d++)u(d,-2,"progid:DXImageTransform.Microsoft.Blur(pixelradius=2,makeshadow=1,shadowopacity=.3)");for(d=1;d<=i.lines;d++)u(d);return n(e,l)};p.prototype.opacity=function(t,e,i,o){var n=t.firstChild;o=o.shadow&&o.lines||0;if(n&&e+o<n.childNodes.length){n=n.childNodes[e+o];n=n&&n.firstChild;n=n&&n.firstChild;if(n)n.opacity=i}}}var h=f(o("group"),{behavior:"url(#default#VML)"});if(!a(h,"transform")&&h.adj)c();else i=a(h,"animation");return p});
(function(){var t=[].indexOf||function(t){for(var e=0,n=this.length;e<n;e++){if(e in this&&this[e]===t)return e}return-1},e=[].slice;(function(t,e){if(typeof define==="function"&&define.amd){return define("waypoints",["jquery"],function(n){return e(n,t)})}else{return e(t.jQuery,t)}})(this,function(n,r){var i,o,l,s,f,u,a,c,h,d,p,y,v,w,g,m;i=n(r);c=t.call(r,"ontouchstart")>=0;s={horizontal:{},vertical:{}};f=1;a={};u="waypoints-context-id";p="resize.waypoints";y="scroll.waypoints";v=1;w="waypoints-waypoint-ids";g="waypoint";m="waypoints";o=function(){function t(t){var e=this;this.$element=t;this.element=t[0];this.didResize=false;this.didScroll=false;this.id="context"+f++;this.oldScroll={x:t.scrollLeft(),y:t.scrollTop()};this.waypoints={horizontal:{},vertical:{}};t.data(u,this.id);a[this.id]=this;t.bind(y,function(){var t;if(!(e.didScroll||c)){e.didScroll=true;t=function(){e.doScroll();return e.didScroll=false};return r.setTimeout(t,n[m].settings.scrollThrottle)}});t.bind(p,function(){var t;if(!e.didResize){e.didResize=true;t=function(){n[m]("refresh");return e.didResize=false};return r.setTimeout(t,n[m].settings.resizeThrottle)}})}t.prototype.doScroll=function(){var t,e=this;t={horizontal:{newScroll:this.$element.scrollLeft(),oldScroll:this.oldScroll.x,forward:"right",backward:"left"},vertical:{newScroll:this.$element.scrollTop(),oldScroll:this.oldScroll.y,forward:"down",backward:"up"}};if(c&&(!t.vertical.oldScroll||!t.vertical.newScroll)){n[m]("refresh")}n.each(t,function(t,r){var i,o,l;l=[];o=r.newScroll>r.oldScroll;i=o?r.forward:r.backward;n.each(e.waypoints[t],function(t,e){var n,i;if(r.oldScroll<(n=e.offset)&&n<=r.newScroll){return l.push(e)}else if(r.newScroll<(i=e.offset)&&i<=r.oldScroll){return l.push(e)}});l.sort(function(t,e){return t.offset-e.offset});if(!o){l.reverse()}return n.each(l,function(t,e){if(e.options.continuous||t===l.length-1){return e.trigger([i])}})});return this.oldScroll={x:t.horizontal.newScroll,y:t.vertical.newScroll}};t.prototype.refresh=function(){var t,e,r,i=this;r=n.isWindow(this.element);e=this.$element.offset();this.doScroll();t={horizontal:{contextOffset:r?0:e.left,contextScroll:r?0:this.oldScroll.x,contextDimension:this.$element.width(),oldScroll:this.oldScroll.x,forward:"right",backward:"left",offsetProp:"left"},vertical:{contextOffset:r?0:e.top,contextScroll:r?0:this.oldScroll.y,contextDimension:r?n[m]("viewportHeight"):this.$element.height(),oldScroll:this.oldScroll.y,forward:"down",backward:"up",offsetProp:"top"}};return n.each(t,function(t,e){return n.each(i.waypoints[t],function(t,r){var i,o,l,s,f;i=r.options.offset;l=r.offset;o=n.isWindow(r.element)?0:r.$element.offset()[e.offsetProp];if(n.isFunction(i)){i=i.apply(r.element)}else if(typeof i==="string"){i=parseFloat(i);if(r.options.offset.indexOf("%")>-1){i=Math.ceil(e.contextDimension*i/100)}}r.offset=o-e.contextOffset+e.contextScroll-i;if(r.options.onlyOnScroll&&l!=null||!r.enabled){return}if(l!==null&&l<(s=e.oldScroll)&&s<=r.offset){return r.trigger([e.backward])}else if(l!==null&&l>(f=e.oldScroll)&&f>=r.offset){return r.trigger([e.forward])}else if(l===null&&e.oldScroll>=r.offset){return r.trigger([e.forward])}})})};t.prototype.checkEmpty=function(){if(n.isEmptyObject(this.waypoints.horizontal)&&n.isEmptyObject(this.waypoints.vertical)){this.$element.unbind([p,y].join(" "));return delete a[this.id]}};return t}();l=function(){function t(t,e,r){var i,o;r=n.extend({},n.fn[g].defaults,r);if(r.offset==="bottom-in-view"){r.offset=function(){var t;t=n[m]("viewportHeight");if(!n.isWindow(e.element)){t=e.$element.height()}return t-n(this).outerHeight()}}this.$element=t;this.element=t[0];this.axis=r.horizontal?"horizontal":"vertical";this.callback=r.handler;this.context=e;this.enabled=r.enabled;this.id="waypoints"+v++;this.offset=null;this.options=r;e.waypoints[this.axis][this.id]=this;s[this.axis][this.id]=this;i=(o=t.data(w))!=null?o:[];i.push(this.id);t.data(w,i)}t.prototype.trigger=function(t){if(!this.enabled){return}if(this.callback!=null){this.callback.apply(this.element,t)}if(this.options.triggerOnce){return this.destroy()}};t.prototype.disable=function(){return this.enabled=false};t.prototype.enable=function(){this.context.refresh();return this.enabled=true};t.prototype.destroy=function(){delete s[this.axis][this.id];delete this.context.waypoints[this.axis][this.id];return this.context.checkEmpty()};t.getWaypointsByElement=function(t){var e,r;r=n(t).data(w);if(!r){return[]}e=n.extend({},s.horizontal,s.vertical);return n.map(r,function(t){return e[t]})};return t}();d={init:function(t,e){var r;if(e==null){e={}}if((r=e.handler)==null){e.handler=t}this.each(function(){var t,r,i,s;t=n(this);i=(s=e.context)!=null?s:n.fn[g].defaults.context;if(!n.isWindow(i)){i=t.closest(i)}i=n(i);r=a[i.data(u)];if(!r){r=new o(i)}return new l(t,r,e)});n[m]("refresh");return this},disable:function(){return d._invoke(this,"disable")},enable:function(){return d._invoke(this,"enable")},destroy:function(){return d._invoke(this,"destroy")},prev:function(t,e){return d._traverse.call(this,t,e,function(t,e,n){if(e>0){return t.push(n[e-1])}})},next:function(t,e){return d._traverse.call(this,t,e,function(t,e,n){if(e<n.length-1){return t.push(n[e+1])}})},_traverse:function(t,e,i){var o,l;if(t==null){t="vertical"}if(e==null){e=r}l=h.aggregate(e);o=[];this.each(function(){var e;e=n.inArray(this,l[t]);return i(o,e,l[t])});return this.pushStack(o)},_invoke:function(t,e){t.each(function(){var t;t=l.getWaypointsByElement(this);return n.each(t,function(t,n){n[e]();return true})});return this}};n.fn[g]=function(){var t,r;r=arguments[0],t=2<=arguments.length?e.call(arguments,1):[];if(d[r]){return d[r].apply(this,t)}else if(n.isFunction(r)){return d.init.apply(this,arguments)}else if(n.isPlainObject(r)){return d.init.apply(this,[null,r])}else if(!r){return n.error("jQuery Waypoints needs a callback function or handler option.")}else{return n.error("The "+r+" method does not exist in jQuery Waypoints.")}};n.fn[g].defaults={context:r,continuous:true,enabled:true,horizontal:false,offset:0,triggerOnce:false};h={refresh:function(){return n.each(a,function(t,e){return e.refresh()})},viewportHeight:function(){var t;return(t=r.innerHeight)!=null?t:i.height()},aggregate:function(t){var e,r,i;e=s;if(t){e=(i=a[n(t).data(u)])!=null?i.waypoints:void 0}if(!e){return[]}r={horizontal:[],vertical:[]};n.each(r,function(t,i){n.each(e[t],function(t,e){return i.push(e)});i.sort(function(t,e){return t.offset-e.offset});r[t]=n.map(i,function(t){return t.element});return r[t]=n.unique(r[t])});return r},above:function(t){if(t==null){t=r}return h._filter(t,"vertical",function(t,e){return e.offset<=t.oldScroll.y})},below:function(t){if(t==null){t=r}return h._filter(t,"vertical",function(t,e){return e.offset>t.oldScroll.y})},left:function(t){if(t==null){t=r}return h._filter(t,"horizontal",function(t,e){return e.offset<=t.oldScroll.x})},right:function(t){if(t==null){t=r}return h._filter(t,"horizontal",function(t,e){return e.offset>t.oldScroll.x})},enable:function(){return h._invoke("enable")},disable:function(){return h._invoke("disable")},destroy:function(){return h._invoke("destroy")},extendFn:function(t,e){return d[t]=e},_invoke:function(t){var e;e=n.extend({},s.vertical,s.horizontal);return n.each(e,function(e,n){n[t]();return true})},_filter:function(t,e,r){var i,o;i=a[n(t).data(u)];if(!i){return[]}o=[];n.each(i.waypoints[e],function(t,e){if(r(i,e)){return o.push(e)}});o.sort(function(t,e){return t.offset-e.offset});return n.map(o,function(t){return t.element})}};n[m]=function(){var t,n;n=arguments[0],t=2<=arguments.length?e.call(arguments,1):[];if(h[n]){return h[n].apply(null,t)}else{return h.aggregate.call(null,n)}};n[m].settings={resizeThrottle:100,scrollThrottle:30};return i.load(function(){return n[m]("refresh")})})}).call(this);
;(function($){var h=$.scrollTo=function(a,b,c){$(window).scrollTo(a,b,c)};h.defaults={axis:'xy',duration:parseFloat($.fn.jquery)>=1.3?0:1,limit:true};h.window=function(a){return $(window)._scrollable()};$.fn._scrollable=function(){return this.map(function(){var a=this,isWin=!a.nodeName||$.inArray(a.nodeName.toLowerCase(),['iframe','#document','html','body'])!=-1;if(!isWin)return a;var b=(a.contentWindow||a).document||a.ownerDocument||a;return/webkit/i.test(navigator.userAgent)||b.compatMode=='BackCompat'?b.body:b.documentElement})};$.fn.scrollTo=function(e,f,g){if(typeof f=='object'){g=f;f=0}if(typeof g=='function')g={onAfter:g};if(e=='max')e=9e9;g=$.extend({},h.defaults,g);f=f||g.duration;g.queue=g.queue&&g.axis.length>1;if(g.queue)f/=2;g.offset=both(g.offset);g.over=both(g.over);return this._scrollable().each(function(){if(e==null)return;var d=this,$elem=$(d),targ=e,toff,attr={},win=$elem.is('html,body');switch(typeof targ){case'number':case'string':if(/^([+-]=)?\d+(\.\d+)?(px|%)?$/.test(targ)){targ=both(targ);break}targ=$(targ,this);if(!targ.length)return;case'object':if(targ.is||targ.style)toff=(targ=$(targ)).offset()}$.each(g.axis.split(''),function(i,a){var b=a=='x'?'Left':'Top',pos=b.toLowerCase(),key='scroll'+b,old=d[key],max=h.max(d,a);if(toff){attr[key]=toff[pos]+(win?0:old-$elem.offset()[pos]);if(g.margin){attr[key]-=parseInt(targ.css('margin'+b))||0;attr[key]-=parseInt(targ.css('border'+b+'Width'))||0}attr[key]+=g.offset[pos]||0;if(g.over[pos])attr[key]+=targ[a=='x'?'width':'height']()*g.over[pos]}else{var c=targ[pos];attr[key]=c.slice&&c.slice(-1)=='%'?parseFloat(c)/100*max:c}if(g.limit&&/^\d+$/.test(attr[key]))attr[key]=attr[key]<=0?0:Math.min(attr[key],max);if(!i&&g.queue){if(old!=attr[key])animate(g.onAfterFirst);delete attr[key]}});animate(g.onAfter);function animate(a){$elem.animate(attr,f,g.easing,a&&function(){a.call(this,e,g)})}}).end()};h.max=function(a,b){var c=b=='x'?'Width':'Height',scroll='scroll'+c;if(!$(a).is('html,body'))return a[scroll]-$(a)[c.toLowerCase()]();var d='client'+c,html=a.ownerDocument.documentElement,body=a.ownerDocument.body;return Math.max(html[scroll],body[scroll])-Math.min(html[d],body[d])};function both(a){return typeof a=='object'?a:{top:a,left:a}}})(jQuery);
// Lazy load
(function(e,t,n,r){e.fn.lazy=function(r){function o(n){if(typeof n!="boolean")n=false;s.each(function(){var r=e(this);var s=this.tagName.toLowerCase();if(a(r)||n){if(r.attr(i.attribute)&&(s=="img"&&r.attr(i.attribute)!=r.attr("src")||s!="img"&&r.attr(i.attribute)!=r.css("background-image"))&&!r.data("loaded")&&(r.is(":visible")||!i.visibleOnly)){var o=e(new Image);e.each(this.attributes,function(e,t){if(t.name!="src"){var n=r.attr(t.name);o.attr(t.name,n)}});if(i.onError)o.error(function(){i.onError(o)});var u=true;o.one("load",function(){var e=function(){if(u){t.setTimeout(e,100);return}r.hide();if(s=="img")r.attr("src",o.attr("src"));else r.css("background-image","url("+o.attr("src")+")");r[i.effect](i.effectTime);if(i.removeAttribute)r.removeAttr(i.attribute);if(i.afterLoad)i.afterLoad(r);o.unbind("load");o.remove()};e()});if(i.beforeLoad)i.beforeLoad(r);o.attr("src",r.attr(i.attribute));if(i.onLoad)i.onLoad(o);u=false;if(o.complete)o.load();r.data("loaded",true)}}});s=e(s).filter(function(){return!e(this).data("loaded")})}function u(){if(i.delay>=0)setTimeout(function(){o(true)},i.delay);if(i.delay<0||i.combined){o(false);e(t).bind("scroll",l(i.throttle,o));e(t).bind("resize",l(i.throttle,o))}}function a(e){var t=n.documentElement.scrollTop?n.documentElement.scrollTop:n.body.scrollTop;return t+f()+i.threshold>e.offset().top+e.height()}function f(){if(t.innerHeight)return t.innerHeight;if(n.documentElement&&n.documentElement.clientHeight)return n.documentElement.clientHeight;if(n.body&&n.body.clientHeight)return n.body.clientHeight;if(n.body&&n.body.offsetHeight)return n.body.offsetHeight;return i.fallbackHeight}function l(e,t){function s(){function o(){r=+(new Date);t.apply()}var s=+(new Date)-r;n&&clearTimeout(n);if(s>e||!i.enableThrottle)o();else n=setTimeout(o,e-s)}var n;var r=0;return s}var i={bind:"load",threshold:500,fallbackHeight:2e3,visibleOnly:true,delay:-1,combined:false,attribute:"data-src",removeAttribute:true,effect:"show",effectTime:0,enableThrottle:false,throttle:250,beforeLoad:null,onLoad:null,afterLoad:null,onError:null};if(r)e.extend(i,r);var s=this;if(i.bind=="load")e(t).load(u);else if(i.bind=="event")u();if(i.onError)s.bind("error",function(){i.onError(e(this))});return this};e.fn.Lazy=e.fn.lazy})(jQuery,window,document);

$(document).ready(function() {
  var updateGameListTimeout;
  $(".drop-down > img:first-child").click(function(event) {
    toggleMenuFilter();
    event.stopPropagation();
  });

  $('html').click(function() {
    toggleMenuFilter(false);
    var listBarGame = document.getElementById("list-bar-game");
    listBarGame.style.display = 'none';
  });

  var searchBar = document.getElementById("search-game");
  $(searchBar).on('keydown keyup', function (){
    clearTimeout(updateGameListTimeout);
    updateGameListTimeout = setTimeout(updateGameList, 400);
  });
});

updateGameList = function() {
  removeAllChildNodes(document.getElementById("list-bar-game"));
  var value = document.getElementById("search-game").value;
  if (value.length > 2)
    getGameList(value, openListBar);
}

getGameList = function(searchTerms, callback) {
  if (searchTerms == "" || searchTerms.length < 2)
    return;
  var games = [];
  games.gamedb = [];
  games.local = [];
  var link = "/getGameList";

  // Affichage du chargement
  var listBarGame = document.getElementById("list-bar-game");
  listBarGame.style.display = 'block';
  var loader = new Spinner(optsMin).spin(listBarGame);

  $.post(link, { "search": searchTerms }, function(data) {
    var baseImgUrl = $(data).find("baseImgUrl").text();

    // Jeu de idgamedb
    $(data).find("Game").each(function() {
      var game = {};
      game.id = $(this).find("id").text();
      game.gameTitle = $(this).find("GameTitle").text();
      game.plateform = $(this).find("Platform").text();
      var releaseDate = $(this).find("ReleaseDate").text();
      releaseDate = releaseDate.split("/");
      game.releaseDate = releaseDate[1]+"/"+releaseDate[0]+"/"+releaseDate[2];
      game.frontImgUrl = baseImgUrl + 
                        $(this).find("boxart[side='front']").attr('thumb');
      games.gamedb.push(game);
    });

    // Jeu locaux
    $(data).find("GameLocal").each(function() {
      var game = {};
      game.id = $(this).find("id").text();
      game.gameTitle = $(this).find("GameTitle").text();
      game.plateform = $(this).find("Platform").text();
      game.releaseDate = $(this).find("ReleaseDate").text();
      game.frontImgUrl = "/files/images/games/front/" + game.id + ".jpg";
      games.local.push(game);
    });
    loader.stop();
    callback(games);
  });
}

openListBar = function(games) {
  var listBarGame = document.getElementById("list-bar-game");
  var gamesLocalCount = games.local.length;
  var gamesGameDbCount = games.gamedb.length;

  if (gamesLocalCount == 0 && gamesGameDbCount == 0) {
    var divNoGame = document.createElement("div");
    divNoGame.classList.add("list-no-game");
    divNoGame.innerHTML = "Aucun jeux trouvé";
    listBarGame.appendChild(divNoGame);
  } else {
    if (gamesLocalCount > 0) {
      var divSeparator = document.createElement("div");
      divSeparator.classList.add("list-title");
      divSeparator.innerHTML = "Jeux locaux";
      listBarGame.appendChild(divSeparator);

      // Affichage des jeux locaux
      for (i = 0; i < gamesLocalCount; i++) {
        var gameElement = document.createElement("a");
        var innerElement;
        var game = games.local[i];
        gameElement.setAttribute("href", "/gameDetails/" + game.id);
        innerElement = document.createElement("img");
        innerElement.style.width = "50px";
        innerElement.style.height = "50px";
        innerElement.style.backgroundImage = "url(" + game.frontImgUrl + ")";
        innerElement.style.backgroundPosition = "50%";
        innerElement.style.backgroundSize = "100%";
        innerElement.style.backgroundRepeat = "no-repeat";
        innerElement.style.float = "left";
        innerElement.style.marginRight = "10px";
        innerElement.setAttribute("src", game.frontImgUrl);
        innerElement.onerror = function() {
          this.setAttribute("src", "/files/images/img-transparent.gif");
          this.style.backgroundImage = "url(/files/images/no-front.jpg)";
        }
        innerElement.onload = function() {
          this.setAttribute("src", "/files/images/img-transparent.gif");
        }
        gameElement.appendChild(innerElement);

        innerElement = document.createElement("div");
        innerElement.innerHTML = game.gameTitle;
        gameElement.appendChild(innerElement);

        innerElement = document.createElement("div");
        var str = 
        innerElement.innerHTML = game.plateform + (game.releaseDate == "" ? "" : " - " + game.releaseDate);
        gameElement.appendChild(innerElement);

        listBarGame.appendChild(gameElement);
      }
    }

    if (gamesGameDbCount > 0) {
      divSeparator = document.createElement("div");
      divSeparator.classList.add("list-title");
      divSeparator.innerHTML = "Jeux disponibles";
      listBarGame.appendChild(divSeparator);

      // Affichage des jeux de gamedb
      for (i = 0; i < gamesGameDbCount; i++) {
        var gameElement = document.createElement("a");
        var game = games.gamedb[i];
        gameElement.setAttribute("href", "/gameDetails/db/" + game.id);

        innerElement = document.createElement("div");
        innerElement.innerHTML = game.gameTitle;
        gameElement.appendChild(innerElement);

        innerElement = document.createElement("div");
        innerElement.innerHTML = game.plateform + " - " + game.releaseDate;
        gameElement.appendChild(innerElement);
        
        listBarGame.appendChild(gameElement);
      }
    }
  }

  // Ajouter un nouveau jeu
  var aNewGame = document.createElement("a");
  aNewGame.classList.add("list-new-game");
  aNewGame.innerHTML = "Vous ne trouvez pas votre jeu? Ajoutez-le! ";
  aNewGame.setAttribute("href", "/addNewGame");

  var spanArrow = document.createElement("span");
  spanArrow.classList.add("icon");
  spanArrow.classList.add("icon-bracket2");
  aNewGame.appendChild(spanArrow);

  listBarGame.appendChild(aNewGame);
}

removeAllChildNodes = function(element) {
  while (element.hasChildNodes())
    element.removeChild(element.lastChild);
}

addTag = function(name, tagManager, newTag, form) {
  if (!newTag)
    return;

  var htmlTag = document.createElement('span');
  htmlTag.classList.add("tag");
  htmlTag.innerHTML = newTag;

  var hiddenTag = document.createElement('input');
  hiddenTag.setAttribute('type', 'hidden');
  hiddenTag.setAttribute('name', name);
  hiddenTag.setAttribute('value', newTag);
  hiddenTag.classList.add('hidden-tag');
  form.appendChild(hiddenTag);

  // Delete on click
  htmlTag.onclick = function() {
    htmlTag.parentNode.removeChild(this);
    form.removeChild(hiddenTag);
  }

  tagManager.insertBefore(htmlTag, tagManager.lastChild);
}

removeAllTags = function(form) {
  $(form).find('.tag').each(function() {
    $(this).remove();
  });

  $(form).find('.hidden-tag').each(function() {
    $(this).remove();
  });
}

// Tag manager
tagManager = function (name, element, form) {

  var wrapper = document.createElement('div');
  
  var copy = element.cloneNode(true);
  copy.setAttribute('autocomplete', 'off');

  // Populate autocompletion of genres
  $.get("/getGenresList", function(data) {
    $(copy).autocomplete({
      source: JSON.parse(data),
      select: function(event, ui) {
        addTag(name, wrapper, ui.item.value, form);
      },
      close: function(event, ui) {
        copy.value = "";
      }
    });
  });

  // Pressed on Return
  $(copy).keypress(function(e) {
    if(e.keyCode == 13) {
      addTag(name, wrapper, this.value, form);
      this.value = "";
    }
  });

  wrapper.classList.add("tag-manager");
  wrapper.classList.add("flex");
  wrapper.classList.add("flex-h");
  wrapper.appendChild(copy);
  element.parentNode.replaceChild(wrapper, element);
}

toggleMenuFilter = function(visible) {
  var menu = document.getElementById('wrap-menu-filer');
  if (visible || visible === undefined && menu.classList.contains('hidden')) {
    menu.classList.remove("hidden");
    menu.classList.add("visible");
  } else {
    menu.classList.remove("visible");
    menu.classList.add("hidden");
  }
}

var opts = {
  lines: 9, // The number of lines to draw
  length: 40, // The length of each line
  width: 2, // The line thickness
  radius: 20, // The radius of the inner circle
  corners: 1, // Corner roundness (0..1)
  rotate: 54, // The rotation offset
  direction: 1, // 1: clockwise, -1: counterclockwise
  color: '#6D3CA8', // #rgb or #rrggbb
  speed: 1.8, // Rounds per second
  trail: 45, // Afterglow percentage
  shadow: false, // Whether to render a shadow
  hwaccel: false, // Whether to use hardware acceleration
  className: 'spinner', // The CSS class to assign to the spinner
  zIndex: 2e9, // The z-index (defaults to 2000000000)
  top: 'auto', // Top position relative to parent in px
  left: 'auto' // Left position relative to parent in px
};

var optsMin = {
  lines: 9, // The number of lines to draw
  length: 10, // The length of each line
  width: 2, // The line thickness
  radius: 5, // The radius of the inner circle
  corners: 1, // Corner roundness (0..1)
  rotate: 54, // The rotation offset
  direction: 1, // 1: clockwise, -1: counterclockwise
  color: '#6D3CA8', // #rgb or #rrggbb
  speed: 1.8, // Rounds per second
  trail: 45, // Afterglow percentage
  shadow: false, // Whether to render a shadow
  hwaccel: false, // Whether to use hardware acceleration
  className: 'spinner', // The CSS class to assign to the spinner
  zIndex: 2e9, // The z-index (defaults to 2000000000)
  top: 'auto', // Top position relative to parent in px
  left: 'auto' // Left position relative to parent in px
};

submitform = function(id) {
  $("#" + id).submit();
}

var spinner = null;
showLoading = function() {
  if (spinner != null)
    spinner.stop();
  var target = document.getElementById('loading-wrap');
  spinner = new Spinner(opts).spin(target);
}

hideLoading = function() {
  spinner.stop();
  spinner = null;
}