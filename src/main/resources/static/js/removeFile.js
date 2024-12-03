function removeFile() {
    const newInput = `
          <div id="file-input">
            <input class="form-control col-xs-3" type="file" name="upfile"/>
          </div>
    `
    $('#file-input').replaceWith(newInput);
}