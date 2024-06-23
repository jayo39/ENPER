$(document).ready(function() {
    let isComposing = false;

    $('.worksheet-form').on('compositionstart', function() {
        isComposing = true;
    });

    $('.worksheet-form').on('compositionend', function() {
        isComposing = false;
    });

    $('.worksheet-form').keydown(function(e) {
        if (e.key === "Enter" && !isComposing) {
            e.preventDefault();
            let currentIndex = $('.worksheet-form').index(this);
            let nextInput = $('.worksheet-form').eq(currentIndex + 1);
            if (nextInput.length) {
                nextInput.focus();
            }
        }
    });
});

function checkWord(event) {
    const regex = /^[a-zA-Z]+$/;
    if (!regex.test(event.key) && event.key !== 'Backspace' && event.key !== 'Delete' && event.key !== 'ArrowLeft' && event.key !== 'ArrowRight') {
        event.preventDefault();
    }
}

function scrambleSentence(sentence) {
    let words = sentence.split(' ');
    let n = words.length;
    while (n) {
        let i = Math.floor(Math.random() * n--);
        let temp = words[n];
        words[n] = words[i];
        words[i] = temp;
    }
    return words.join(' / ');
}

function scrambleSentences() {
    $('.scramble-form').each(function(index) {
        let input = $(this).val();
        let scrambled = scrambleSentence(input);
        $('.scramble').eq(index).text(scrambled);
    });
}

function generateString(word) {
    let alphabet = 'abcdefghijklmnopqrstuvwxyz';
    let randomLetters = Array.from({length: 19}, () => alphabet[Math.floor(Math.random() * alphabet.length)]);
    let randomPosition = Math.floor(Math.random() * (20 - word.length));

    for (let i = 0; i < word.length; i++) {
        randomLetters[randomPosition + i] = word[i];
    }

    return randomLetters;
}

function printDocument() {
    let foundEmpty = false;
    $('.required').each(function() {
        if ($(this).val().trim() === '') {
            $(this).focus();
            foundEmpty = true;
            return false;
        }
    });
    if(!foundEmpty) {
        scrambleSentences();
        let count = $('[id^="w-"]').length; // ensure this is the correct way to count the elements
        for (let i = 1; i <= count; i++) {
            let wordId = '#w-' + i;
            let meaningId = '#m-' + i;
            let word = $(wordId).val();
            let meaning = $(meaningId).val();

            let scrambledLetters = generateString(word);
            let scrambleDivId = '#scrable-' + i;
            let scrambleDiv = $(scrambleDivId);

            scrambleDiv.empty();

            scrambledLetters.forEach(letter => {
                let span = $('<span>').text(letter);
                scrambleDiv.append(span);
            });

            let content = '<b>' + word + '</b>';
            if (meaning) {
                content += '<span class="mx-3">-</span>' + meaning;
            } else {
                content += '<span class="mx-3">-</span>____________________';
            }
            $('#word-' + i).html(content);
        }
        $('.page-text-footer').text($('#book-title').val());
        window.scrollTo(0, 0);
        window.print();
    }

}