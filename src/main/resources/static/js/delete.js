$(function() {
    $('#btnDel').click(function() {
        let answer = confirm("Delete this book?");
        if(answer) {
            $("form[name='frmDelete']").submit();
        }
    });

    $('#btnDelQuestion').click(function() {
        let answer = confirm("Delete this content?");
        if(answer) {
            $("form[name='questionDelete']").submit();
        }
    });
});