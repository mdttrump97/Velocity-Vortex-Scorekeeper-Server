var audio = {
    "start-autonomous": new Audio('sound/startauto.wav'),
    "end-autonomous": new Audio('sound/endauto.wav'),
    "start-teleop": new Audio('sound/startteleop.wav'),
    "end-teleop": new Audio('sound/endteleop.wav'),
    "time-running-out": new Audio('sound/30sec.wav'),
    "e-stop": new Audio('sound/estop.wav'),
}

$(function(){

var mq = window.matchMedia("(min-aspect-ratio: 32/21)");
mq.addListener(aspectRatioChange);
aspectRatioChange(mq);
function aspectRatioChange(mq) {
    if (mq.matches) {
        $("#background-image").attr("src", "images/bg16x9.png")
    } else {
        $("#background-image").attr("src", "images/bg4x3.png")
    }
}

let stompClient;
let stompSuccess;
let stompError;
function stompConnect() {
    var socket = new SockJS('/scorekeeper/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, stompSuccess, stompError);
};

stompSuccess = function (frame) {
    stompClient.subscribe('/topic/scores', function (score) {
        $.each(JSON.parse(score.body), function(i, e) {
            var id = '#' + e.gameMode + "-" + e.alliance + "-" + e.goal;
            $(id).html(e.score);
        })
    });

    stompClient.subscribe('/topic/clock/time', function (time) {
        secondsToClockDisplay(JSON.parse(time.body).time);
    });

    stompClient.subscribe('/topic/clock/audio', function (audioFile) {
        audio[JSON.parse(audioFile.body).audioFile].play();
    });

    stompClient.subscribe('/topic/clock/control', function (control) {
        if(JSON.parse(control.body).control === 'stop-clock') {
           $("#clock").html("<font color='red'>0:00</font>");
        }
    });

    stompClient.subscribe('/topic/matches/currentMatch', function (match) {
        match = JSON.parse(match.body);
        $("#red1").html(match.red1);
        $("#red2").html(match.red2);
        $("#blue1").html(match.blue1);
        $("#blue2").html(match.blue2);
    });
};

stompError = function (error) {
    $("#error-indicator").show();
    setTimeout(stompConnect, 1000);
    if (localTime != 150 && localTime != 120 && localTime != 0) {
        secondsToClockDisplay(localTime-1);
    }
};

stompConnect();

});