$(function() {
    $('#pageStart').keypress(function(e) {
        if(e.which === 13) {
            e.preventDefault();
            $('#pageEnd').focus();
        }
    });
    $('#pageEnd').keypress(function(e) {
        if(e.which === 13) {
            e.preventDefault();
            var firstPage = $('#pageStart').val();
            var lastPage = $('#pageEnd').val();
            var book_id = $('#book_id').val();
            loadDetail(firstPage, lastPage, book_id);
        }
    });

    $('.category').on("change", function() {
        var selectedCategory = $(this).val();
        if(selectedCategory === "General Info") {
            $('#detailed').addClass('d-none');
            $('#general').removeClass('d-none');
            $('#detailedsummary').removeAttr("selected");
            $('#generalinfo').attr('selected', 'selected');
        } else if (selectedCategory === "Detailed Summary") {
            $('#general').addClass('d-none');
            $('#detailed').removeClass('d-none');
            $('#generalinfo').removeAttr("selected");
            $('#detailedsummary').attr('selected', 'selected');
        }
    });

    $('#searchBtn').click(function() {
        var firstPage = $('#pageStart').val();
        var lastPage = $('#pageEnd').val();
        var book_id = $('#book_id').val();
        loadDetail(firstPage, lastPage, book_id);
    });
});

function loadDetail(firstPage, lastPage, book_id) {
    $.ajax({
        url: "/detail/list?firstPage=" + firstPage + "&lastPage=" + lastPage + "&book_id=" + book_id,
        type: "GET",
        cache: false,
        success: function(data, status, xhr) {
            buildDetail(data);
            addDelete(firstPage, lastPage, book_id);
        }
    });
}

function buildDetail(result) {
    const out = [];
    result.forEach(detail => {
        let id = detail.id;
        let firstPage = detail.firstPage;
        let lastPage = detail.lastPage;
        let content = detail.detail;
        const row = `
            <div id="detail-${id}" class="my-2">
                <div class="border bg-light rounded p-2">
                    <p class="fw-bold">${firstPage} - ${lastPage}</p>
                    <p>${content}</p>
                    <div class="d-flex justify-content-end">
                        <button class="btn btn-outline-danger" data-detail-id="${id}">Delete</button>
                    </div>
                </div>
            </div>
        `;
        out.push(row);
    });
    $('#pageInfo').html(out.join("\n"));
}

function addDelete(firstPage, lastPage, book_id) {
    $("[data-detail-id]").click(function() {
        const detail_id = $(this).attr("data-detail-id");
        $.ajax({
            url: "/detail/delete",
            type: "POST",
            cache: false,
            data: {"detail_id": detail_id},
            success: function(data, status, xhr) {
                loadDetail(firstPage, lastPage, book_id);
            }
        });
    });
}