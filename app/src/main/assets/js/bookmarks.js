class BB10BrowserBookmarks {
  constructor() {
    this.formEl = document.querySelector('.js-addBookmarkForm');
    this.render();
  }

  get bookmarks() {
    return browserSettings.get('bookmarks');
  }

  set bookmarks(bookmarks) {
    return browserSettings.set('bookmarks', bookmarks);
  }

  render() {
    const bookmarksContainer = document.querySelector('.js-bookmarks');

    if (!bookmarksContainer) {
      return;
    }

    const bookmarkTemplate = document.querySelector('.js-bookmarkTemplate').innerHTML;
    const addBookmarkTemplate = document.querySelector('.js-addBookmarkTemplate').innerHTML;

    const html = this.bookmarks.map((bookmark, index) => {
      return bookmarkTemplate.replace('{{index}}', index)
        .replace('{{title}}', bookmark.title || '')
        .replace(/\{\{url\}\}/g, bookmark.url || '');
    }).join('') + addBookmarkTemplate;

    bookmarksContainer.innerHTML = html;
    this.attachEvents(bookmarksContainer);
  }

  attachEvents(bookmarksContainer) {
    const removeBookmarkElements = bookmarksContainer.querySelectorAll('.js-removeBookmark');
    const addBookmarkEl = bookmarksContainer.querySelector('.js-addBookmark');

    [...removeBookmarkElements].forEach(el => {
      el.addEventListener('click', this.remove.bind(this));
    });

    addBookmarkEl.addEventListener('click', this.showForm.bind(this));
  }

  remove(event) {
    event.preventDefault();
    event.stopPropagation();

    const {index} = this._getBookmarkByEvent(event).dataset;

    let {bookmarks} = this;
    bookmarks.splice(index, 1);
    this.bookmarks = bookmarks;

    this.render();
  }

  showForm() {
    if (!this.formEl) {
      return;
    }

    this.formEl.classList.remove('form--hidden');
    this.formEl.title.focus();
  }

  hideForm() {
    this.formEl.reset();
    this.formEl.classList.add('form--hidden');
  }

  add() {
    this.save(this.formEl.title.value, this.formEl.url.value);
    this.render();
    this.hideForm();
  }

  save(title, url) {
    if (!title || !url) {
      return;
    }

    let {bookmarks} = this;
    const existingIndex = bookmarks.map(({url}) => url).indexOf(url);

    if (existingIndex !== -1) {
      bookmarks[existingIndex].title = title;
    } else {
      bookmarks.push({title, url});
    }

    this.bookmarks = bookmarks;
  }

  _getBookmarkByEvent(event) {
    return event.path.find(el => el.className.includes('js-bookmark'));
  }
}

window.bookmarks = new BB10BrowserBookmarks();
