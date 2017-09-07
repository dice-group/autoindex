function progressbarmove() {
  var elem = document.getElementById("myBar");   
  var width = 0.5;
  var id = setInterval(frame, 100);
  function frame() {
    if (width >= 100) {
      clearInterval(id);
    } else {
      width++; 
      elem.style.width = width + '%'; 
    }
  }
}