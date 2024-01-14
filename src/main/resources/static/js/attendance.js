const resultList = document.getElementById('resultData');
const title = document.getElementById('resultTitle');
let mainBox = $('.table');
let resultContainer = $('.resultContainer');

function reset() {
  mainBox.addClass('d-none');
  resultContainer.addClass('d-none');
}

function getTop() {
  let unFormattedString = '';
  let oneResultArray = [];
  let resultArray = [];
  let topStudent = [];
  unFormattedString = document.getElementById("inputarea").value;
  unFormattedString = unFormattedString.replace(/\([^)]*\)|[a-zA-Z]/g, '');
  const regex = /([\u3131-\uD79D]+\s[\s\S]+?)(?=[\u3131-\uD79D]+\s|$)/g;

  const formattedStudentArray = [...unFormattedString.matchAll(regex)].map(match => match[0].trim().split(/\s+/));
  const topNum = document.getElementById("select-option").value;

  resultList.innerHTML = '';

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

  if(topNum === 'all') {
     topStudent = resultArray.slice(0);
  } else {
     topStudent = resultArray.slice(0, Number(topNum));
  }

  if(topStudent.length === 0) {
    let t1 = document.createElement("td");
    let t2 = document.createElement("td");
    let row = document.createElement("tr");

    t1.innerHTML = 'None';
    t2.innerHTML = 'None';
    row.append(t1);
    row.append(t2);
  }

  mainBox.removeClass('d-none');
  resultContainer.removeClass('d-none');

  title.innerHTML = `Result - ${topStudent.length}`;
  for(let i = 0; i < topStudent.length; i++) {
      let t1 = document.createElement("td");
      let t2 = document.createElement("td");
      let row = document.createElement("tr");

      t1.innerHTML = `${topStudent[i][0]}`;
      t2.innerHTML = `${Math.round(topStudent[i][1])}`;
      row.append(t1);
      row.append(t2);
      resultList.append(row);
  }
}
