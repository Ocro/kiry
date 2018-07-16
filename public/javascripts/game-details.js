$(document).ready(function() {

  if (!XMLHttpRequest.prototype.sendAsBinary) {
    XMLHttpRequest.prototype.sendAsBinary = function (sData) {
      var nBytes = sData.length, ui8Data = new Uint8Array(nBytes);
      for (var nIdx = 0; nIdx < nBytes; nIdx++) {
        ui8Data[nIdx] = sData.charCodeAt(nIdx) & 0xff;
      }
      /* send as ArrayBufferView...: */
      this.send(ui8Data);
      /* ...or as ArrayBuffer (legacy)...: this.send(ui8Data.buffer); */
    };
  }

  var boxart = document.getElementById('file-boxart-front');
  if (boxart != null)
    boxart.onchange = function(e) {
      var file = document.getElementById('file-boxart-front').files[0];
      sendFile(file, "/uploadBoxArt", document.getElementById('boxart-front'));
    };

  // Lazy loads
  $(".lazy").lazy({
    effect : "fadeIn",
    effectTime: 200
  });

  // Clique sur le bouton close.
  var elements = document.getElementsByClassName("close-btn");
  for (i = 0; i < elements.length; i++) {
    elements[i].onclick = function() {
      var toRemove = this.parentNode;
      var section = toRemove.parentNode;
      $(toRemove).fadeToggle(300, function() {
        if (section.getElementsByTagName("div").length == 1)
          section.parentNode.removeChild(section);
        toRemove.parentNode.removeChild(toRemove);
      });
    }
  }

  // Onmouse sur la vue rapide.
  elements = document.getElementsByClassName("quick-view-game");
  for (i = 0; i < elements.length; i++) {
    elements[i].onmouseover = function() {
      var closeBtn = this.getElementsByClassName("close-btn")[0];
      closeBtn.style.display = "block";
    }
    elements[i].onmouseout = function() {
      var closeBtn = this.getElementsByClassName("close-btn")[0];
      closeBtn.style.display = "none";
    }
  }

  var sections = $('.category');
  var navigation_links = $('#left-menu > a');

  navigation_links.click( function(event) {
    $.scrollTo(
      $(this).attr("href"),
      {
        duration: 300,
        offset: { 'left':0, 'top': -0.15*$(window).height() }
      }
    );
  });

  sections.waypoint(function(direction) {
    var active_section;
    active_section = $(this);
    if (direction === "up") active_section = active_section.prev();
    var active_link = $('nav a[href="#' + active_section.attr("id") + '"]');
    navigation_links.removeClass("current");
    active_link.addClass("current");
  }, { offset: '35%' });
});

actionGame = function(source, link, idGame) {
  $.get(link, function(data) {
    var section = source;
    var element = document.querySelectorAll("[id-game='"+idGame+"']")[0];
    $(element).fadeToggle(300, function() {
      if (section.getElementsByTagName("div").length == 1)
        section.parentNode.removeChild(section);
      element.parentNode.removeChild(element);
    });
  });
}

actionPostGame = function(source, link, email, idGame) {
  $.post(link, {
    email: email,
    idGame: idGame
  },
  function(data) {
    var section = source;
    var element = section.querySelectorAll("[id-game='"+idGame+"']")[0];
    $(element).fadeToggle(300, function() {
      if (section.getElementsByTagName("div").length == 1)
        section.parentNode.removeChild(section);
      element.parentNode.removeChild(element);
    });
  });
}

sendFile = function(file, uri, display) {
  var wraper = createFullWrap();
  this.wraper = wraper;
  display.appendChild(wraper);

  var xhr = new XMLHttpRequest();
  var fd = new FormData();
  this.xhr = xhr;

  var self = this;
  this.xhr.upload.addEventListener("progress", function(e) {
    if (e.lengthComputable) {
      var percentage = Math.round((e.loaded * 100) / e.total);
      self.wraper.innerHTML = percentage;
    }
  }, false);
  
  xhr.upload.addEventListener("load", function(e){
    self.wraper.parentNode.removeChild(wraper);
    var form = document.getElementById("boxart-front");
    var frontImg = new Image();
    frontImg.src = frontPath;
    frontImg.alt = "front";
    form.innerHTML = "";
    form.appendChild(frontImg);

  }, false);

  xhr.open("POST", uri, true);

  fd.append('id', document.getElementById('id-game').value);
  fd.append('file', file);
  xhr.send(fd);
}

createFullWrap = function() {
  var myDiv = document.createElement("div");
  myDiv.classList.add("full-wrap");
  return myDiv;
}