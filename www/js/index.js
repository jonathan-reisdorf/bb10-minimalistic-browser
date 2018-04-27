document.addEventListener('deviceready', () => {
  const viewportHeight = window.innerHeight * window.devicePixelRatio;
  const browser = window.inAppBrowserXwalk.open('https://google.com', {
    openHidden: false,
    height: viewportHeight - Math.ceil(viewportHeight * 0.1)
  });

  const navigationFormEl = document.querySelector('.navigation');
  const navigationUrlEl = document.querySelector('.navigation__url');

  navigationFormEl.addEventListener('submit', () => {
    browser.loadUrl(navigationUrlEl.value);
    navigationUrlEl.blur();
  });
}, false);
