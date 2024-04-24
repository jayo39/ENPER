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
          ['insert', ['hr', 'link', 'picture', 'video']],
          ['view', ['undo', 'redo']],
        ],
        hint: {
          words: ['→', '★'],
          match: /([>*])$/,
          search: function (keyword, callback) {
            callback($.grep(this.words, function (item) {
              return (keyword === '>' && item === '→') || (keyword === '*' && item === '★');
            }));
          },
          content: function (item) {
            return item;
          }
        }
    });
});