class BB10BrowserTabs {
  constructor() {
    tabs.render = this.renderTabs;
    tabs.getTabs('tabs.render');
  }

  renderTabs(tabsArray) {
    console.log(tabsArray);
  }
}

new BB10BrowserTabs();