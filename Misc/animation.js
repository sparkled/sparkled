   var i = 0;
   setInterval(function(){
       $("#led" + i).attr("fill", "#f0f");
       $("#led" + i).attr("stroke", "#f0f");
       $("#led" + i).attr("stroke-width", "8px");

       var previous = i - 1;
       if (i == 0) {
           previous = 349;
       }

       $("#led" + previous).attr("fill", "none"); 
       $("#led" + previous).attr("stroke", "#000");
       $("#led" + previous).attr("stroke-width", "1px");

       i++;

      if (i == 350) {
          i = 0;
      }
   }, 25);