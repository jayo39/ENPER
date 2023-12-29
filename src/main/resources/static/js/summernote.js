$(function() {
    $('#content').summernote({
        height: 400,
        toolbar: [
          ['style', ['style']],
          ['font', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
          ['color', ['color']],
          ['para', ['ul', 'ol', 'paragraph']],
          ['table', ['table']],
          ['fontname', ['fontname']],
          ['fontsize', ['fontsize']],
          ['insert', ['link', 'picture', 'video']],
          ['view', ['undo', 'redo']],
        ],
        hint: {
          words: ['→'],
          match: /\B>(\w*)$/,
          search: function (keyword, callback) {
            callback($.grep(this.words, function (item) {
              return item.indexOf(keyword) == 0;
            }));
          },
          content: function () {
            return '→';
          }
        }
    });
});