$(document).on('click', '.star-bookmark', function(e) {
    e.stopPropagation();
    e.preventDefault();

    if (!isUserLoggedIn) {
        window.location.href = '/user/login';
        return false;
    }

    const $star = $(this);
    const $row = $star.closest('tr');
    const $tbody = $row.parent();
    const bookId = $row.find('input[type="hidden"]').val();

    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = parseInt(urlParams.get('page')) || 1;

    $.ajax({
        url: '/favorite/toggle/' + bookId,
        type: 'POST',
        success: function(isFavorite) {
            const refreshUrl = window.location.href;
            const $star = $row.find('.star-bookmark');

            if (isFavorite) {
                $star.removeClass('fa-regular').addClass('fa-solid');
            } else {
                $star.removeClass('fa-solid').addClass('fa-regular');
            }

            $.get(refreshUrl, function(data) {
                const $newRows = $(data).find('tbody tr');

                $row.fadeOut(300, function() {
                    $tbody.html($newRows);

                    const $newlyMovedRow = $tbody.find(`input[value="${bookId}"]`).closest('tr');
                    if ($newlyMovedRow.length > 0) {
                        $newlyMovedRow.hide().fadeIn(300);
                    }
                });
            });
        },
        error: function(err) {
            if(err.status === 401) window.location = `/user/login`;
        }
    });
});

$(document).on('click', '.clickable', function (e) {
  const bookId = $(this).children("input").val();
  if (!bookId) return;
  window.location = `/book/detail/${bookId}`
});