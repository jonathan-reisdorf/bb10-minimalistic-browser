document.addEventListener('deviceready', () => {
  const viewportHeight = window.innerHeight * window.devicePixelRatio;
  const browser = window.inAppBrowserXwalk.open('https://google.com', {
    openHidden: false,
    height: viewportHeight - Math.ceil(viewportHeight * 0.1)
  });

  const navigationUrlEl = document.querySelector('.navigation__url');
  navigationUrlEl.addEventListener('keyup', event => {
    if (event.code === 'Enter') {
      browser.loadUrl(navigationUrlEl.value);
      navigationUrlEl.blur();
    }
  });
}, false);
