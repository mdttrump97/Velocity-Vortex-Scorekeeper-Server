var clockButtonLookup = {
    "start-autonomous": function () {
        $("#start-autonomous").prop('disabled', true);
        $("#start-teleop").prop('disabled', true);
        $("#reset-clock").prop('disabled', true);
        $("#stop-clock").prop('disabled', false);
    },
    "end-autonomous": function () {
        $("#start-autonomous").prop('disabled', false);
        $("#start-teleop").prop('disabled', false);
        $("#reset-clock").prop('disabled', false);
        $("#stop-clock").prop('disabled', true);
    },
    "end-teleop": function () {
        $("#start-autonomous").prop('disabled', false);
        $("#start-teleop").prop('disabled', false);
        $("#reset-clock").prop('disabled', false);
        $("#stop-clock").prop('disabled', true);
    },
    "start-teleop": function () {
        $("#start-autonomous").prop('disabled', true);
        $("#start-teleop").prop('disabled', true);
        $("#reset-clock").prop('disabled', true);
        $("#stop-clock").prop('disabled', false);
    },
    "reset-clock": function () {},
    "stop-clock": function () {
        $("#clock").html("<font color='red'>0:00</font>");
        $("#start-autonomous").prop('disabled', false);
        $("#start-teleop").prop('disabled', false);
        $("#reset-clock").prop('disabled', false);
        $("#stop-clock").prop('disabled', true);
    },
}

let submitMatch;
var currentMatchNumber = 1;

$(function() {

submitMatch = function(match) {
    stompClient.send("/topic/matches/currentMatch", {}, match);
};

let stompClient;

function stompConnect() {
    var socket = new SockJS('/scorekeeper/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, stompSuccess, stompError);
};

function stompSuccess(frame) {
    $("#disconnect-alert").fadeOut();
    $("#grey-out-overlay").fadeOut();

    stompClient.subscribe('/topic/scores', function (score) {
        $.each(JSON.parse(score.body), function(i, e) {
            var id = '#' + e.gameMode + "-" + e.alliance + "-" + e.goal;
            $(id).html(e.score);
        });
    });

    stompClient.subscribe('/topic/clock/time', function (time) {
        secondsToClockDisplay(JSON.parse(time.body).time);
    });

    stompClient.subscribe('/topic/clock/control', function (control) {
        clockButtonLookup[JSON.parse(control.body).control]();
    });

    stompClient.subscribe('/topic/matches', function (matches) {
        $.each(JSON.parse(matches.body), function(i, match) {
            $("#matches > tbody:last-child").append(
                "<tr><td><input onChange='submitMatch(this.value)' id='match" + match.matchNumber + "' type='radio' name='currentMatch' value='" + JSON.stringify(match) + "'>" +
                '<label for="match' + match.matchNumber + '">&nbsp' + match.matchNumber + "</label></td><td>" +
                match.red1 + "</td><td>" + match.red2 + "</td><td>" +
                match.blue1 + "</td><td>" + match.blue2 + "</td></tr>"
            );
        });
    });

    stompClient.subscribe('/topic/matches/currentMatch', function (match) {
        match = JSON.parse(match.body);
        var id = "#match" + match.matchNumber;
        $(id).prop("checked", true);
        currentMatchNumber = match.matchNumber;
        $("#currentMatch").html("Current Match: " + currentMatchNumber)
    });
};

function stompError(error) {
    $("#disconnect-alert").fadeIn();
    $("#grey-out-overlay").fadeIn();
    setTimeout(stompConnect, 1000);
    if (!isNaN(localTime) && localTime != 150 && localTime != 120 && localTime != 0) {
        secondsToClockDisplay(localTime-1);
    }
};

stompConnect();

$("#start-autonomous").click(function () {
    stompClient.send("/topic/clock/control", {}, JSON.stringify({"control": "start-autonomous"}));
});

$("#start-teleop").click(function () {
    stompClient.send("/topic/clock/control", {}, JSON.stringify({"control": "start-teleop"}));
});

$("#reset-clock").click(function () {
    stompClient.send("/topic/clock/control", {}, JSON.stringify({"control": "reset-clock"}));
});

$("#stop-clock").click(function () {
    stompClient.send("/topic/clock/control", {}, JSON.stringify({"control": "stop-clock"}));
});

$('[data-toggle=confirmation]').confirmation({
  rootSelector: '[data-toggle=confirmation]',
  // other options
});

$("#clear-scores").on("confirmed.bs.confirmation", function() {
    stompClient.send("/topic/scores/reset", {}, JSON.stringify({"reset": "reset"}));
});

$("#next-match").click(function () {
    currentMatchNumber += 1;
    var id = "#match" + currentMatchNumber;
    submitMatch($(id).val());
});

});