class BB10BrowserContext {
  constructor() {
    this.pageTitle = pageContext.title();
    this.pageUrl = pageContext.url();

    this.initialize();
  }

  initialize() {
    if (this.isSystemFile || !this.pageUrl) {
      return;
    }

    this.renderCaption();
    this.initializeReload();
    this.updateBookmarkStatus();

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
    captionEl.innerHTML = [this.pageTitle, this.pageUrl].filter(item => item).join(' &#8212; ');
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

  get isBookmarked() {
    return bookmarks.getIndexByUrl(this.pageUrl) !== -1;
  }

  updateBookmarkStatus() {
    const addEl = document.querySelector('.js-contextAddBookmark');
    const removeEl = document.querySelector('.js-contextRemoveBookmark');
    const {isBookmarked} = this;

    addEl.classList[isBookmarked ? 'add' : 'remove']('menu__item--hidden');
    removeEl.classList[!isBookmarked ? 'add' : 'remove']('menu__item--hidden');
  }

  bookmarkPage() {
    bookmarks.save(this.pageTitle || this.pageUrl, this.pageUrl);
    this.updateBookmarkStatus();
  }

  unbookmarkPage() {
    const index = bookmarks.getIndexByUrl(this.pageUrl);
    bookmarks.removeByIndex(index);
    this.updateBookmarkStatus();
  }
}

const browserContext = new BB10BrowserContext();