let localTime;
function secondsToClockDisplay(time) {
    localTime = time;
    var sec_num = parseInt(time, 10);
    var minutes = Math.floor(sec_num / 60);
    var seconds = sec_num - (minutes * 60);

    if (seconds < 10) {seconds = "0"+seconds;}
    $("#clock").html(minutes+':'+seconds);
}

