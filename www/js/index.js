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

  _getNavigationHeight(viewportHeight = null) {
    viewportHeight = viewportHeight || this._getViewportHeight();
    const calculatedHeight = Math.ceil(viewportHeight * 0.1);
    const minHeight = 30 * window.devicePixelRatio;
    return calculatedHeight > minHeight ? calculatedHeight : minHeight;
  }

  _getContentHeight() {
    const viewportHeight = this._getViewportHeight();
    return viewportHeight - this._getNavigationHeight(viewportHeight);
  }

  attachEvents() {
    const navigationFormEl = document.querySelector('.navigation');
    const navigationUrlEl = document.querySelector('.navigation__url');

    navigationFormEl.addEventListener('submit', () => {
      this.browser.loadUrl(navigationUrlEl.value);
      navigationUrlEl.blur();
    });

    navigationUrlEl.addEventListener('focus', () => {
      this.browser.setFocusable(false);
    });

    navigationUrlEl.addEventListener('blur', () => {
      this.browser.setFocusable(true);
    });

    window.addEventListener('resize', event => {
      this.browser.resize(this._getContentHeight());
    });
  }
}

document.addEventListener('deviceready', () => {
  new BB10Browser('https://google.com');
}, false);
