var updateGameListTimeout;

$(document).ready(function() {

  $("#searchGame").keydown(function() {
    clearTimeout(updateGameListTimeout);
    updateGameListTimeout = setTimeout(updateGameList, 400);
  });

  $.get("/getGenresList", function(data) {
    $('#genres').autocomplete({
      source: JSON.parse(data)
    });
  });

  $.get("/getCompagniesList", function(data) {
    $('.compagnies').autocomplete({
      source: JSON.parse(data)
    });
  });

  tagManager('genres[]', 
             document.getElementById('tag-genres'), 
             document.getElementById('form-add-new-game'));
});

updateGameList = function() {
  $("#auto-fill").html("<small><a href='javascript:goToStepTwo()'>vous pouvez aussi ajouter manuellement un jeu</a></small>");
  var gameTitle = $("#searchGame").val();
  if (gameTitle == "" || gameTitle.length < 2)
    return;
  $("#auto-fill").html('<div class="phone-hidden mt1"><h5 style="display: inline;">Puis choisissez le jeu dans la liste</h5></div><ul class="list-game flex" id="list-search-games"></ul>');

  showLoading();
  var link = "/getGameList/" + gameTitle;
  $.get(link, function(data) {
    var baseImgUrl = $(data).find("baseImgUrl").text();
    $(data).find("game").each(function() {
      var id = $(this).find("id").text();
      var gameTitle = $(this).find("GameTitle").text();
      var plateform = $(this).find("Platform").text();
      var releaseDate = $(this).find("ReleaseDate").text();
      releaseDate = releaseDate.split("/");
      releaseDate = releaseDate[1]+"/"+releaseDate[0]+"/"+releaseDate[2];
      var frontImgUrl = baseImgUrl + 
                        $(this).find("boxart[side='front']").attr('thumb');
      $("#list-search-games").append("<li class='txtcenter flex auto-fill-game' onClick='javascript:updateGameInfo("+id+")'><span>" + gameTitle + "</span><span>" + releaseDate + "</span><span>" + plateform + "</span>");
    });

    // Aucun jeu trouvé
    if($("#list-search-games").html() === "")
      $("#list-search-games").html("Aucun jeu trouvé, <a href='javascript:goToStepTwo()'> ajoutez-le manuellement</a>");

    hideLoading();
  });
}

updateGameInfo = function(idGame) {
  showLoading();
  var link = "/getGameInfo/" + idGame;
  $.get(link, function(data) {
    $(data).find("game").each(function() {
      var id = $(this).find("id").text();
      var gameTitle = $(this).find("GameTitle").text();
      var platform = $(this).find("PlatformId").text();
      var youtube = $(this).find("youtube").text();
      var rating = Math.round($(this).find("Rating").text() * 10);
      var releaseDate = $(this).find("ReleaseDate").text();
      releaseDateSplit = releaseDate.split("/");
      releaseDate = releaseDateSplit[2]+"-"+releaseDateSplit[0]+"-"+releaseDateSplit[1];
      var publisher = $(this).find("Publisher").text();
      var developer = $(this).find("Developer").text();
      $("#idGameDb").val(id);
      $("#gameTitle").val(gameTitle);
      $("#game-title-label").html(gameTitle);
      $("#youtube").val(youtube);
      $("#releaseDate").val(releaseDate);
      $("#rating").val(rating);
      $("#platform").val(platform);
      $("#publisher").val(publisher);
      $("#developer").val(developer);

      removeAllTags(document.getElementById('form-add-new-game'));

      // Populate genres tags
      $(this).find("genre").each(function() {
        addTag('genres[]', 
               document.getElementById('tag-genres').parentNode, 
               $(this).text(),
               document.getElementById('form-add-new-game'));
      });

      hideLoading();
      goToStepTwo();
    });
  });
}

updatePlatforms = function() {
  beginImportPlatforms();
  $.ajax({
    type: "get", url: "/importPlatform",
    success: function (data, text) {
      endImportPlatforms();
      alert("La liste des plateformes a été mise à jour.");
      $("#platform").replaceWith(data);
    },
    error: function (request, status, error) {
      endImportPlatforms();
      alert("Une erreur est survenue lors de l'importation des plateforms.");
    }
  });
}

beginImportPlatforms = function() {
  showLoading();
  $('#link-update-platforms').hide();
}

endImportPlatforms = function() {
  hideLoading();
  $('#link-update-platforms').show();
}

backToStepOne = function() {
  $("#input-extra-informations").hide(300);
  $("#first-step").show(300);
  window.scrollTo(0, 0);
}

goToStepTwo = function() {
  $("#first-step").hide(300);
  $("#input-extra-informations").show(300);
  window.scrollTo(0, 0);
}