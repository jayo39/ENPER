$(function() {
    $('.form-control').keypress(function(e) {
        if(e.which === 13) {
            e.preventDefault();
            var inputs = $(this).closest('form').find('.form-control');
            inputs.eq(inputs.index(this) + 1).focus();
        }
    });

    $('#btn-finish').click(function() {
        if($('#book').val() === "") {
            $('#book').focus();
            return;
        } else if($('#pageStart').val() === "") {
            $('#pageStart').focus();
            return;
        } else if($('#pageEnd').val() === "") {
            $('#pageEnd').focus();
            return;
        }
        $("form[name='detailAdd']").submit();
    });
});