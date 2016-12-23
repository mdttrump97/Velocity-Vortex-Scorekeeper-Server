function secondsToClockDisplay(time) {
    var sec_num = parseInt(time, 10); // don't forget the second param
    var minutes = Math.floor(sec_num / 60);
    var seconds = sec_num - (minutes * 60);

    if (seconds < 10) {seconds = "0"+seconds;}
    return minutes+':'+seconds;
}

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


$(function() {

var socket = new SockJS('/scorekeeper/ws');
var stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {
    stompClient.subscribe('/topic/scores', function (score) {
        $.each(JSON.parse(score.body), function(i, e) {
            var id = '#' + e.gameMode + "-" + e.alliance + "-" + e.goal;
            $(id).html(e.score);
        })
    });

    stompClient.subscribe('/topic/clock/time', function (time) {
        $("#clock").html(secondsToClockDisplay(JSON.parse(time.body).time));
    });

    stompClient.subscribe('/topic/clock/control', function (control) {
        clockButtonLookup[JSON.parse(control.body).control]();
    });
});


$("#start-autonomous").click(function () {
    stompClient.send("/topic/clock/control", {}, JSON.stringify({"control": "start-autonomous"}));
});

$("#start-teleop").click(function () {
    stompClient.send("/topic/clock/control", {}, JSON.stringify({"control": "start-teleop"}));
});

$("#reset-clock").click(function () {
    stompClient.send("/topic/clock/control", {}, JSON.stringify({"control": "reset-clock"}));
})

$("#stop-clock").click(function () {
    stompClient.send("/topic/clock/control", {}, JSON.stringify({"control": "stop-clock"}));
})

$('[data-toggle=confirmation]').confirmation({
  rootSelector: '[data-toggle=confirmation]',
  // other options
});

$("#clear-scores").on("confirmed.bs.confirmation", function() {
    stompClient.send("/topic/scores/reset", {}, JSON.stringify({"reset": "reset"}));
})

});