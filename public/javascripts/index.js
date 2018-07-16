 $(document).ready(function() {

  // Make active current link.
  $("a[list-type='@typeList']").addClass("active");

  sortByPlatform();
});

sortByPlatform = function() {
  
  var games = document.getElementById("list-user-game");
  if (games == null)
    return;

  var gamesOutput = document.createElement("section");
  var elem = games.getElementsByTagName("a")

  createNav = function() {
    var nav = document.createElement("nav");
    nav.classList.add("flex");
    nav.classList.add("list-added-game");
    return nav;
  };

  createSection = function(platform, nav) {
    var box = document.createElement("section");
    var title = document.createElement("h1");
    title.classList.add("platform");
    title.innerHTML = platform;
    box.appendChild(title);
    box.appendChild(nav);
    gamesOutput.appendChild(box);
  };

  var nav = createNav();
  nav.appendChild(elem[0].cloneNode(true));
  for(var i = 1; i < elem.length; i++) {
    if (elem[i].getAttribute("platform") !== elem[i-1].getAttribute("platform")) {
      createSection(elem[i-1].getAttribute("platform"), nav);
      nav = createNav();
    }
    nav.appendChild(elem[i].cloneNode(true));
  }
  createSection(elem[elem.length-1].getAttribute("platform"), nav);
  games.innerHTML = gamesOutput.innerHTML;

  // Active lazy load.
  $($(".list-added-game").find(".lazy")).lazy({
    effect : "fadeIn",
    effectTime: 200,
    onError: function(element) {
      $(element).attr("src", "/files/images/no-front.jpg");
    }
  });
}