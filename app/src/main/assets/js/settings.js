class BB10BrowserSettings {
  constructor() {
    this._elements = [...document.querySelectorAll('.js-setting')];
    this._initialize();
  }

  get DEFAULTS() {
    return {
      'search.engine': 'ddg'
    };
  }

  _initialize() {
    try {
      this._settings = JSON.parse(localStorage.getItem('beolSettings') || '{}');
    } catch(e) {
      this._settings = {};
      console.warn('Error retrieving browser settings:', e);
    }

    this._initializeElements();
  }

  _initializeElements() {
    this._elements.forEach(el => {
      const key = el.dataset.setting;
      let value = this.get(key);

      if (typeof value === 'undefined') {
        value = this.DEFAULTS[key];
      }

      el.value = value;

      el.addEventListener('change', () => this.set(key, el.value));
      el.classList.remove('setting--uninitialized');
    });
  }

  get(key) {
    const path = key.split('.');
    return (this._traverse(path, this._settings) || {})._value;
  }

  set(key, value) {
    let path = key.split('.');
    const end = path.pop();
    let data = this._traverse(path, this._settings, true);
    data[end] = Object.assign({}, data[end], { _value: value });

    localStorage.setItem('beolSettings', JSON.stringify(this._settings));
    this.onSaved(key);
  }

  onSaved(key) {
    alert(`Setting ${key} saved`);
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
