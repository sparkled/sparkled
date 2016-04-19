   var i = 0;
   setInterval(function(){
       getLed(i).css('fill', '#f0f');
       getLed(i).css('r', '6');
       getLed(i).css('stroke-width', '0');

       var previous = i - 1;
       if (i == 0) {
           previous = 349;
       }

       getLed(previous).css('fill', ''); 
       getLed(previous).css('r', '');
       getLed(previous).css('stroke-width', '');

       i++;

      if (i == 350) {
          i = 0;
      }
   }, 25);

var getLed = function (ledNum) {
  return $('circle[data-led=' + ledNum + ']')
}