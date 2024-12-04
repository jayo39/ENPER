function removeFile() {
    const newInput = `
          <div id="file-input">
            <input id="removeFile" type="hidden" name="removeFile" value="true"/>
            <input class="form-control col-xs-3" type="file" name="upfile" onchange="handleFileChange()"/>
          </div>
    `
    $('#file-input').replaceWith(newInput);
}

function handleFileChange() {
    $('#removeFile').remove();
}