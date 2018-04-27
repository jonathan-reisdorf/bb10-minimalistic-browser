document.addEventListener('deviceready', () => {
  const viewportHeight = window.innerHeight * window.devicePixelRatio;
  const browser = window.inAppBrowserXwalk.open('https://google.com', {
    openHidden: false,
    height: viewportHeight - Math.ceil(viewportHeight * 0.1)
  });

  console.log(browser);
}, false);
