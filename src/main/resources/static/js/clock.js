function secondsToClockDisplay(time) {
    var sec_num = parseInt(time, 10); // don't forget the second param
    var minutes = Math.floor(sec_num / 60);
    var seconds = sec_num - (minutes * 60);

    if (seconds < 10) {seconds = "0"+seconds;}
    return minutes+':'+seconds;
}

$(function() {

var socket = new SockJS('/scorekeeper/ws');
var stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {
    stompClient.subscribe('/topic/clock/time', function (time) {
        $("#clock").html(secondsToClockDisplay(JSON.parse(time.body).time));
    });

    stompClient.subscribe('/topic/clock/control', function (control) {
        if(JSON.parse(control.body).control === 'stop-clock') {
            $("#clock").html("<font color='red'>0:00</font>");
        }
    });
});

});