// At Sign Up - Makes sure the fields are correctly fulfilled.
function validateSignupForm() {
    let pass = document.getElementById("password");
    let passRep = document.getElementById("password-repeat");
    let correctPass = false;

    let selectMenu = document.getElementById("selectMenu");
    let roleOption = selectMenu.options[selectMenu.selectedIndex].text;
    let correctOption = false;

    if (pass.value !== passRep.value) {
        pass.style.border = "2px solid red";
        passRep.style.border = "2px solid red";
        document.getElementById("error-message").innerText = "Passwords don't match. ";
    } else {
        pass.style.border = "";
        passRep.style.border = "";
        document.getElementById("error-message").innerText = "";
        correctPass = true;
    }

    if (roleOption === 'Choose your role') {
        selectMenu.style.border = "2px solid red";
    } else {
        selectMenu.style.border = "";
        correctOption = true;
    }

    if (correctPass && correctOption) {
        pass.style.border = "";
        passRep.style.border = "";
        selectMenu.style.border = "";
        document.getElementsByTagName('form')[0].submit();
    }
}

// Makes the navbar fixed when scroll
document.addEventListener("DOMContentLoaded", function () {
    window.addEventListener('scroll', function () {
        if (window.scrollY > 0) {
            document.getElementById('navbar_top').classList.add('fixed-top');
            // add padding top to show content behind navbar
            let navbar_height = document.querySelector('.navbar').offsetHeight;
            document.body.style.paddingTop = navbar_height + 'px';
        } else {
            document.getElementById('navbar_top').classList.remove('fixed-top');
            // remove padding top from body
            document.body.style.paddingTop = '0';
        }
    });
});

// Password update
function checkPasswordValidity() {
    let pass = document.getElementById("new_password");
    let passConf = document.getElementById("confirm_new_password");

    if (pass.value !== passConf.value) {
        pass.style.border = "2px solid red";
        passConf.style.border = "2px solid red";
        document.getElementById("error-message").innerText = "Passwords don't match. ";
    } else {
        pass.style.border = "";
        passConf.style.border = "";
        document.getElementById("error-message").innerText = "";
        document.getElementsByTagName('form')[0].submit();
    }
}

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

// Rate with stars
jQuery(document).ready(function ($) {
    $(".btn-rating").on('click', (function () {

        let previous_value = $("#selected_rating").val();

        let selected_value = $(this).attr("data-attr");
        $("#selected_rating").val(selected_value);

        $(".selected-rating").empty().html(selected_value);

        for (let i = 1; i <= selected_value; ++i) {
            $("#rating-star-" + i).toggleClass('btn-warning').toggleClass('btn-default');
        }

        for (let ix = 1; ix <= previous_value; ++ix) {
            $("#rating-star-" + ix).toggleClass('btn-warning').toggleClass('btn-default');
        }
    }));
});

// Get filter results
function getUrlParameter(sParam) {
    let sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
    return "";
};