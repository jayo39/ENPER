let mainBox = $('.main-box');
let enroll = $('#enroll');

function reset() {
    mainBox.addClass('d-none');
    document.getElementById('tomorrow').value = '';
}

function getResult() {
    let tomorrowInput = $('#tomorrow').val();

    let cleanedTomorrow = processInput(tomorrowInput);

    let enrollList = cleanedTomorrow;

    mainBox.removeClass('d-none');

    if (enrollList.length === 0) {
        enroll.text('None');
    } else {
        enroll.text(enrollList.join(', '));
    }

}

function processInput(input) {
    input = input.replace(/\([^)]*\)/g, '');

    let array = input.split(/[\/,\n]/);

    array = array.map(item => item.replace(/^\d+/, '').trim()).filter(item => item !== '');

    array = [...new Set(array)];

    return array;
 }