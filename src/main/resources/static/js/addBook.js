$(function() {
    $('.form-control').keypress(function(e) {
        if(e.which === 13) {
            e.preventDefault();
            var nextInput = $(this).closest("div").next().find(".form-control");
            if(nextInput.length !== 0) {
                nextInput.focus();
            }
        }
    });
});