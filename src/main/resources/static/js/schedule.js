const clockmodal = $('#clock-modal');
const hourButton = document.querySelector('.timepicker-hour');
const minuteButton = document.querySelector('.timepicker-minute');
const clockFaceHour = document.querySelector('#hour .timepicker-clock');
const clockFaceMinute = document.querySelector('#minute .timepicker-clock');
const timepickerHandHour = document.querySelector('#handHour');
const timepickerHandMin = document.querySelector('#handMin');
const hourMarkers = document.querySelectorAll('.timepicker-time-tips-hours');
const minuteMarkers = document.querySelectorAll('.timepicker-time-tips-minutes');
const hourView = document.querySelector('#hour');
const minuteView = document.querySelector('#minute');
let isDragging = false;
let mode = 'hour';
let lastClickedTimeInput = null;
let savehour = null;
let saveminute = null;

$(function() {
    const sidebar = $('#sidebar');
    const openSidebar = $('#schedule');
    const closeSidebar = $('#closeSidebar');
    const clearBtn = $('#clear-all');
    const body = $('body');
    let isOpen = false;
    let bodyWidth = body.width();

    $("#sidebar").removeClass('d-none');

    clockFaceHour.addEventListener('mousedown', handleStart);
    clockFaceMinute.addEventListener('mousedown', handleStart);
    document.addEventListener('mousemove', handleMove);
    document.addEventListener('mouseup', handleEnd);

    clockFaceHour.addEventListener('touchstart', handleStart);
    clockFaceMinute.addEventListener('touchstart', handleStart);
    document.addEventListener('touchmove', handleMove, { passive: false });
    document.addEventListener('touchend', handleEnd);

    if(!(typeof scheUser_id === 'undefined') && localStorage.getItem('barStatus') === 'open') {
        sidebar.css('transition', 'none');
        body.css('transition', 'none');
        openBar();

        setTimeout(function() {
            sidebar.css('transition', '0.2s');
            body.css('transition', '0.2s');
        }, 10);
    }

    function openBar() {
        let screenWidth = $(window).width();
        isOpen = true;
        loadSchedule();
        sidebar.css('right', '0');
        $('#schedule').addClass('header-active');
        if (screenWidth > 980) {
            body.css('padding-right', '260px');
        }
    }

    $(window).resize(function() {
        if(isOpen) {
            let screenWidth = $(window).width();
            if (screenWidth <= 980) {
                body.css('padding-right', '0px');
            } else if (screenWidth > 980) {
                body.css('padding-right', '260px');
            }
        }
    });

    sidebar.click(function() {
        checkSession();
    });

    openSidebar.click(function() {
        checkSession(function() {
            localStorage.setItem('barStatus', 'open');
            openBar();
        });
    });

    closeSidebar.click(function() {
        isOpen = false;
        localStorage.removeItem('barStatus');
        sidebar.css('right', '-260px');
        $('#schedule').removeClass('header-active');
        body.css('padding-right', '0px');
    });

    clearBtn.click(function() {
        let answer = confirm("This will clear your schedule!");
        if(answer) {
            $.ajax({
                url: "/schedule/clear",
                type: "POST",
                cache: false,
                success: function(data, status, xhr) {
                    loadSchedule();
                }
            });
        }
    });

    $("#add-btn").click(function() {
        const studentName = $('#schedule-studentName').val().trim();
        const endTime = $('#schedule-endTime-initial').val().trim();
        if(!studentName) {
            return;
        } else if (!endTime) {
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
        lastClickedTimeInput.getElementsByClassName("display-time")[0].textContent = '';
        lastClickedTimeInput = null;
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
        let [hours, minutes] = endTime.split(':').map(num => parseInt(num, 10));

        let ampm = hours >= 12 ? 'PM' : 'AM';
        hours = hours % 12;
        hours = hours ? hours : 12;

        let newTime = hours.toString().padStart(2, '0') + ':' + minutes.toString().padStart(2, '0') + ' ' + ampm;

        const row = `
            <div id="schedule-${id}">
                <div id="schedule-info-${id}" class="d-flex justify-content-between align-items-center text-yellow input-group">
                    <div class="mb-1 me-1">
                      <div data-schedule-id-name="${id}" class="studentName">${studentName}</div>
                    </div>
                    <div class="mb-1 ms-1">
                      <div class="top-input form-control time-input d-flex justify-content-end align-items-center column-gap-3">
                        <div class="display-time">${newTime}</div>
                        <div>
                          <i class="far fa-clock"></i>
                        </div>
                      </div>
                      <input data-schedule-id-time="${id}" type="time" class="form-control" id="schedule-endTime-${id}" value="" hidden>
                    </div>
                </div>
            </div>
            <textarea data-schedule-id-note="${id}" class="form-control my-1 stu-note" placeholder="Note..">${content}</textarea>
            <div class="d-flex justify-content-between">
                <div class="form-check me-2">
                  <span>Mark Complete</span>
                  <input id="check-${id}" data-schedule-id-check="${id}" class="form-check-input" type="checkbox" value="">
                </div>
                <div class="form-check form-switch">
                  <input data-schedule-id="${id}" class="form-check-input bg-secondary" type="checkbox" role="switch">
                </div>
            </div>
            <hr class="border border-secondary">
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
            data: data
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
                data: data
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
        'class': 'form-control studentNameInput name-input top-input',
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
$(document).on('change', '[data-schedule-id-name-input]', function() {
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
            data: data
        });
    }
});

$(document).on('input', '.name-input', function() {
    if (this.value.length > 8) {
        this.value = this.value.slice(0, 8);
    }
});

function checkSession(successCallback) {
    $.ajax({
        url: '/checkSession',
        type: 'GET',
        success: function(response) {
            if (typeof successCallback === 'function') {
                successCallback();
            }
        },
        error: function(xhr, status, error) {
            alert("Session expired.");
            location.href = '/user/login';
        }
    });
}

function openClockModal() {
    clockmodal.removeClass('d-none');
    $('.timepicker-hour').addClass('active');
    $('.timepicker-minute').removeClass('active');
}

function closeClockModal() {
    hour_switch = false;
    minute_switch = false;
    clockmodal.addClass('d-none');
}

$(document).on('click', '.time-input', function() {
    lastClickedTimeInput = this;
    mode = 'hour';
    if (lastClickedTimeInput.getElementsByClassName("display-time")[0].textContent) {
        let [time, period] = lastClickedTimeInput.getElementsByClassName("display-time")[0].textContent.split(' ');
        [savehour, saveminute] = time.split(':');

        hourButton.textContent = savehour;
        minuteButton.textContent = saveminute;

        savehour = parseInt(savehour);
        saveminute = parseInt(saveminute);

        updateActiveMarker(Math.floor(savehour % 12), 'hour');
        if (period === 'AM') {
            $('.timepicker-am').addClass('active');
            $('.timepicker-pm').removeClass('active');
        } else {
            $('.timepicker-pm').addClass('active');
            $('.timepicker-am').removeClass('active');
        }

        hourView.classList.remove('d-none');
        minuteView.classList.add('d-none');
        const hourDegrees = ((savehour % 12) / 12) * 360;
        setHandRotation(hourDegrees, 'hour');
    } else {
        resetClock();
    }
    openClockModal();
});

$(document).on('click', '.timepicker-cancel', function() {
    closeClockModal();
});

$(document).on('click', '.timepicker-clear', function() {
    resetClock();
});

$(document).on('click', '.timepicker-am', function() {
    document.querySelector('.timepicker-am').classList.add('active');
    document.querySelector('.timepicker-pm').classList.remove('active');
});

$(document).on('click', '.timepicker-pm', function() {
    document.querySelector('.timepicker-pm').classList.add('active');
    document.querySelector('.timepicker-am').classList.remove('active');
});

$(document).on('click', '.timepicker-hour', function() {
    hour_switch = true;
    toggleToHourSelection();
});

$(document).on('click', '.timepicker-minute', function() {
    minute_switch = true;
    toggleToMinuteSelection();
});

$(document).on('click', '.timepicker-button.timepicker-submit', function() {
    const newhour = hourButton.textContent;
    const newminute = minuteButton.textContent;
    const amPm = document.querySelector('.timepicker-hour-mode.active').textContent;

    lastClickedTimeInput.getElementsByClassName("display-time")[0].textContent = `${newhour}:${newminute} ${amPm}`;

    const timeForDatabase = convertTo24HourFormat(parseInt(newhour, 10), newminute, amPm);
    if (lastClickedTimeInput.getAttribute('id') === "initial") {
        document.querySelector('#schedule-endTime-initial').value = timeForDatabase;
    } else {
        lastClickedTimeInput.nextSibling.nextSibling.value = timeForDatabase;
        let event = new Event('change', {
            'bubbles': true,
            'cancelable': true
        });
        lastClickedTimeInput.nextSibling.nextSibling.dispatchEvent(event);
    }
    closeClockModal();
});


function toggleToMinuteSelection() {
    saveminute = minuteButton.textContent;
    $('#hour').addClass('d-none');
    $('#minute').removeClass('d-none');
    $('.timepicker-hour').removeClass('active');
    $('.timepicker-minute').addClass('active');
    mode = 'minute';
    if (saveminute) {
        const minuteDegrees = saveminute * 6;
        updateActiveMarker(saveminute / 5, 'minute');
        setHandRotation(minuteDegrees, 'minute');
    } else {
        updateActiveMarker(0, 'minute');
        setHandRotation(0, 'minute');
    }
}

function toggleToHourSelection() {
    savehour = hourButton.textContent;
    $('#minute').addClass('d-none');
    $('#hour').removeClass('d-none');
    $('.timepicker-minute').removeClass('active');
    $('.timepicker-hour').addClass('active');
    mode = 'hour';
    if (savehour) {
        updateActiveMarker(Math.floor(savehour % 12), 'hour');
        const hourDegrees = ((savehour % 12) / 12) * 360;
        setHandRotation(hourDegrees, 'hour');
    } else {
        updateActiveMarker(0, 'hour');
        setHandRotation(360, 'hour');
    }
}

function snapToNearest(degrees, mode) {
    return mode === 'hour' ? Math.round(degrees / 30) * 30 : Math.round(degrees / 6) * 6;
}

function updateActiveMarker(index, mode) {
    const markers = mode === 'hour' ? hourMarkers : minuteMarkers;
    markers.forEach((marker, i) => {
        marker.classList.remove('active');
        if (i === index) {
            marker.classList.add('active');
        }
    });
}

function calculateRotation(e) {
    const target = mode === 'hour' ? clockFaceHour : clockFaceMinute;
    const rect = target.getBoundingClientRect();
    const centerX = rect.left + rect.width / 2;
    const centerY = rect.top + rect.height / 2;
    let x = e.clientX;
    let y = e.clientY;

    if (e.touches) {
        x = e.touches[0].clientX;
        y = e.touches[0].clientY;
    }

    const angleDeg = Math.atan2(y - centerY, x - centerX) * 180 / Math.PI + 90;
    return angleDeg < 0 ? angleDeg + 360 : angleDeg;
}

function updateHandAndDisplay(angle) {
    const snappedAngle = snapToNearest(angle, mode);
    setHandRotation(snappedAngle, mode);
    if (mode === 'hour') {
        const hour = snappedAngle / 30 || 12;
        hourButton.textContent = hour < 10 ? `0${hour}` : hour;
        updateActiveMarker(Math.floor(hour % 12), 'hour');
    } else {
        const minute = snappedAngle / 6 % 60;
        minuteButton.textContent = minute < 10 ? `0${minute}` : minute;
        updateActiveMarker(minute / 5, 'minute');
    }
}

function setHandRotation(degrees, mode) {
    if (mode === 'hour') {
        timepickerHandHour.style.transform = `rotateZ(${degrees}deg)`;
    } else {
        timepickerHandMin.style.transform = `rotateZ(${degrees}deg)`;
    }
}

function handleStart(e) {
    e.preventDefault();
    isDragging = true;
    const angle = calculateRotation(e);
    updateHandAndDisplay(angle);
}

function handleMove(e) {
    if (!isDragging) return;
    const angle = calculateRotation(e);
    updateHandAndDisplay(angle);
}

function handleEnd() {
    if (!isDragging) return;
    isDragging = false;
    if (mode === 'hour') toggleToMinuteSelection();
}

function convertTo24HourFormat(hour, minute, amPm) {
    let hourIn24 = hour % 12;
    if (amPm === 'PM') {
        hourIn24 += 12;
    }
    return `${hourIn24.toString().padStart(2, '0')}:${minute.padStart(2, '0')}:00`;
}

function resetClock() {
    hourButton.textContent = '12';
    minuteButton.textContent = '00';
    saveminute = null;
    document.querySelector('.timepicker-am').classList.remove('active');
    document.querySelector('.timepicker-pm').classList.add('active');

    setHandRotation(360, 'hour');
    setHandRotation(0, 'minute');

    $('#hour').removeClass('d-none');
    $('#minute').addClass('d-none');
    mode = 'hour';

    updateActiveMarker(0, 'hour');
    updateActiveMarker(0, 'minute');
}