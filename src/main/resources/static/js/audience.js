function secondsToClockDisplay(time) {
    var sec_num = parseInt(time, 10); // don't forget the second param
    var minutes = Math.floor(sec_num / 60);
    var seconds = sec_num - (minutes * 60);

    if (seconds < 10) {seconds = "0"+seconds;}
    return minutes+':'+seconds;
}

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

    stompClient.subscribe('/topic/clock/audio', function (audioFile) {
        audio[JSON.parse(audioFile.body).audioFile].play();
    });

    stompClient.subscribe('/topic/clock/control', function (control) {
        if(JSON.parse(control.body).control === 'stop-clock') {
           $("#clock").html("<font color='red'>0:00</font>");
        }
    });
});

});