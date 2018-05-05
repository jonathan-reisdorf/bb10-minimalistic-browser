class BB10Browser {
  constructor(url) {
    this.browser = window.inAppBrowserXwalk.open(url, {
      openHidden: false,
      navigationHeight: 40
    });
  }
}

document.addEventListener('deviceready', () => {
  new BB10Browser('https://google.com');
}, false);
