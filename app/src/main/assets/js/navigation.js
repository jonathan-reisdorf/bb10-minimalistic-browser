class BB10BrowserSearch {
  static get engine() {
    return browserSettings.get('search.engine');
  }

  static get homepageUrl() {
    return {
      google: 'https://www.google.com/',
      ddg: 'https://duckduckgo.com/'
    }[this.engine];
  }

  static getSearchUrl(query) {
    return {
      google: `https://www.google.com/search?q=${query}&oq=${query}`,
      ddg: `https://duckduckgo.com/?q=${query}`
    }[this.engine];
  }
}


class BB10BrowserNavigationActions {
  constructor(navigationReference) {
    this.elements = [...document.querySelectorAll('.js-navigationAction')];
    this.activeEl = null;
    this.isPending = false;
    this.navigationReference = navigationReference;
  }

  attachEvents() {
    this.elements.forEach(el => {
      el.addEventListener('click', () => {
        if (this.isPending) {
          return;
        }

        if (el.dataset.toggle) {
          this.activeEl = this.activeEl ? null : el;
        }

        this.renderStatus();
        this.execute(el);
      });
    });

    window.onSystemTabOpen = this.onSystemTabOpen.bind(this);
    window.onSystemTabClose = this.onSystemTabClose.bind(this);
  }

  renderStatus() {
    const activeClassName = 'navigation__action--active';

    this.elements.forEach(el => el.classList.remove(activeClassName));
    if (this.activeEl) {
      this.activeEl.classList.add(activeClassName);
    }
  }

  execute(el) {
    if (!el.dataset.action) {
      return;
    }

    this[el.dataset.action]();
  }

  toggleSystemTab(openAction) {
    this.isPending = true;

    if (!this.activeEl) {
      return navigation.closeSystemTab();
    }

    return navigation[openAction]();
  }

  toggleTabsOverview() {
    this.toggleSystemTab('showTabsOverview');
  }

  toggleContext() {
    this.toggleSystemTab('showContextMenu');
  }

  goPrev() {
    navigation.goPrev();
  }

  goNext() {
    navigation.goNext();
  }

  onSystemTabClose() {
    this.activeEl = null;
    this.isPending = false;
    this.renderStatus();

    browserSettings.update();
    this.navigationReference.enableInput();
  }

  onSystemTabOpen() {
    this.isPending = false;
    this.navigationReference.disableInput();
  }

  updatePrev(show) {
    if (this.isPrevShown === show) {
      return;
    }

    this.isPrevShown = show;
    this.updateHideable('goPrev', show);
  }

  updateNext(show) {
    if (this.isNextShown === show) {
      return;
    }

    this.isNextShown = show;
    this.updateHideable('goNext', show);
  }

  updateHideable(action, show) {
    const targetEl = this.elements.find(el => el.dataset.action === action);

    if (!targetEl) {
      return;
    }

    targetEl.classList[show ? 'remove' : 'add']('navigation__action--hidden');
  }
}


class BB10BrowserNavigationConvenience {
  constructor() {
    if (!window.navigationConvenience) {
      return;
    }

    this.update();
    browserSettings.hooks.push(this.update.bind(this));
  }

  get isEnabled() {
    return browserSettings.get('navigation.autopaste');
  }

  update() {
    navigationConvenience.setEnabled(!!this.isEnabled);
  }

  getClipboardText() {
    const clipboardText = this.isEnabled && navigationConvenience.getClipboardText() || null;

    if (clipboardText === this._previousClipboardText) {
      return null;
    }

    this._previousClipboardText = clipboardText;
    return clipboardText;
  }
}


class BB10BrowserNavigation {
  constructor() {
    this.navigationUrlEl = document.querySelector('.navigation__url');

    this.actions = new BB10BrowserNavigationActions(this);
    this.convenience = new BB10BrowserNavigationConvenience();
    this.attachEvents();
  }

  openUrl(url) {
    this.loadUrl = null;
    navigation.openUrl(this.determineUrl(url, true));
  }

  determineUrl(input, useSearchQueryFallback = false) {
    input = (input || '').trim();

    const isUrl = input.split('?')[0].indexOf(' ') === -1 && input.split('?')[0].indexOf('.') !== -1;
    const containsProtocol = isUrl && input.indexOf('://') !== -1;

    if (isUrl && containsProtocol) {
      return input;
    }

    if (isUrl) {
      return 'http://' + input;
    }

    if (!useSearchQueryFallback) {
      return false;
    }

    const query = encodeURIComponent(input);
    return BB10BrowserSearch.getSearchUrl(query);
  }

  updateUrl(url) {
    if (url === this.loadUrl) {
      return;
    }

    const navigationUrlEl = document.querySelector('.navigation__url');
    navigationUrlEl.dataset.url = url;

    if (document.hasFocus() && document.activeElement === navigationUrlEl) {
      return;
    }

    navigationUrlEl.value = url;
  }

  attachEvents() {
    const navigationFormEl = document.querySelector('.js-navigationForm');
    const {navigationUrlEl} = this;

    navigationFormEl.addEventListener('submit', () => {
      navigationUrlEl.dataset.url = navigationUrlEl.value;
      navigationUrlEl.blur();
      this.openUrl(navigationUrlEl.value);
    });

    navigationUrlEl.addEventListener('focus', () => {
      navigationUrlEl.setSelectionRange(0, navigationUrlEl.value.length);
    });

    navigationUrlEl.addEventListener('blur', () => {
      navigationUrlEl.value = navigationUrlEl.dataset.url || '';
      setTimeout(() => navigationUrlEl.setSelectionRange(0, 0), 50);
    });

    this.actions.attachEvents();

    window.onNavigationEvent = this.onNavigationEvent.bind(this);
    window.onApplicationResume = this.onApplicationResume.bind(this);
  }

  onNavigationEvent(event) {
    if (!event) {
      return;
    }

    switch(event.type) {
      case 'loadstart':
        this.setLoadInProgress(true, event.url, event.navigationUrl);
        break;
      case 'loadstop':
        this.setLoadInProgress(false, event.url, event.navigationUrl);
        break;
      case 'loadprogress':
        this.setLoadProgress(event.progress);
        break;
      case 'status':
        this.setLoadInProgress(false, event.navigationUrl, event.navigationUrl);
        break;
      default:
        console.warn('unhandled event:', event);
    }

    this.onNavigationHistoryEvent(event);
  }

  onNavigationHistoryEvent(event) {
    if (typeof event.navigationHasPrev !== 'undefined') {
      this.actions.updatePrev(event.navigationHasPrev);
    }

    if (typeof event.navigationHasNext !== 'undefined') {
      this.actions.updateNext(event.navigationHasNext);
    }
  }

  setLoadInProgress(isInProgress, url, navigationUrl) {
    if (!url || (this.loadUrl && url !== navigationUrl)) {
        return;
    }

    this.loadInProgress = isInProgress;
    this.updateUrl(url);
    this.loadUrl = url;

    this.renderProgress();
  }

  setLoadProgress(progress) {
    this.loadProgress = parseInt(progress, 10);
    this.renderProgress();
  }

  renderProgress() {
    const navigationIndicatorEl = document.querySelector('.navigation__indicator');

    if (!this.loadInProgress && this.loadProgress === 100) {
      navigationIndicatorEl.style.display = 'none';
      return;
    }

    const navigationProgressEl = document.querySelector('.navigation__progress');
    navigationProgressEl.style.width = this.loadProgress + '%';
    navigationIndicatorEl.style.display = 'block';
  }

  disableInput() {
    this.navigationUrlEl.disabled = true;
  }

  enableInput() {
    this.navigationUrlEl.disabled = false;
  }

  get homepageUrl() {
    const homepageType = browserSettings.get('navigation.homepage.type');

    switch (homepageType) {
      case 'bookmarks':
        return browserSettings.get('bookmarks.url');
      case 'search':
        return BB10BrowserSearch.homepageUrl;
      default:
        return browserSettings.get('navigation.homepage.url');
    }
  }

  get defaultUrl() {
    return this.clipboardUrl || this.homepageUrl;
  }

  get clipboardUrl() {
    return this.determineUrl(this.convenience.getClipboardText());
  }

  onApplicationResume() {
    const clipboardUrl = this.clipboardUrl;

    if (clipboardUrl) {
      this.openUrl(clipboardUrl);
    }
  }
}

const browserNavigation = new BB10BrowserNavigation();
browserNavigation.openUrl(browserNavigation.defaultUrl);
