class BB10BrowserContext {
  constructor() {
    this.pageTitle = pageContext.title();
    this.pageUrl = pageContext.url();

    this.initialize();
  }

  initialize() {
    if (this.isSystemFile) {
      return;
    }

    this.renderCaption();
    this.initializeReload();

    document.querySelector('.js-contextMenu').classList.remove('menu--uninitialized');
  }

  initializeReload() {
    const reloadEl = document.querySelector('.js-contextReload');
    let timeout;

    reloadEl.addEventListener('touchstart', event => {
      timeout = setTimeout(() => this.reloadPage(true), 1000);
    });

    reloadEl.addEventListener('touchend', () => clearTimeout(timeout));
  }

  get isSystemFile() {
    return this.pageUrl.indexOf('file:///android_asset/') === 0;
  }

  renderCaption() {
    const captionEl = document.querySelector('.js-contextCaption');
    captionEl.innerHTML = `${this.pageTitle} &#8212; ${this.pageUrl}`;
  }

  reloadPage(reloadForced) {
    pageContext.reload(!!reloadForced);
  }

  loadPage(url) {
    // resolve relative links as well
  	const link = document.createElement('a');
  	link.href = url;
  	pageContext.load(link.href);
  }
}

const browserContext = new BB10BrowserContext();