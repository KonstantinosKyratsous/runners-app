function chooseUserType(tab) {
    const nav_tabs = document.getElementsByClassName('nav-link');

    Array.prototype.forEach.call(nav_tabs, function(nav_tab) {
        nav_tab.className = 'nav-link'

        if (nav_tab === tab) {
            nav_tab.className = 'nav-link active'
        }
    });

    const nav_tabContents = document.getElementsByClassName('tab-pane');

    Array.prototype.forEach.call(nav_tabContents, function (contents) {
        contents.className = 'tab-pane fade'

        if (tab.id === 'nav-athlete-tab') {
            document.getElementById('nav-athlete').className = 'tab-pane fade show active'
        }
        else if (tab.id === 'nav-coach-tab') {
            document.getElementById('nav-coach').className = 'tab-pane fade show active'
        }
        else if (tab.id === 'nav-organizer-tab') {
            document.getElementById('nav-organizer').className = 'tab-pane fade show active'
        }
        else if (tab.id === 'nav-nutritionist-tab') {
            document.getElementById('nav-nutritionist').className = 'tab-pane fade show active'
        }
    });

}

// Code for races filter - contains the location that there is no more usable
let clickedDate = false;
let startDate = "";
let endDate = "";

let locations = getUrlParameter("location").split(",");
let dateRange = getUrlParameter("date-range");

$('input[name="location"]').map(function () {
    if (locations.includes(this.value)) {
        this.checked = true;
    }
});

if (dateRange.length > 0)
    $('#date-range').val(dateRange);

$(function () {
    $('input[name="date-range"]').daterangepicker({
        opens: 'left'
    }, function (start, end, label) {
        clickedDate = true;
        startDate = start.format("MM/DD/YYYY");
        endDate = end.format("MM/DD/YYYY");
        ;
    });
});

$("#submit-button").click(function () {
    let finalUrl = "/races?";

    $(".location").map(function () {
        if ($(this).is(":checked"))
            finalUrl += finalUrl.includes("location") ? "," + this.value : "location=" + this.value;
    });

    if (finalUrl.includes("location") && clickedDate)
        finalUrl += "&";

    if (clickedDate)
        finalUrl += "date-range=" + startDate + " - " + endDate;

    window.location.href = finalUrl;
});

// Filter location results
function filterLocationResults() {
    let checkboxes = document.getElementsByClassName("checkbox");
    let results = document.getElementsByClassName("result");
    let locations = [];

    for (const filter of checkboxes) {
        if (filter.checked === true)
            locations.push(filter.value);
    }

    let isEmptyLocations = (locations.length === 0);

    for (const result of results) {
        let location = result.getElementsByClassName("race-location")[0];

        result.hidden = (!locations.includes(location.innerText));

        if (isEmptyLocations)
            result.hidden = false;
    }
}

// Filter product results
function filterProductResults() {
    let checkboxes = document.getElementsByClassName("checkbox");
    let results = document.getElementsByClassName("result");
    let types = [];

    for (const filter of checkboxes) {
        if (filter.checked === true) {
            types.push(filter.value);
        }
    }

    let isEmptyTypes = (types.length === 0);

    for (const result of results) {
        let type = result.getElementsByClassName("product-type")[0];

        result.hidden = (!types.includes(type.innerText));

        if (isEmptyTypes)
            result.hidden = false;
    }
}