$(function() {
    const sidebar = $('#sidebar');
    const openSidebar = $('#schedule');
    const closeSidebar = $('#closeSidebar');
    const clearBtn = $('#clear-all');

    openSidebar.click(function() {
        loadSchedule();
        $('')
        sidebar.css('right', '0');
    });

    closeSidebar.click(function() {
        sidebar.css('right', '-300px');
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
    if (result.length == 0) {
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
                <div id="schedule-info-${id}" class="d-flex justify-content-between align-items-center text-warning">
                    <div class="h5">
                      <div>${studentName}</div>
                    </div>
                    <div class="mb-1">
                      <input data-schedule-id-time="${id}" type="time" class="form-control" id="schedule-endTime" placeholder="" value="${endTime}">
                    </div>
                </div>
            </div>
            <textarea data-schedule-id-note="${id}" class="form-control my-1 note" placeholder="Note..">${content}</textarea>
            <div class="d-flex justify-content-between mb-3">
                <div class="form-check me-2">
                  <input id="check-${id}" data-schedule-id-check="${id}" class="form-check-input" type="checkbox" value="">
                  <span class="text-light">Checkup</span>
                </div>
                <div class="form-check form-switch">
                    <input data-schedule-id="${id}" class="form-check-input" type="checkbox">
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
            $("#schedule-info-" + id).removeClass('text-warning');
            $("#schedule-info-" + id).addClass('text-secondary');
            $("#check-" + id).prop("checked", true);
        } else if (isFinished == 0) {
            $("#schedule-info-" + id).removeClass('text-secondary');
            $("#schedule-info-" + id).addClass('text-warning');
            $("#check-" + id).prop("checked", false);
        }
    });
    $('.note').on("input", function() {
        this.style.height = "auto";
        this.style.height = (this.scrollHeight) + "px";
    });
    $('.note').trigger("input");
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
            $("#schedule-info-" + schedule_id).removeClass('text-warning');
            $("#schedule-info-" + schedule_id).addClass('text-secondary');
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
            $("#schedule-info-" + schedule_id).removeClass('text-secondary');
            $("#schedule-info-" + schedule_id).addClass('text-warning');
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