$('.polished').on('click', function(){
    const $ta  = $(this);
    const text = $ta.val();

    function flashGreen() {
      $ta.addClass('copied');
      setTimeout(() => {
        $ta.removeClass('copied');
      }, 200);
    }

    // modern browsers
    if (navigator.clipboard && navigator.clipboard.writeText) {
      navigator.clipboard.writeText(text)
        .then(() => {
            flashGreen();
        })
        .catch(err => {
          console.error('Failed to copy: ', err);
        });
    }
    else {
      // older browsers
      $ta.select();
      try {
        document.execCommand('copy');
        flashGreen();
      } catch (err) {
        console.error('Fallback copy failed: ', err);
      }
      window.getSelection().removeAllRanges();
    }
});

$('#convert-all').on('click', function() {
  const $btn = $(this);
  const $origRows = $('#original-container .schedule-row');
  const $convRows = $('#converted-container .converted-row');
  let pending = 0;

  // disable the button immediately
  $btn.prop('disabled', true);

  $origRows.each(function(i) {
    const $orig = $(this);
    const $convRow = $convRows.eq(i);
    const id = $convRow.data('schedule-id');
    const studentName = $orig.find('h6').text().trim();
    const writingNote = $orig.find('.stu-note-writing').val().trim();
    const speakingNote = $orig.find('.stu-note-speaking').val().trim();

    const $writingTa = $convRow.find('.polished-writing');
    const $speakingTa = $convRow.find('.polished-speaking');

    function convert(type, note, $ta) {
      const cur = $ta.val().trim();
      if (cur !== 'N/A' && !cur.startsWith('Error')) return;

      if (!note) {
        $ta.val('N/A');
        return;
      }
      // increment counter before firing off the AJAX
      pending++;
      $.post('/api/chat/student-note', { studentName, note, type })
        .done(function(converted) {
          const val = converted.replace(/<name>/gi, studentName);
          $ta.val(val);

          // save to DB if not N/A
          if (val != 'N/A') {
            const data = { schedule_id: id };
            if (type === 'writing')  data.polishedWriting  = val;
            if (type === 'speaking') data.polishedSpeaking = val;
            $.post('/schedule/edit', data);
          }
        })
        .fail(function(xhr) {
          $ta.val(`Error: Quota exceeded.`);
        })
        .always(function() {
          // decrement, and if that was the last call, re-enable
          pending--;
          if (pending === 0) {
            $btn.prop('disabled', false);
          }
        });
    }

    convert('writing',  writingNote,  $writingTa);
    convert('speaking', speakingNote, $speakingTa);
  });

  // edge case: if every note was empty, pending stayed 0
  if (pending === 0) {
    $btn.prop('disabled', false);
  }
});
