$(function() {
   $('.clickable').click(function() {
        var bookId = $(this).children("input").val();
        $.ajax({
            url: "/history/add?book_id=" + bookId,
            type: "POST",
            cache: false
        });
   });
});