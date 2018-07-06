class BB10BrowserSettings {
  constructor() {
    this._elements = [...document.querySelectorAll('.js-setting')];
    this._conditionalElements = [...document.querySelectorAll('.js-settingConditional')];
    this._calculatedSettings = [...document.querySelectorAll('.js-settingCalculated')];
    this._initialize();
  }

  static get DEFAULTS() {
    return {
      'bookmarks': [
        {
          title: 'DuckDuckGo',
          url: 'https://duckduckgo.com/'
        }
      ],
      'bookmarks.url': 'file:///android_asset/bookmarks.html',
      'search.engine': 'ddg',
      'navigation.homepage.type': 'bookmarks',
      'navigation.homepage.url': '',
      'navigation.autopaste': false,
      'navigation.userAgent.type': 'default',
      'navigation.userAgent.custom': '',
      'navigation.userAgent.presets': {
        chromeMobile: 'Mozilla/5.0 (Linux; Android 4.3; Passport Build/10.3.3.213) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3452.0 Mobile Safari/537.36',
        chromeDesktop: 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36',
        firefoxDesktop: 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:61.0) Gecko/20100101 Firefox/61.0'
      }
    };
  }

  get userAgent() {
    const type = this.get('navigation.userAgent.type');

    if (type === 'custom') {
      return this.get('navigation.userAgent.custom');
    }

    return this.get('navigation.userAgent.presets')[type] || navigator.userAgent;
  }

  _initialize() {
    this.hooks = [];
    this.update();
    this._initializeElements();
  }

  _initializeElements() {
    this._elements.forEach(el => {
      const key = el.dataset.setting;

      this.setElValue(el, this.get(key));
      el.addEventListener('change', () => {
        this.set(key, this.getElValue(el));
        this._updateConditionalSettings();
        this._updateCalculatedSettings();
      });
      el.classList.remove('setting--uninitialized');
    });

    this._updateConditionalSettings();
    this._updateCalculatedSettings();
  }

  _updateConditionalSettings() {
    this._conditionalElements.forEach(el => {
      const actualValue = this.get(el.dataset.settingConditionalKey);
      let expectedValue = el.dataset.settingConditionalValue;
      const isNegation = expectedValue.charAt(0) === '!';
      expectedValue = isNegation ? expectedValue.slice(1) : expectedValue;

      const isConditionMet = (expectedValue === actualValue) !== isNegation;
      el.classList[isConditionMet ? 'remove' : 'add']('setting--hidden');
      el.classList.remove('setting--uninitialized');
    });
  }

  _updateCalculatedSettings() {
    this._calculatedSettings.forEach(el => {
      const getterFn = el.dataset.settingCalculate;
      el.innerHTML = this[getterFn];
    });
  }

  setElValue(el, value) {
    el.type === 'checkbox' ? el.checked = !!value : el.value = value;
  }

  getElValue(el) {
    return el.type === 'checkbox' ? !!el.checked : el.value;
  }

  update() {
    try {
      this._settings = JSON.parse(localStorage.getItem('beolSettings') || '{}');
    } catch(e) {
      this._settings = this._settings || {};
      console.warn('Error retrieving browser settings:', e);
    }

    this.hooks.forEach(hook => hook(this._settings));
  }

  get(key) {
    const path = key.split('.');
    let value = (this._traverse(path, this._settings) || {})._value;

    return typeof value !== 'undefined' ? value : BB10BrowserSettings.DEFAULTS[key];
  }

  set(key, value) {
    let path = key.split('.');
    const end = path.pop();
    let data = this._traverse(path, this._settings, true);
    data[end] = Object.assign({}, data[end], { _value: value });

    localStorage.setItem('beolSettings', JSON.stringify(this._settings));
    this.onSaved(key, value);
    return value;
  }

  onSaved(key, value) {
    this.showMessage(`setting "${key}" changed to ${JSON.stringify(value)}`);
  }

  showMessage(message) {
      const messagesContainer = document.querySelector('.js-messages');

      if (!messagesContainer) {
          return; // alert(message)
      }

      const messageEl = document.createElement('div');
      messageEl.className = 'message';
      messageEl.innerHTML = message;

      document.querySelector('.js-messages').appendChild(messageEl);
      setTimeout(() => messageEl.remove(), 3500);
  }

  _traverse(path, data, create) {
    if (!path.length) {
      return data;
    }

    const segment = path.shift();

    if (!data[segment] && (path.length || create)) {
      data[segment] = {};
    }

    return this._traverse(path, data[segment]);
  }
}


window.browserSettings = new BB10BrowserSettings();
