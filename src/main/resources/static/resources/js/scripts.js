// At Sign Up - Makes sure the fields are correctly fulfilled.
function validateSignupForm() {
    let pass = document.getElementById("password");
    let passRep = document.getElementById("password-repeat");
    let correctPass = false;

    let authorityBox = document.getElementById("authorities");
    let authorities = $("input:checkbox:checked");
    let checkedAuth = false;

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

    if (authorities.length > 0) {
        authorityBox.style.border = "";
        checkedAuth = true;
    } else {
        authorityBox.style.border = "2px solid red";
    }

    if (correctPass && checkedAuth) {
        pass.style.border = "";
        passRep.style.border = "";
        authorityBox.style.border = "";
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

    //Rest API Call for add an object to the favorite list
    $("#add-favorite").click(function () {
        let url = window.location.pathname + "/add-favorite";

        $.get(url, function () {
            document.getElementById("add-favorite").hidden = true;
            document.getElementById("remove-favorite").hidden = false;
        });
    });

    //Rest API Call for remove an object from the favorite list
    $("#remove-favorite").click(function () {
        let url = window.location.pathname + "/remove-favorite";

        $.get(url, function () {
            document.getElementById("add-favorite").hidden = false;
            document.getElementById("remove-favorite").hidden = true;
        });
    });
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
}

// Add object to favorite
function addFavorite(element) {
    let id = element.id.replace("add-favorite-", "");
    let url = (window.location.pathname + "/" + id + "/add-favorite")
                .replace("favorites/", "")
                .replace("//", "/");

    $.get(url, function () {
        document.getElementById("add-favorite-" + id).hidden = true;
        document.getElementById("remove-favorite-" + id).hidden = false;
    });
}

// Remove object from favorite
function removeFavorite(element) {
    let id = element.id.replace("remove-favorite-", "");
    let url = (window.location.pathname + "/" + id + "/remove-favorite")
                .replace("favorites/", "")
                .replace("//", "/");

    $.get(url, function () {
        document.getElementById("add-favorite-" + id).hidden = false;
        document.getElementById("remove-favorite-" + id).hidden = true;
    });
}

// Checks if at least one race type is selected (races/new, races/update)
function validateRaceForm() {
    let raceTypes = $("input:checkbox");
    let checked = false;
    raceTypes.map(function () {
        if ($(this).is(":checked")) {
            checked = true;
            document.getElementById("error-message").innerText = "";
            document.getElementById("checkboxes").style.border = "";
            document.getElementsByTagName('form')[0].submit();
        }
    })

    if (!checked) {
        document.getElementById("error-message").innerText = "You have to choose at least one. ";
        document.getElementById("checkboxes").style.border = "2px solid red";
        window.scrollTo(0, 0);
    }

}

function findRacesByFilters() {
    let finalUrl = "/races?";

    let distances = document.querySelectorAll("input[id^='distance-']:checked");

    if (distances.length > 0) {
        let distanceUrl = "distance=";
        for (const distance of distances) {
            distanceUrl += (distanceUrl === "distance=")? distance.value: "," + distance.value;
        }
        finalUrl += distanceUrl;
    }

    let fields = document.querySelectorAll("input[id^='field-']:checked");

    if (fields.length > 0) {
        let fieldUrl = distances.length > 0? "&field=": "field=";
        for (const field of fields) {
           fieldUrl += (fieldUrl.endsWith("field="))? field.value: "," + field.value;
        }

        finalUrl += fieldUrl;
    }

    let date = $('#date-range').val();

    if (date.split(" - ")[0] !== date.split(" - ")[1]) {
        if (distances.length > 0)
            finalUrl += "&";
        finalUrl += "date-range=" + date;
    }

    let sort = $("#sort").val();

    if (sort !== 'none') {
        finalUrl += "&sort=" + sort;
    }

    window.location.href = finalUrl;
}

function findTrainingPlansByFilters() {
    let finalUrl = "/training-plans?";
    let distances = document.querySelectorAll("input[id^='type-']:checked");
    let fields = document.querySelectorAll("input[id^='field-']:checked");
    let experiences = document.querySelectorAll("input[id^='exp-']:checked");

    [].forEach.call(distances, function (element) {
       finalUrl += finalUrl === "/training-plans?"? "distance=" + element.value: "," + element.value;
    });

    [].forEach.call(fields, function (element) {
       finalUrl += !finalUrl.includes("field")? (finalUrl === "/training-plans?"? "field=": "&field=") + element.value: "," + element.value;
    });

    [].forEach.call(experiences, function (element) {
        finalUrl += !finalUrl.includes("experience")? (finalUrl === "/training-plans?"? "experience=": "&experience=") + element.value: "," + element.value;
    });

    window.location.href = finalUrl;
}

function findProductsByFilters() {
    let finalUrl = "/products?";
    let minPrice = $("#min-price");
    let maxPrice = $("#max-price")

    $(".product-category").map(function () {
        if ($(this).is(":checked"))
            finalUrl += finalUrl.includes("category") ? "," + this.value : "category=" + this.value;
    });

    let typeUrl = finalUrl.includes("category")? "&": "";

    $(".product-type").map(function () {
        if ($(this).is(":checked"))
            typeUrl += typeUrl.includes("type") ? "," + this.value : "type=" + this.value;
    });

    if (typeUrl.includes("type"))
        finalUrl += typeUrl;

    let rateUrl = finalUrl === "/products?"? "rate=": "&rate=";
    $('input[name="rate"]').map(function() {
        if ($(this).is(":checked")) {
            rateUrl += this.value + ",5";
            finalUrl += rateUrl;
        }
    });

    let priceUrl = finalUrl.includes("type") ? "&price=" : "price=";

    if ($(minPrice).val() && $(maxPrice).val())
        finalUrl += priceUrl + $(minPrice).val() + "," + $(maxPrice).val();
    else if (!$(minPrice).val() && $(maxPrice).val())
        finalUrl += priceUrl + "0," + $(maxPrice).val();
    else if ($(minPrice).val() && !$(maxPrice).val())
        if ($(minPrice).val() > 0)
            finalUrl += priceUrl + $(minPrice).val() + ",100000";

    let order = $("#order").val();

    if (order !== 'none') {
        finalUrl += "&order=" + order;
    }

    window.location.href = finalUrl;
}

function changeViewAs(viewType) {
    Array.prototype.forEach.call(document.getElementsByClassName("active"), function (elem) {
        elem.classList.remove("active");
    });

    document.cookie = "VIEW-AS=" + viewType.id.toUpperCase();
    viewType.classList.add("active");

    window.location.href = "/";
}

function fillRaceFiltersByUrlParams() {
    let dateRange = getUrlParameter("date-range");
    let distanceParam = getUrlParameter("distance");
    let fieldParam = getUrlParameter("field");

    if (dateRange.length > 0)
        $('#date-range').val(dateRange);

    if (distanceParam.length > 0) {
        let distances = distanceParam.split(',');

        $("input:checkbox").map(function () {
            if (distances.includes(this.value)) {
                this.checked = true;
            }
        })
    }

    if (fieldParam.length > 0) {
        let fields = fieldParam.split(',');
        $("input:checkbox").map(function () {
            if (fields.includes(this.value)) {
                this.checked = true;
            }
        })
    }

    $(function () {
        $('input[name="date-range"]').daterangepicker({
            opens: 'left'
        });
    });
}

function fillTrainingPlanFiltersByUrlParams() {
    console.log("EP");
    let distances = getUrlParameter("distance");
    let fieldOptions = getUrlParameter("field");
    let experiences = getUrlParameter("experience");

    if (distances.length > 0) {
        let types = distances.split(',');

        $("input[id^='type-']").map(function () {
            if (types.includes(this.value)) {
                this.checked = true;
            }
        });
    }

    if (fieldOptions.length > 0) {
        let fields = fieldOptions.split(',');

        $("input[id^='field-']").map(function () {
            if (fields.includes(this.value)) {
                this.checked = true;
            }
        });
    }

    if (experiences.length > 0) {
        let exps = experiences.split(',');

        $("input[id^='exp-']").map(function () {
            if (exps.includes(this.value)) {
                this.checked = true;
            }
        })
    }
}

function fillProductsFiltersByUrlParams() {
    let categories = getUrlParameter("category").split(",");
    let types = getUrlParameter("type").split(",");
    let rates = getUrlParameter("rate").split(",");
    let prices = getUrlParameter("price").split(",");

    $('input[name="type"]').map(function () {
        if (types.includes(this.value)) {
            this.checked = true;
        }
    });

    $('input[name="category"]').map(function () {
        if (categories.includes(this.value)) {
            this.checked = true;
            $('#' + this.value.toLowerCase() + '-types').toggle();
        }
    });

    $('input[name="rate"]').map(function () {
       if (rates[0] === this.value) {
           this.checked = true;
       }
    });

    if (prices.length === 2) {
        $('#min-price').val(prices[0]);
        $('#max-price').val(prices[1]);
    }
}

function setFavorites(favorites) {
    [].forEach.call(document.querySelectorAll('[id^="add-favorite-"]'), function (element) {
        let id = Number(element.id.replace("add-favorite-", ""));
        if (favorites.includes(id)) {
            document.getElementById("add-favorite-" + id).hidden = true;
            document.getElementById("remove-favorite-" + id).hidden = false;
        } else {
            document.getElementById("add-favorite-" + id).hidden = false;
            document.getElementById("remove-favorite-" + id).hidden = true;
        }
    });
}

function showHideFilters() {
    let filters = document.getElementById('filters');
    let trigger = document.getElementById('triggerFilters');
    let order = document.getElementById('order');

    filters.hidden = !filters.hidden;
    if (order !== null)
        order.hidden = !order.hidden;
    trigger.innerText = filters.hidden? 'Show Filters': 'Hide Filters';
}

// GET Request for Notifications
function createNotifications() {
    function notificationHTML(title, body, id, url) {
        return  '<li class="dropdown-item notification" >\n' +
                '<div class="row">\n' +
                '<div class="col-11">' +
                '<a style="text-decoration: none; color: white" href="' + url + '">\n' +
                '<strong class="notification_span">' + title + '</strong>\n' +
                '<span class="notification_span">' + body + '</span>\n' +
                '</a>\n' +
                '</div>\n' +
                '<button type="button" class="col-1 btn-light btn-close ms-auto delete-notif" style="width: 19px" id="' + id + '" onclick="deleteNotification(this)" aria-label="Close"></button>\n' +
                '</div> \n' +
                '</li>';
    }

    let wrapper = $('#notification-wrapper');

    $.get("/notifications", function(notifications){
        if (notifications.length === 0) {
            wrapper.append("<li class='dropdown-item-text'>0 Notifications</li>")
        }

        for (let notification of notifications) {
            let currentNotification = notificationHTML(notification.title, notification.body, notification.id, notification.url);
            if (!wrapper.html().includes(currentNotification))
                wrapper.append(currentNotification);
        }

        wrapper.append("<li><hr class='dropdown-divider'></li>");
        wrapper.append('<li><a class="dropdown-item" href="/profile?p=notifications">Show All</a></li>');
    })
    .fail(function() {
        wrapper.append("<li class='dropdown-item'>Failed Loading Notifications</li>");
    });
}

function deleteNotification(notification) {
    $.get("/notifications/delete/" + notification.id, function () {
        $("#" + notification.id).parent().parent().remove();
        if (window.location.href.includes('/profile'))
            $("#" + notification.id).parent().parent().remove();
    });
}

function deleteAllNotifications() {
    $.get("/notifications/delete-all", function () {
        $('#notification-wrapper li.notification').map(function() {
            this.remove();
        });
    });
}
