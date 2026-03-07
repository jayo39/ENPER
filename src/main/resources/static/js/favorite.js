function showFavorites(element) {
    const userId = $(element).data('id');
    const username = $(element).data('username');

    $('#fav-modal-username').text(username);
    const $list = $('#favorites-list');
    $list.empty();
    $('#favorites-loader').removeClass('d-none');

    $('#customFavModal').css('display', 'flex');

    $.ajax({
        url: `/favorite/list/${userId}`,
        method: 'GET',
        success: function(books) {
            $('#favorites-loader').addClass('d-none');

            if (!books || books.length === 0) {
                $list.append('<li class="list-group-item text-center py-4 text-muted">No favorited books yet.</li>');
                return;
            }

            books.forEach(book => {
                $list.append(`
                    <li class="list-group-item d-flex align-items-center justify-content-between">
                        <div class="d-flex align-items-center">
                            <div>
                                <div>${book.title}</div>
                                <div class="small text-muted">${book.series}</div>
                            </div>
                        </div>
                    </li>
                `);
            });
        },
        error: function() {
            $('#favorites-loader').addClass('d-none');
            $list.append('<li class="list-group-item text-danger text-center">Failed to load favorites.</li>');
        }
    });
}

function closeFavModal() {
    $('#customFavModal').css('display', 'none');
}

$(window).click(function(event) {
    if (event.target.id === 'customFavModal') {
        closeFavModal();
    }
});