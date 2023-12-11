$(function() {
    $('.form-control').keypress(function(e) {
        if(e.which === 13) {
            e.preventDefault();
            var nextInputDiv = $(this).closest('.form-outline').nextAll('.form-outline').first();
            var nextInput = nextInputDiv.find('.form-control');
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