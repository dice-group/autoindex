$('#').click(function() {
    // get the contents of the link that was clicked
    var linkText = $(this).text();

    // replace the contents of the div with the link text
    $('#content-container').html(linkText);

    // cancel the default action of the link by returning false
    return false;
});