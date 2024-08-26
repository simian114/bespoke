// NOTE: surreal 로 대체. surreal 은 LoB 에 좋음
document.addEventListener('DOMContentLoaded', () => {

    // Get all "navbar-burger" elements
    const $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);

    // Add a click event on each of them
    $navbarBurgers.forEach(el => {
        el.addEventListener('click', () => {

            // Get the target from the "data-target" attribute
            const target = el.dataset.target;
            const $target = document.getElementById(target);

            // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
            el.classList.toggle('is-active');
            $target.classList.toggle('is-active');

        });
    });
});

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
