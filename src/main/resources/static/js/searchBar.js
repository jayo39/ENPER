function checkKeyword() {
    if ($('#searchBar').val().length == 1) {
        $('#searchBtn').prop('disabled', true);
        $('#keywordErr').removeClass('d-none');
    } else {
        $('#searchBtn').prop('disabled', false);
        $('#keywordErr').addClass('d-none');
    }
}


