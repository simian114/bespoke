function setCookie(name, value, days) {
    var date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000)); // days를 밀리초로 변환
    var expires = "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

function saveTopBanner(e) {
    // TODO: session storage 에 저장
    const ret = e.detail.xhr.response;
    sessionStorage.setItem('topBanner', ret);
}

function handleAfterOnLoad(e) {
    const path = e.detail.pathInfo.requestPath;
    if (path === "/banner/top-banner") {
        //
    } else {
        // 추가..
    }
}
