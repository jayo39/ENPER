const resultList = document.getElementById('studentList');
const title = document.getElementById('result-title');

let mainBox = $('.main-box');

function reset() {
    mainBox.addClass('d-none');
    document.getElementById('inputarea').value = '';
}

function getTop() {
  let unFormattedString = '';
  let oneResultArray = [];
  let resultArray = [];
  resultList.innerHTML = '';
  unFormattedString = document.getElementById("inputarea").value;
  unFormattedString = unFormattedString.replace(/\([^)]*\)|[a-zA-Z]/g, '');
  const regex = /([\u3131-\uD79D]+\s[\s\S]+?)(?=[\u3131-\uD79D]+\s|$)/g;

  const formattedStudentArray = [...unFormattedString.matchAll(regex)].map(match => match[0].trim().split(/\s+/));

  for(let i = 0; i < formattedStudentArray.length; i++) {
    let attendCount = 0;
    const isTimeStamp = (str) => /^\d{2}:\d{2}$/.test(str);
    const isKoreanName = (str) => /[\u3131-\uD79D]+/.test(str);

    const filteredArray = formattedStudentArray[i].filter(item => isKoreanName(item) || isTimeStamp(item));
    formattedStudentArray[i] = filteredArray;
    attendCount = (formattedStudentArray[i].length - 1) / 3;
    oneResultArray = [formattedStudentArray[i][0], attendCount];
    resultArray = [...resultArray, oneResultArray];
  }

  resultArray.sort((a, b) => parseInt(b[1], 10) - parseInt(a[1], 10));
  const topThirty = resultArray.slice(0, 30);

  if(topThirty.length === 0) {
    resultList.innerHTML = 'None';
  }

  mainBox.removeClass('d-none');
  title.innerHTML = `Result - ${topThirty.length}`;

  for(let i = 0; i < topThirty.length; i++) {
    resultList.innerHTML += `<div>${topThirty[i].join(' ')}</div>`;
  }
}
