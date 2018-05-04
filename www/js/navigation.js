class BB10BrowserNavigation {
  constructor(url) {
    this.attachEvents();
    this.openUrl(url);
  }

  openUrl(url) {
    this.loadUrl = null;
    navigation.openUrl(this.determineUrl(url));
  }

  determineUrl(input) {
    const isUrl = input.split('?')[0].indexOf(' ') === -1 && input.split('?')[0].indexOf('.') !== -1;
    const containsProtocol = isUrl && input.indexOf('://') !== -1;

    if (isUrl && containsProtocol) {
      return input;
    }

    if (isUrl) {
      return 'http://' + input;
    }

    const search = encodeURIComponent(input);
    return `https://www.google.com/search?q=${search}&oq=${search}`;
  }

  updateUrl(url) {
    if (url === this.loadUrl) {
      return;
    }

    const navigationUrlEl = document.querySelector('.navigation__url');
    navigationUrlEl.value = url;
  }

  attachEvents() {
    const navigationFormEl = document.querySelector('.navigation');
    const navigationUrlEl = document.querySelector('.navigation__url');

    navigationFormEl.addEventListener('submit', () => {
      this.openUrl(navigationUrlEl.value);
      navigationUrlEl.blur();
    });

    navigationUrlEl.addEventListener('focus', () => {
      console.log('select!');
      navigationUrlEl.setSelectionRange(0, navigationUrlEl.value.length);
    });

    navigationUrlEl.addEventListener('blur', () => {
      console.log('deselect!');
      navigationUrlEl.setSelectionRange(0, 0);
    });

    window.onNavigationEvent = this.onNavigationEvent.bind(this);
  }

  onNavigationEvent(event) {
    switch(event && event.type) {
      case 'loadstart':
        this.setLoadInProgress(true, event.url, event.navigationUrl);
        break;
      case 'loadstop':
        this.setLoadInProgress(false, event.url, event.navigationUrl);
        break;
      case 'loadprogress':
        this.setLoadProgress(event.progress);
        break;
      default:
        console.log('unhandled event:', event);
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
}

new BB10BrowserNavigation('https://google.com');
