$(function() {
   $('.clickable').click(function() {
        var bookId = $(this).children("input").val();
        location.href = "/book/detail/" + bookId;
        th:href="@{'/book/detail/' + ${book.id}}"
   });
});