$(function() {
    $('#btnDel').click(function() {
        let answer = confirm("Delete this book?");
        if(answer) {
            $("form[name='frmDelete']").submit();
        }
    });
});