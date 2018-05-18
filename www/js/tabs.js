class BB10BrowserTabs {
  constructor() {
    tabs.render = this.renderTabs.bind(this);
    tabs.getTabs('tabs.render');
  }

  renderTabs(tabsArray) {
    const tabsContainer = document.querySelector('.js-tabs');
    const tabTemplate = document.querySelector('.js-tabTemplate').innerHTML;
    const addTabTemplate = document.querySelector('.js-addTabTemplate').innerHTML;

    const html = (tabsArray || []).map((tab, index) => {
      return tabTemplate.replace('{{index}}', index)
        .replace('{{title}}', tab.navigationTitle || '')
        .replace('{{url}}', tab.navigationUrl || tab.navigationOriginalUrl || '');
    }).join('') + addTabTemplate;

    tabsContainer.innerHTML = html;
    this.attachEvents(tabsContainer);
  }

  attachEvents(tabsContainer) {
    const tabElements = tabsContainer.querySelectorAll('.js-tab');
    const closeElements = tabsContainer.querySelectorAll('.js-closeTab');
    const addTabElement = tabsContainer.querySelector('.js-addTab');

    [...tabElements].forEach(tabEl => {
      tabEl.addEventListener('click', this.openTab.bind(this));
    });

    [...closeElements].forEach(closeEl => {
      closeEl.addEventListener('click', this.closeTab.bind(this));
    });

    addTabElement.addEventListener('click', this.addTab.bind(this));
  }

  openTab(event) {
    const index = parseInt(this._getTabByEvent(event).dataset.index, 10);
    tabs.openTab(index);
  }

  closeTab(event) {
    event.stopPropagation();

    const index = parseInt(this._getTabByEvent(event).dataset.index, 10);
    tabs.closeTab(index, 'tabs.render');
  }

  addTab(event) {
    tabs.addTab('about:blank');
  }

  _getTabByEvent(event) {
    return event.path.find(el => el.className.includes('js-tab'));
  }
}

new BB10BrowserTabs();