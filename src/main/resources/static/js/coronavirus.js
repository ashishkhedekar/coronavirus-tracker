$(document).ready(function(){
    $("#searchCountryInput").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#countriesStatus tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});
