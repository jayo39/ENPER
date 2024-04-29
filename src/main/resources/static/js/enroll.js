let mainBox = $('.main-box');
let unenroll = $('#unenroll');
let enroll = $('#enroll');

function reset() {
    mainBox.addClass('d-none');
    document.getElementById('yesterday').value = '';
    document.getElementById('today').value = '';
    document.getElementById('tomorrow').value = '';
}

function getResult() {
    let yesterdayInput = $('#yesterday').val();
    let todayInput = $('#today').val();
    let tomorrowInput = $('#tomorrow').val();

    let cleanedYesterday = processInput(yesterdayInput);
    let cleanedToday = processInput(todayInput);
    let cleanedTomorrow = processInput(tomorrowInput);

    let unenrollList = getUnenroll(cleanedYesterday, cleanedToday, cleanedTomorrow);
    let enrollList = getEnroll(cleanedYesterday, cleanedToday, cleanedTomorrow);

    mainBox.removeClass('d-none');

    if(unenrollList.length === 0) {
        unenroll.text('None');
    } else {
        unenroll.text(unenrollList);
    }

    if (enrollList.length === 0) {
        enroll.text('None');
    } else {
        enroll.text(enrollList);
    }

}

function processInput(input) {
    input = input.replace(/\([^)]*\)/g, '');

    let array = input.split(/[\/,\n]/);

    array = array.map(item => item.replace(/^\d+/, '').trim()).filter(item => item !== '');

    return array;
  }

function getUnenroll(yesterdayArray, todayArray, tomorrowArray) {
    const combinedSet = new Set([...yesterdayArray, ...todayArray]);

    const newArray = Array.from(combinedSet).filter(item => !tomorrowArray.includes(item));

    return newArray;
}

function getEnroll(yesterdayArray, todayArray, tomorrowArray) {
    return tomorrowArray;
}