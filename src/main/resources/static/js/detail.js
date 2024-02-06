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
            addEdit();
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
        let role1 = 'none';
        if(roles.length !== 0) {
            let role1 = roles[0].name;
        }
        let role2 = 'none';
        if (roles.length > 1) {
            role2 = roles[1].name;
        }

        const editBtn = (role1 !== 'ROLE_ADMIN' && role2 !== 'ROLE_ADMIN') ? '' : `
            <button class="btn btn-link btn-sm text-primary btn-rounded" data-detail-edit-id="${id}">Edit</button>
        `

        const deleteBtn = (role1 !== 'ROLE_ADMIN' && role2 !== 'ROLE_ADMIN') ? '' : `
            <button class="btn btn-link btn-sm text-danger btn-rounded" data-detail-id="${id}">Delete</button>
        `
        const row = `
            <div id="detail-${id}" class="my-2">
                <div class="border bg-light rounded p-2">
                    <p class="fw-bold">${firstPage} - ${lastPage}</p>
                    <p>${content}</p>
                    <div class="d-flex justify-content-end">
                        <div class="mx-2">
                            ${editBtn}
                        </div>
                        <div>
                            ${deleteBtn}
                        </div>
                    </div>
                </div>
            </div>
        `;
        out.push(row);
    });
    $('#pageInfo').html(out.join("\n"));
}

function addEdit() {
    $("[data-detail-edit-id]").click(function() {
        const detail_id = $(this).attr("data-detail-edit-id");
        location.href = "/detail/edit/" + detail_id;
    });
}

function addDelete(firstPage, lastPage, book_id) {
    $("[data-detail-id]").click(function() {
        let answer = confirm("Confirm delete");
        if(answer) {
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
        }
    });
}