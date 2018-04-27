class BB10Browser {
  constructor(url) {
    this.browser = window.inAppBrowserXwalk.open(url, {
      openHidden: false,
      height: this._getContentHeight()
    });

    this.attachEvents();
  }

  _getViewportHeight() {
    return window.innerHeight * window.devicePixelRatio;
  }

  _getContentHeight() {
    const viewportHeight = this._getViewportHeight();
    return viewportHeight - Math.ceil(viewportHeight * 0.1);
  }

  attachEvents() {
    const navigationFormEl = document.querySelector('.navigation');
    const navigationUrlEl = document.querySelector('.navigation__url');

    navigationFormEl.addEventListener('submit', () => {
      this.browser.loadUrl(navigationUrlEl.value);
      navigationUrlEl.blur();
    });

    window.addEventListener('resize', event => {
      this.browser.resize(this._getContentHeight());
    });
  }
}

document.addEventListener('deviceready', () => {
  new BB10Browser('https://google.com');
}, false);
