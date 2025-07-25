$(document).on('click', '.clickable', function (e) {
  const bookId = $(this).children("input").val();
  if (!bookId) return;
  window.location = `/book/detail/${bookId}`
});