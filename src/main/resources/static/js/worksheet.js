$(function() {
    function updateSelectAll(tableId, checkboxClass, selectAllId) {
        var checkboxes = document.querySelectorAll(`#${tableId} tbody .${checkboxClass}`);
        var allChecked = Array.from(checkboxes).every(checkbox => checkbox.checked);
        document.getElementById(selectAllId).checked = allChecked;
    }

    function addCheckboxListeners(tableId, checkboxClass, selectAllId) {
        var checkboxes = document.querySelectorAll(`#${tableId} tbody .${checkboxClass}`);
        checkboxes.forEach(function(checkbox) {
            checkbox.addEventListener('change', function() {
                updateSelectAll(tableId, checkboxClass, selectAllId);
            });
        });
    }

    document.getElementById('printButton').addEventListener('click', function() {
        window.print();
    });

    addCheckboxListeners('leftTable', 'leftRowCheckbox', 'leftSelectAll');
    addCheckboxListeners('rightTable', 'rightRowCheckbox', 'rightSelectAll');

    document.getElementById('leftSelectAll').addEventListener('change', function() {
        var checkboxes = document.querySelectorAll('#leftTable tbody .leftRowCheckbox');
        for (var checkbox of checkboxes) {
            checkbox.checked = this.checked;
        }
    });

    document.getElementById('rightSelectAll').addEventListener('change', function() {
        var checkboxes = document.querySelectorAll('#rightTable tbody .rightRowCheckbox');
        for (var checkbox of checkboxes) {
            checkbox.checked = this.checked;
        }
    });

    document.getElementById('moveRightBtn').addEventListener('click', function() {
        var checkboxes = document.querySelectorAll('#leftTable tbody .leftRowCheckbox:checked');
        var rightTbody = document.querySelector('#rightTable tbody');
        var leftTbody = document.querySelector('#leftTable tbody');

        var noWorksheetRow = rightTbody.querySelector('.no-worksheet');
        if (noWorksheetRow) {
            rightTbody.removeChild(noWorksheetRow);
        }

        checkboxes.forEach(function(checkbox) {
            var row = checkbox.closest('tr');
            checkbox.checked = false;
            checkbox.classList.remove('leftRowCheckbox');
            checkbox.classList.add('rightRowCheckbox');
            leftTbody.removeChild(row);
            rightTbody.appendChild(row);
        });

        document.getElementById('leftSelectAll').checked = false;
        document.getElementById('rightSelectAll').checked = false;

        addCheckboxListeners('rightTable', 'rightRowCheckbox', 'rightSelectAll');
    });

    document.getElementById('moveLeftBtn').addEventListener('click', function() {
        var checkboxes = Array.from(document.querySelectorAll('#rightTable tbody .rightRowCheckbox:checked'));
        var leftTbody = document.querySelector('#leftTable tbody');
        var rightTbody = document.querySelector('#rightTable tbody');

        checkboxes.sort(function(a, b) {
            var rowA = a.closest('tr');
            var rowB = b.closest('tr');
            var indexA = parseInt(rowA.dataset.originalIndex, 10);
            var indexB = parseInt(rowB.dataset.originalIndex, 10);
            return indexA - indexB;
        });

        checkboxes.forEach(function(checkbox) {
            var row = checkbox.closest('tr');
            checkbox.checked = false;
            checkbox.classList.remove('rightRowCheckbox');
            checkbox.classList.add('leftRowCheckbox');
            rightTbody.removeChild(row);
            leftTbody.appendChild(row);
        });

        if (rightTbody.children.length === 0) {
            var noWorksheetRow = document.createElement('tr');
            noWorksheetRow.classList.add('no-worksheet');
            var td = document.createElement('td');
            td.setAttribute('colspan', '8');
            td.textContent = 'No worksheet have been added.';
            noWorksheetRow.appendChild(td);
            rightTbody.appendChild(noWorksheetRow);
        }

        document.getElementById('leftSelectAll').checked = false;
        document.getElementById('rightSelectAll').checked = false;

        addCheckboxListeners('leftTable', 'leftRowCheckbox', 'leftSelectAll');

        var rowsArray = Array.from(leftTbody.querySelectorAll('tr[data-original-index]'));
        rowsArray.sort(function(a, b) {
            var indexA = parseInt(a.dataset.originalIndex, 10);
            var indexB = parseInt(b.dataset.originalIndex, 10);
            return indexA - indexB;
        });

        leftTbody.innerHTML = '';

        rowsArray.forEach(function(row) {
            leftTbody.appendChild(row);
        });
    });
});
