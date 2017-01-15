$(function() {

let stompClient;
function stompConnect() {
    var socket = new SockJS('/scorekeeper/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, stompSuccess, stompError);
};

function stompSuccess(frame) {
    $("#error-indicator").hide();

    stompClient.subscribe('/topic/clock/time', function (time) {
        secondsToClockDisplay(JSON.parse(time.body).time);
    });

    stompClient.subscribe('/topic/clock/control', function (control) {
        if(JSON.parse(control.body).control === 'stop-clock') {
            $("#clock").html("<font color='red'>0:00</font>");
        }
    });
};

function stompError(error) {
    $("#error-indicator").show();
    setTimeout(stompConnect, 1000);
    if (!isNaN(localTime) && localTime != 150 && localTime != 120 && localTime != 0) {
        secondsToClockDisplay(localTime-1);
    }
};

stompConnect();

});