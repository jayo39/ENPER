$(function() {
    const sidebar = $('#sidebar');
    const openSidebar = $('#schedule');
    const closeSidebar = $('#closeSidebar');
    const clearBtn = $('#clear-all');
    const body = $('body');
    let isOpen = false;
    let bodyWidth = body.width();

    $("#sidebar").removeClass('d-none');

    if(localStorage.getItem('barStatus') === 'open') {
        sidebar.css('transition', 'none');
        body.css('transition', 'none');
        openBar();

        setTimeout(function() {
                sidebar.css('transition', '0.3s');
                body.css('transition', '0.3s');
        }, 10);
    }

    function openBar() {
        let screenWidth = $(window).width();
        isOpen = true;
        loadSchedule();
        sidebar.css('right', '0');
        $('#schedule').addClass('nav-active');
        if (screenWidth > 980) {
            body.css('padding-right', '300px');
        }
    }

    $(window).resize(function() {
        if(isOpen) {
            let screenWidth = $(window).width();
            if (screenWidth <= 980) {
                body.css('padding-right', '0px');
            } else if (screenWidth > 980) {
                body.css('padding-right', '300px');
            }
        }
    });

    openSidebar.click(function() {
        localStorage.setItem('barStatus', 'open');
        openBar();
    });

    closeSidebar.click(function() {
        isOpen = false;
        localStorage.removeItem('barStatus');
        sidebar.css('right', '-300px');
        $('#schedule').removeClass('nav-active');
        body.css('padding-right', '0px');
    });

    clearBtn.click(function() {
        $.ajax({
            url: "/schedule/clear",
            type: "POST",
            cache: false,
            success: function(data, status, xhr) {
                loadSchedule();
            }
        });
    });

    $("#add-btn").click(function() {
        const studentName = $('#schedule-studentName').val().trim();
        const endTime = $('#schedule-endTime-initial').val().trim();
        if(!studentName) {
            $('#schedule-studentName').focus();
            return;
        } else if (!endTime) {
            $('#schedule-endTime-initial').focus();
            return;
        }
        const data = {
            "studentName": studentName,
            "endTime": endTime
        };
        $.ajax({
            url: "/schedule/add",
            type: "POST",
            data: data,
            cache: false,
            success: function(data, status, xhr) {
                loadSchedule();
            }
        });
        $('#schedule-studentName').val('');
        $('#schedule-endTime-initial').val('');
    });

    $('#schedule-studentName').on("input", function() {
        var maxLength = 8;
        if ($(this).val().length > maxLength) {
          $(this).val($(this).val().substring(0, maxLength));
        }
    });
});

function loadSchedule() {
    $.ajax({
        url: "/schedule/list?user_id=" + scheUser_id,
        type: "GET",
        cache: false,
        success: function(data, status, xhr) {
            buildSchedule(data);
            listenDelete();
            listenEdit();
        }
    });
}

function buildSchedule(result) {
    if (result.length === 0) {
        $("#schedule-title").text("Checkup - " + 0);
    } else {
        $("#schedule-title").text("Checkup - " + result.length);
    }

    const out = [];
    result.forEach(schedule => {
        let id = schedule.id;
        let studentName = schedule.studentName;
        let endTime = schedule.endTime;
        let content = schedule.note === null ? "" : schedule.note;
        let isFinished = schedule.isFinished;
        const row = `
            <div id="schedule-${id}">
                <div id="schedule-info-${id}" class="d-flex justify-content-between align-items-center text-yellow input-group">
                    <div class="mb-1 me-1">
                      <div data-schedule-id-name="${id}" class="studentName">${studentName}</div>
                    </div>
                    <div class="mb-1 ms-1">
                      <input data-schedule-id-time="${id}" type="time" class="form-control" id="schedule-endTime" placeholder="" value="${endTime}">
                    </div>
                </div>
            </div>
            <textarea data-schedule-id-note="${id}" class="form-control my-1 stu-note" placeholder="Note..">${content}</textarea>
            <div class="d-flex justify-content-between mb-3">
                <div class="form-check me-2">
                  <span class="text-light">Checkup</span>
                  <input id="check-${id}" data-schedule-id-check="${id}" class="form-check-input" type="checkbox" value="">
                </div>
                <div class="form-check form-switch">
                  <input id="flexSwitchCheckDefault" data-schedule-id="${id}" class="form-check-input bg-secondary" type="checkbox" role="switch">
                </div>
            </div>
            <hr class="bg-light border">
        `;
        out.push(row);
    });
    $('#schedule-list').html(out.join("\n"));
    result.forEach(schedule => {
        let id = schedule.id;
        let isFinished = schedule.isFinished;
        if (isFinished == 1) {
            $("#schedule-info-" + id).removeClass('text-yellow');
            $("#schedule-info-" + id).addClass('text-gray');
            $("#check-" + id).prop("checked", true);
        } else if (isFinished == 0) {
            $("#schedule-info-" + id).removeClass('text-gray');
            $("#schedule-info-" + id).addClass('text-yellow');
            $("#check-" + id).prop("checked", false);
        }
    });
    $('.stu-note').on("input", function() {
        this.style.height = "auto";
        this.style.height = (this.scrollHeight) + "px";
    });
    $('.stu-note').trigger("input");
}

function listenEdit() {
    $("[data-schedule-id-time]").on('change', function() {
        const schedule_id = $(this).attr("data-schedule-id-time");
        const time = $(this).val();
        const data = {
            "schedule_id": schedule_id,
            "time": time
        };
        $.ajax({
            url: "/schedule/edit",
            type: "POST",
            cache: false,
            data: data,
            success: function(data, status, xhr) {
                loadSchedule();
            }
        });
    });
    $("[data-schedule-id-note]").on('change', function() {
        const schedule_id = $(this).attr("data-schedule-id-note");
        const content = $(this).val();
        const data = {
            "schedule_id": schedule_id,
            "content": content
        };
        $.ajax({
            url: "/schedule/edit",
            type: "POST",
            cache: false,
            data: data,
            success: function(data, status, xhr) {
                loadSchedule();
            }
        });
    });
}

function listenDelete(result) {
    $("[data-schedule-id-check]").click(function() {
        const schedule_id = $(this).attr("data-schedule-id-check");
        if($(this).prop("checked")) {
            $("#schedule-info-" + schedule_id).removeClass('text-yellow');
            $("#schedule-info-" + schedule_id).addClass('text-gray');
            const data = {
                "schedule_id": schedule_id,
                "isFinished": true
            };
            $.ajax({
                url: "/schedule/edit",
                type: "POST",
                cache: false,
                data: data,
                success: function(data, status, xhr) {
                }
            });
        } else if (!($(this).prop("checked"))) {
            $("#schedule-info-" + schedule_id).removeClass('text-gray');
            $("#schedule-info-" + schedule_id).addClass('text-yellow');
            const data = {
                "schedule_id": schedule_id,
                "isFinished": false
            };
            $.ajax({
                url: "/schedule/edit",
                type: "POST",
                cache: false,
                data: data,
                success: function(data, status, xhr) {
                }
            });
        }
    });
    $("[data-schedule-id]").click(function() {
        const schedule_id = $(this).attr("data-schedule-id");
        $.ajax({
            url: "/schedule/delete",
            type: "POST",
            cache: false,
            data: {"id": schedule_id},
            success: function(data, status, xhr) {
                loadSchedule();
            }
        });
    });
}

$(document).on('click', '.studentName', function() {
    const schedule_id = $(this).attr("data-schedule-id-name");
    var currentText = $(this).text();
    var input = $('<input>', {
        val: currentText,
        type: 'text',
        'class': 'form-control studentNameInput',
        'data-schedule-id-name-input': schedule_id,
        blur: function() {
            var newText = $(this).val() || currentText;
            var newDiv = $('<div>', {
                text: newText,
                'class': 'studentName',
                'data-schedule-id-name': schedule_id
            });

            $(this).replaceWith(newDiv);
        },
        keyup: function(e) {
            if (e.key === 'Enter') {
                $(this).blur();
            }
        }
    });

    $(this).replaceWith(input);
    input.focus();
});
$(document).on('blur', '[data-schedule-id-name-input]', function() {
    const schedule_id = $(this).attr("data-schedule-id-name-input");
    const name = $(this).val();

    if (name !== '') {
        const data = {
            "schedule_id": schedule_id,
            "name": name
        };

        $.ajax({
            url: "/schedule/edit",
            type: "POST",
            cache: false,
            data: data,
            success: function(data, status, xhr) {
                loadSchedule();
            }
        });
    }
});