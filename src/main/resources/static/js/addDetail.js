$(function() {
    $('.form-control').keypress(function(e) {
        if(e.which === 13) {
            e.preventDefault();
            var nextInput = $(this).next().find(".form-control");
            if(nextInput.length !== 0) {
                nextInput.focus();
            }
        }
    });

    $('#btn-finish').click(function() {
        if($('#book').val() === "") {
            $('#book').focus();
            return;
        }
        $("form[name='detailAdd']").submit();
    });
});