// addHistory.js
$(document).on('click', '.clickable', function (e) {
  const bookId = $(this).children("input").val();
  if (!bookId) return;

  // Try to use the Beacon API if we’re about to leave the page
  if (e.metaKey || e.ctrlKey || e.shiftKey || e.button !== 0) {
    // user opened in new tab/window
    navigator.sendBeacon('/history/add', new URLSearchParams({ book_id: bookId }));
    return;
  }

  // Otherwise post then navigate
  $.post('/history/add', { book_id: bookId })
    .always(() => window.location = `/book/detail/${bookId}`)    // only leave when request finishes
    .fail(() => console.error('Failed to log history'));
});
