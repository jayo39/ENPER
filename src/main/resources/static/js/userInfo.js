let userId = '';
let historyData = [];
let currentSortOrder = 'desc';

$(function() {
    const purgeBtn = $('#purgeBtn');
    loadHistory();

    $(document).on('click', '#sort-date-header', function() {
        toggleSortDate();
    });

    purgeBtn.click(function() {
         let answer = confirm("This will purge ALL history!");
         if(answer) {
             $.ajax({
                 url: "/history/clear",
                 type: "POST",
                 cache: false,
                 success: function(data, status, xhr) {
                    $('#select-option').prop('selectedIndex', 0);
                    loadHistory();
                 }
             });
         }
    });
});

function handleUserChange(selectElement) {
    userId = selectElement.value;
    if(userId) {
        loadHistory(userId);
    } else {
        loadHistory();
    }
}

function loadHistory(paramId) {
    let url = "/history/list";
    if(paramId) {
        url += '?user_id=' + paramId;
    }
    $.ajax({
        url: url,
        type: "GET",
        cache: false,
        success: function(data, status, xhr) {
            historyData = data;
            sortAndBuild();
        }
    });
}

function toggleSortDate() {
    currentSortOrder = (currentSortOrder === 'desc') ? 'asc' : 'desc';

    // update sort icon
    const icon = $('#sort-icon');
    if (currentSortOrder === 'asc') {
        icon.attr('class', 'fas fa-sort-up ms-1');
    } else {
        icon.attr('class', 'fas fa-sort-down ms-1');
    }

    sortAndBuild();
}

function sortAndBuild() {
    historyData.sort((a, b) => {
        const dateA = new Date(a.accessDate);
        const dateB = new Date(b.accessDate);

        return currentSortOrder === 'asc' ? dateA - dateB : dateB - dateA;
    });

    buildHistory(historyData);
    deleteHistory();
}

function buildHistory(result) {
    const out = [];
    let row = '';
    if (result.length === 0) {
        row = `
            <tr>
                <td colspan="12">No state history found.</td>
            </tr>
        `;
        out.push(row);
    } else {
        result.forEach(history => {
            let id = history.id;
            let username = history.username;
            let title = history.bookTitle;
            let series = history.bookSeries;
            let accessDate = history.accessDate;

            row = `
                 <tr>
                    <td class="align-middle">${username}</td>
                    <td class="align-middle">${title}</td>
                    <td class="align-middle">${series}</td>
                    <td class="align-middle">${accessDate}</td>
                    <td class="align-middle">
                        <button
                            data-history-id="${id}"
                            class="btn btn-link btn-rounded btn-sm text-danger btn-delete"
                        >
                            Delete
                        </button>
                    </td>
                 </tr>
            `;
            out.push(row);
        });
    }
    $('#info-body').html(out.join("\n"));
}

function deleteHistory() {
    $("[data-history-id]").click(function() {
        const id = $(this).attr("data-history-id");
        $.ajax({
            url: "/history/delete",
            type: "POST",
            cache: false,
            data: {"id": id},
            success: function(data, status, xhr) {
                loadHistory(userId);
            }
        });
    });
}