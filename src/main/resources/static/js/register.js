const modal = $('#myModal');
const errorField = $("#errorField");

function openModal() {
    modal.css('display', 'flex');
    $("input[name='username']").val('');
    $("input[name='password']").val('');
    $("input[name='re_password']").val('');
}

function closeModal() {
    errorField.text('');
    modal.css('display', 'none');
}

$(window).on('click', function(event) {
    if (event.target === modal[0]) {
        closeModal();
    }
});

$(function() {
    if (localStorage.getItem('registrationSuccess') === 'true') {
        $("#register-success").show();
        localStorage.removeItem('registrationSuccess');
    }
    $(".btn-delete").click(function() {
        let answer = confirm("Delete this user?");
        if(answer) {
            $(this).closest("form").submit();
        }
    });
    $('#title').on('click', function() {
        location.href = window.location.protocol + "//" + window.location.host + "/";
    });
    $('#register').click(function() {
        var username = $("input[name='username']").val().trim();
        var password = $("input[name='password']").val().trim();
        var re_password = $("input[name='re_password']").val().trim();

        if(!username) {
            $("#username").focus();
            return;
        } else if (!password) {
            $("#password").focus();
            return;
        } else if (!re_password) {
            $("#re_password").focus();
            return;
       }

        const data = {
            "username": username,
            "password": password,
            "re_password": re_password,
        };

        $.ajax({
            url: "/user/register",
            type: "POST",
            data: data,
            cache: false,
            success: function(data, status, xhr) {
                localStorage.setItem('registrationSuccess', 'true');
                location.href = "/admin/panel";
            },
            error: function(xhr, status, error) {
                if (xhr.status === 400) {
                    var errorMsg = xhr.responseText;
                    errorField.text(errorMsg);
                }
            }
        });
    });
});